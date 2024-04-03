package com.sr.electronic.store.Electronic_Store.controllers;

import com.sr.electronic.store.Electronic_Store.dtos.*;
import com.sr.electronic.store.Electronic_Store.services.CategoryService;
import com.sr.electronic.store.Electronic_Store.services.FileService;
import com.sr.electronic.store.Electronic_Store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    //create
    @PostMapping
    public ResponseEntity<CategoryDtos> createCategory(@Valid @RequestBody CategoryDtos categoryDtos){
        //call Service to save object
        CategoryDtos categoryDtos1 = categoryService.create(categoryDtos);
        return new ResponseEntity<>(categoryDtos, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDtos> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryDtos categoryDtos)
    {
        CategoryDtos updated = categoryService.update(categoryDtos, categoryId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted Successfully !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDtos>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title",required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "0",required = false)String sortDir
    ){
        PageableResponse<CategoryDtos> all = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDtos> getSingle(@PathVariable String categoryId){
        CategoryDtos categoryDtos = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public  ResponseEntity<ImageResponse> uploadCategoryCoverImage(@RequestParam("coverImage") MultipartFile image,
                                                                   @PathVariable String categoryId) throws IOException {
        String imageName = fileService.uploadImage(image, imageUploadPath);
        CategoryDtos category = categoryService.get(categoryId);
        category.setCoverImage(imageName);
        CategoryDtos categoryDtos = categoryService.update(category, categoryId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image is Uploaded Successfully").success(true).status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }


    //serve user image
    @GetMapping("image/{categoryId}")
    public void serveImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDtos category = categoryService.get(categoryId);
        logger.info("cover Image Name : {}",category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    // create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryId") String categoryId,
                                                                @RequestBody ProductDto productDto)
    {
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    //update category of product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String categoryId,
                                                              @PathVariable String productId)
    {

        // It is important that we put productId  1st and category Id 2nd because in service implementation the order is this given below
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title",required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "0",required = false)String sortDir)
    {
        PageableResponse<ProductDto> allOfCategory = productService.getAllOfCategory(categoryId,pageSize,pageNumber,sortBy,sortDir);
        return new ResponseEntity<>(allOfCategory,HttpStatus.OK);
    }

}
