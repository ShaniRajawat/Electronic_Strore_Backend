package com.sr.electronic.store.Electronic_Store.controllers;

import com.sr.electronic.store.Electronic_Store.dtos.*;
import com.sr.electronic.store.Electronic_Store.services.FileService;
import com.sr.electronic.store.Electronic_Store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String imagePath;

    //create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto){
        ProductDto updatedProduct = productService.update(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product is Successfully Deleted!!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId){
        ProductDto productDto = productService.getSingle(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(@RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
                                                               @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                               @RequestParam(value = "sortBy", defaultValue = "title",required = false)String sortBy,
                                                               @RequestParam(value = "sortDir", defaultValue = "0",required = false)String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(@RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
                                                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = "title",required = false)String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = "0",required = false)String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //Search all
    @GetMapping("/search/{subtitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProductByTitle(@PathVariable String subtitle,
                                                                             @RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
                                                                             @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                                             @RequestParam(value = "sortBy", defaultValue = "title",required = false)String sortBy,
                                                                             @RequestParam(value = "sortDir", defaultValue = "0",required = false)String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(subtitle,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //upload Image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("productImage")MultipartFile image,
                                                            @PathVariable String productId) throws IOException
    {
        String filename = fileService.uploadImage(image, imagePath);
        ProductDto productDto = productService.getSingle(productId);
        productDto.setProductImageName(filename);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName()).message("Product Image is successfully Uploaded !!").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //serve  Image
    @GetMapping("image/{productId}")
    public void serveImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.getSingle(productId);
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
