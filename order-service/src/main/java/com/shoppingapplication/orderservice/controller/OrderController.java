package com.shoppingapplication.orderservice.controller;

import com.shoppingapplication.orderservice.dto.OrderRequest;
import com.shoppingapplication.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        System.out.println(orderRequest);
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully!";

    }
}
