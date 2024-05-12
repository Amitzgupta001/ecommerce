package com.test.productservice.service.impl;

import com.test.productservice.common.NullChecker;
import com.test.productservice.dto.request.ProductRequest;
import com.test.productservice.dto.response.PagingResponse;
import com.test.productservice.dto.response.ProductResponse;
import com.test.productservice.entity.Category;
import com.test.productservice.entity.Product;
import com.test.productservice.exception.EntityNotFoundException;
import com.test.productservice.exception.NothingToUpdateException;
import com.test.productservice.mapping.ProductMapping;
import com.test.productservice.repository.CategoryRepository;
import com.test.productservice.repository.ProductRepository;
import com.test.productservice.service.InventoryService;
import com.test.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapping productMapping;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productDto) {
        log.info("Started: creating createProduct for productDto: {}", productDto);
        Optional<Category> categoryOptional = categoryRepository.findById(productDto.getCategoryId());
        if(categoryOptional.isEmpty()){
            throw new EntityNotFoundException("category not found");
        }
        Product producttoSave = productMapping.requestToEntity(productDto);
        producttoSave.setCategory(categoryOptional.get());
        Product savedProduct = productRepository.save(producttoSave);
        ProductResponse productResponse = productMapping.entityToResponse(savedProduct);
        inventoryService.intializeInventory(productResponse.getProductId());
        log.debug("Ended: Ended createProduct for productDto: {} response:{}", productDto, productResponse);
        return productResponse;
    }

    @Override
    public PagingResponse<ProductResponse> getProductsByCategoryId(UUID categoryId, Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        log.info("Started: getting getProductsByCategoryId for categoryId: {}", categoryId);
        this.validateCategoryExist(categoryId);
        Page<Product> productPage = this.findAllPagingByCategory(categoryId, pageNo, pageSize, sortBy, sortDir);
        List<ProductResponse> productResponses = productMapping.entityToResponseList(productPage.getContent());
        PagingResponse<ProductResponse> pageResponse = PagingResponse.<ProductResponse>builder().pageNumber(productPage.getNumber())
                .totalItems(productPage.getNumberOfElements()).totalPages(productPage.getTotalPages()).content(productResponses).build();
        log.debug("Ended: Ended getProductsByCategoryId for categoryId: {} response:{}", categoryId, pageResponse);
        return pageResponse;
    }

    @Override
    public ProductResponse getProductById(UUID productId) {
        log.info("Started: creating getProductById for productId: {}", productId);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()){
            throw new EntityNotFoundException("Product not found");
        }
        ProductResponse productResponse = productMapping.entityToResponse(optionalProduct.get());
        log.debug("Ended: Ended createProduct for productId: {} response:{}", productId, productResponse);
        return productResponse;
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductRequest productDto) {
        log.info("Started: creating updateProduct for productId:{} productDto: {}",productId, productDto);
        validateProduct(productId, productDto);
        Product existProduct = productRepository.findById(productId).get();
        Product product = productMapping.updateProduct(existProduct, productDto);
        product.setProductId(productId);
        Product savedProduct = productRepository.save(product);
        ProductResponse productResponse = productMapping.entityToResponse(savedProduct);
        log.debug("Ended: Ended updateProduct for categoryRequest: {} response:{}", productDto, productResponse);
        return productResponse;
    }

    private void validateProduct(UUID productId, ProductRequest productDto) {
        boolean isExists = productRepository.existsById(productId);
        if(!isExists){
            throw new EntityNotFoundException("Product Not Found");
        }

        if(NullChecker.allNull(productDto)){
            throw new NothingToUpdateException("ALl Field is null in request");
        }
    }

    private Page<Product> findAllPagingByCategory(UUID categoryID, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort;
        if (sortDir.equalsIgnoreCase(Sort.Direction.ASC.toString())) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return productRepository.findAllByCategoryCategoryId(categoryID, pageable);
    }

    private void validateCategoryExist(UUID categoryId) {
        boolean isExists = categoryRepository.existsById(categoryId);
        if(!isExists){
            throw new EntityNotFoundException("Category Not Found");
        }
    }
}
