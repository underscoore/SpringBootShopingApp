package com.shoppingapplication.inventoryservice.service;

import com.shoppingapplication.inventoryservice.dto.InventoryRequest;
import com.shoppingapplication.inventoryservice.model.Inventory;
import com.shoppingapplication.inventoryservice.model.InventoryResponse;
import com.shoppingapplication.inventoryservice.repository.InventoryRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).toList();
        
    }

    @Transactional
    public void createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();

        inventoryRepository.save(inventory);
        log.info("Inventory SkuCode: {} and Quantity {} is Saved", inventory.getSkuCode(), inventory.getQuantity());
    }
}
