package com.sr.electronic.store.Electronic_Store.services.implementaions;

import com.sr.electronic.store.Electronic_Store.dtos.CategoryDtos;
import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.entities.Category;
import com.sr.electronic.store.Electronic_Store.exceptions.ResourceNOtFoundException;
import com.sr.electronic.store.Electronic_Store.helper.Helper;
import com.sr.electronic.store.Electronic_Store.repositories.CategoryRepository;
import com.sr.electronic.store.Electronic_Store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${category.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDtos create(CategoryDtos categoryDtos) {
        //Creating categoryId randomly

        String categoryId = UUID.randomUUID().toString();
        categoryDtos.setCategoryId(categoryId);

        Category category = mapper.map(categoryDtos, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return mapper.map(savedCategory, CategoryDtos.class);
    }

    @Override
    public CategoryDtos update(CategoryDtos categoryDtos, String categoryId) {
        //get Category of given ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNOtFoundException("Category not found with Given Id !!"));

        //update category Details
        category.setTitle(categoryDtos.getTitle());
        category.setDescription(categoryDtos.getDescription());
        category.setCoverImage(categoryDtos.getCoverImage());
        Category saved = categoryRepository.save(category);

        return mapper.map(saved, CategoryDtos.class);
    }

    @Override
    public void delete(String categoryId) {
        //get by Id first
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNOtFoundException("Category not found with Given Id !!"));
        //delete Category cover image
        String fullPath = imagePath + category.getCoverImage();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Category Cover Image not found in folder");
            ex.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public PageableResponse<CategoryDtos> getAll(int pageNumber, int pageSize,String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDtos> pageableResponse = Helper.getPageableResponse(categoryPage, CategoryDtos.class);

        return pageableResponse;
    }

    @Override
    public CategoryDtos get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNOtFoundException("Category not found with Given Id !!"));
        return mapper.map(category, CategoryDtos.class);
    }
}
