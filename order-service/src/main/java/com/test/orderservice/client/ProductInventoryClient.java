package com.test.orderservice.client;


import com.test.orderservice.client.productservice.ProductControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductInventoryClient extends ProductControllerApi {

}
