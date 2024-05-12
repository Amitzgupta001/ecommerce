package com.test.productservice.service.impl;

import com.test.productservice.dto.request.InventoryUpdateRequest;
import com.test.productservice.dto.response.InventoryResponse;
import com.test.productservice.entity.Inventory;
import com.test.productservice.entity.Product;
import com.test.productservice.exception.EntityNotFoundException;
import com.test.productservice.exception.NothingToUpdateException;
import com.test.productservice.mapping.InventoryMapping;
import com.test.productservice.repository.InventoryRepository;
import com.test.productservice.repository.ProductRepository;
import com.test.productservice.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryMapping inventoryMapping;


    @Override
    @Transactional
    @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    public InventoryResponse addInventory(InventoryUpdateRequest inventoryUpdateRequest) {

        log.info("Started: creating addInventory for categoryRequest: {}", inventoryUpdateRequest);
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductProductId(inventoryUpdateRequest.getProductId());
        if(inventoryOpt.isEmpty()){
            log.error("Product inventory not found");
            throw new EntityNotFoundException("Product inventory not found");
        }
        Inventory inventory = inventoryOpt.get();
        inventory.setTotalQuantity(inventory.getTotalQuantity() + inventoryUpdateRequest.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        InventoryResponse inventoryResponse = inventoryMapping.entityToResponse(updatedInventory);
        log.debug("Ended: Ended addInventory for inventoryUpdateRequest: {} response:{}", inventoryUpdateRequest, inventoryResponse);
        return inventoryResponse;
    }

    @Override
    @Transactional
    @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    public InventoryResponse removeInventory(InventoryUpdateRequest inventoryUpdateRequest) {
        log.info("Started: creating addInventory for categoryRequest: {}", inventoryUpdateRequest);
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductProductId(inventoryUpdateRequest.getProductId());
        if(inventoryOpt.isEmpty()){
            log.error("Product inventory not found");
            throw new EntityNotFoundException("Product inventory not found");
        }
        Inventory inventory = inventoryOpt.get();
        if(inventory.getTotalQuantity() < inventoryUpdateRequest.getQuantity()){
            log.error("Product inventory cant be update");
            throw new NothingToUpdateException("Current Inventory is less");
        }
        inventory.setTotalQuantity(inventory.getTotalQuantity() - inventoryUpdateRequest.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        InventoryResponse inventoryResponse = inventoryMapping.entityToResponse(updatedInventory);
        log.debug("Ended: Ended addInventory for inventoryUpdateRequest: {} response:{}", inventoryUpdateRequest, inventoryResponse);
        return inventoryResponse;
    }

    @Override
    public InventoryResponse getInventory(UUID productId) {
        log.info("Started: creating getInventory for productId: {}", productId);
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductProductId(productId);
        if(inventoryOpt.isEmpty()){
            log.error("Product inventory not found");
            throw new EntityNotFoundException("Product inventory not found");
        }
        Inventory inventory = inventoryOpt.get();
        InventoryResponse inventoryResponse = inventoryMapping.entityToResponse(inventory);
        log.debug("Ended: Ended getInventory for productId: {} response:{}", productId, inventoryResponse);
        return inventoryResponse;
    }

    @Override
    public void intializeInventory(UUID productId) {
        log.info("Started: creating intializeInventory for productId: {}", productId);
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductProductId(productId);
        if(inventoryOpt.isPresent()){
            log.error("Product inventory not found");
            throw new NothingToUpdateException("Product inventory is already present");
        }
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            log.error("Product not found");
            throw new NothingToUpdateException("Product not found");
        }
        Inventory savedInventory = inventoryRepository.save(Inventory.builder().product(productOptional.get()).totalQuantity(0).build());
        InventoryResponse inventoryResponse = inventoryMapping.entityToResponse(savedInventory);
        log.debug("Ended: Ended intializeInventory for productId: {} response:{}", productId, inventoryResponse);
    }
}
