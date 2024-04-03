package com.sr.electronic.store.Electronic_Store.services.implementaions;

import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.ProductDto;
import com.sr.electronic.store.Electronic_Store.entities.Category;
import com.sr.electronic.store.Electronic_Store.entities.Product;
import com.sr.electronic.store.Electronic_Store.exceptions.ResourceNOtFoundException;
import com.sr.electronic.store.Electronic_Store.helper.Helper;
import com.sr.electronic.store.Electronic_Store.repositories.CategoryRepository;
import com.sr.electronic.store.Electronic_Store.repositories.ProductRepository;
import com.sr.electronic.store.Electronic_Store.services.ProductService;
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
import java.util.Date;
import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${product.profile.image.path}")
    private String imagePath;
    @Autowired
    private CategoryRepository categoryRepository;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);

        //product Id
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);

        //Date
        product.setAddedDate(new Date());

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {

        //fetch the Product with Given Id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNOtFoundException("Product With given Id Not Found"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setBrandName(productDto.getBrandName());
        product.setProductImageName(productDto.getProductImageName());

        //save the entity
        Product saved = productRepository.save(product);
        ProductDto productDto1 = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNOtFoundException("Product With given Id Not Found"));
        //delete product profile image
        String fullPath = imagePath + product.getProductImageName();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Product Image not found in folder");
            ex.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        productRepository.deleteById(productId);
    }

    @Override
    public ProductDto getSingle(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNOtFoundException("Product With given Id Not Found"));
        return modelMapper.map(product, ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(productPage, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(productPage, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByTitleContaining(subTitle, pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(productPage, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        //fetch the Category from db
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNOtFoundException("Category is not Found with Given Id !!"));
        Product product = modelMapper.map(productDto, Product.class);

        //product Id
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);

        //Date
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);

    }

    //update category of Products
    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        // Fetch Category
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNOtFoundException("No Category found with given Id !!"));
        // Fetch Product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNOtFoundException("No Product found with given Id !!"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);

    }

    //get products of given category
    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageSize,int pageNumber,String sortBy,String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNOtFoundException("No Category found with given Id !!"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

}
