package com.test.orderservice.client;

import com.test.orderservice.client.productservice.UserControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "USER-SERVICE")
public interface UserClient extends UserControllerApi {

}
