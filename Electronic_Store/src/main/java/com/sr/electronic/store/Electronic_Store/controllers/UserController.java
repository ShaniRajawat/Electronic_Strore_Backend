package com.sr.electronic.store.Electronic_Store.controllers;

import com.sr.electronic.store.Electronic_Store.dtos.ApiResponseMessage;
import com.sr.electronic.store.Electronic_Store.dtos.ImageResponse;
import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.UserDto;
import com.sr.electronic.store.Electronic_Store.exceptions.MaxUploadSizeExceededException;
import com.sr.electronic.store.Electronic_Store.services.FileService;
import com.sr.electronic.store.Electronic_Store.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name="UserController", description ="REST APIs related to perform user operations!!")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,
                                              @Valid @RequestBody UserDto userDto)
    {
        UserDto userDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User is Deleted Successfully !!")
                .success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //get all
    @GetMapping
    @Operation(summary="Get all Users !!", description ="REST APIs to Get ALL Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success | Ok"),
            @ApiResponse(responseCode = "401", description = "not Authorized"),
            @ApiResponse(responseCode = "201", description = "New User Created")
    })
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableResponse<UserDto>> getallUser(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc", required = false) String sortDir)
    {
        PageableResponse<UserDto> allUser = userService.getAllUser(pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allUser,HttpStatus.OK);
    }

    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getByUserId(@PathVariable("userId") String userId){
        UserDto user = userService.getByUserId(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email){
        UserDto user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> SearchUser(@PathVariable("keywords") String keywords){
        List<UserDto> users = userService.searchUser(keywords);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public  ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile image,
                                                          @PathVariable String userId) throws IOException{
        String imageName = fileService.uploadImage(image, imageUploadPath);
        UserDto user = userService.getByUserId(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image is Uploaded Successfully").success(true).status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }


    //serve user image
    @GetMapping("image/{userId}")
    public void serveImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getByUserId(userId);
        logger.info("User Image Name : {}",user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
