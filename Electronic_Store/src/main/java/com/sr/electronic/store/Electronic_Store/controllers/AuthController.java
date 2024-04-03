package com.sr.electronic.store.Electronic_Store.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sr.electronic.store.Electronic_Store.dtos.JwtRequest;
import com.sr.electronic.store.Electronic_Store.dtos.JwtResponse;
import com.sr.electronic.store.Electronic_Store.dtos.UserDto;
import com.sr.electronic.store.Electronic_Store.entities.User;
import com.sr.electronic.store.Electronic_Store.exceptions.BadApiRequestException;
import com.sr.electronic.store.Electronic_Store.security.JwtHelper;
import com.sr.electronic.store.Electronic_Store.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/auth")
@Tag(name="AuthController", description ="REST APIs related to perform Authentication!!")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @Value("${googleClientId}")
    private String googleClientId;
    @Value("${newPassword}")
    private String newPassword;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getEmail(),request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
        try {
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new BadApiRequestException("Invalid Username or Password !!");
        }
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name),UserDto.class), HttpStatus.OK);
    }

    //Login with google API(Implementation is too Complex)
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {

        //get the id from Request
        String idToken = data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();

        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Payload : {}", payload);

        String email = payload.getEmail();

        User user = null;
        user = userService.findUserByEmailOptional(email).orElse(null);

        if(user == null){
            user=this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
        }

        ResponseEntity<JwtResponse> jwtResponseEntity = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());

        return jwtResponseEntity;

    }

    private User saveUser(String email, String name, String photoUrl) {
        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();
        UserDto userDto = userService.createUser(newUser);

        return this.modelMapper.map(userDto, User.class);
    }

}
