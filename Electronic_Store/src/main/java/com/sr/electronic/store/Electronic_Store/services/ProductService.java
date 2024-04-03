package com.sr.electronic.store.Electronic_Store.services;

import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.ProductDto;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);
    //update
    ProductDto update(ProductDto productDto, String productId);
    //delete
    void delete(String productId);
    //get single
    ProductDto getSingle(String productId);
    //get all
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get all :Live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
    //search Product
    PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);
    //create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);
    //update category of products
    ProductDto updateCategory(String productId, String categoryId);
    //get products of given category
    PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageSize,int pageNumber,String sortBy,String sortDir);

}
