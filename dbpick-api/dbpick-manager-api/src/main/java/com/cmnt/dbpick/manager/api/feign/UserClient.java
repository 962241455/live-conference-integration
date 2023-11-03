package com.cmnt.dbpick.manager.api.feign;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user", fallbackFactory = UserClientFallbackFactory.class, path = "/service")
public interface UserClient {


}
