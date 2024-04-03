package com.sr.electronic.store.Electronic_Store.services;

import com.sr.electronic.store.Electronic_Store.dtos.CategoryDtos;
import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.entities.Category;

public interface CategoryService {

    //create
    CategoryDtos create(CategoryDtos categoryDtos);

    //update
    CategoryDtos update(CategoryDtos categoryDtos,String categoryId);

    //delete
    void delete(String categoryId);

    //get all
    PageableResponse<CategoryDtos> getAll(int pageNumber, int pageSize,String sortBy, String sortDir);

    //get single category detail
    CategoryDtos get(String categoryId);

    //search
}
