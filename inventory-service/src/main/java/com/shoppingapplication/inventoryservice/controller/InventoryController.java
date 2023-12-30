package com.shoppingapplication.inventoryservice.controller;

import com.shoppingapplication.inventoryservice.dto.InventoryRequest;
import com.shoppingapplication.inventoryservice.model.InventoryResponse;
import com.shoppingapplication.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.createInventory(inventoryRequest);
        return "New Inventory item is Created!";
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        System.out.println(skuCode);
        return inventoryService.isInStock(skuCode);
    }
}
