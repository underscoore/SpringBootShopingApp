package com.shoppingapplication.orderservice.service;

import com.shoppingapplication.orderservice.dto.InventoryResponse;
import com.shoppingapplication.orderservice.dto.OrderLineItemsDto;
import com.shoppingapplication.orderservice.dto.OrderRequest;
import com.shoppingapplication.orderservice.model.Order;
import com.shoppingapplication.orderservice.model.OrderLineItems;
import com.shoppingapplication.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //Call Inventory service
        //Place order if in stock
        List<InventoryResponse> inventoryResponsesArray = webClient.get()
                .uri("http://localhost:8083/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<InventoryResponse>>() {})
                .block();

        List<String> skuPresentInList = new ArrayList<>();
        for (InventoryResponse inventoryResponse : inventoryResponsesArray) {
            String skuCode = inventoryResponse.getSkuCode();
            skuPresentInList.add(skuCode);
        }

        boolean allProductsInStock = inventoryResponsesArray.stream()
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock){
           order.setOrderLineItemsList( order.getOrderLineItemsList().stream().filter(orderLineItemsList->skuPresentInList.contains(orderLineItemsList.getSkuCode())).toList());
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, Please try again later.");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
