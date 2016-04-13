package com.igitras.service;

import com.igitras.mvc.dto.UserDto;
import com.igitras.mvc.rest.MergedResponse;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.util.List;
import java.util.Map;

/**
 * Created by mason on 4/11/16.
 */
@FeignClient(name = "userClient", url = "http://localhost:8878")
public interface UserService {

    @RequestLine("GET /users")
    @Headers({"username: {username}"})
    List<MergedResponse> search(@Param(value = "username") String username,
            @QueryMap Map<String, List<String>> queryMap);

    @RequestLine("GET /users/{name}")
    MergedResponse find(@Param("name") String name);

    @RequestLine("POST /users")
    MergedResponse create(UserDto userDto);

    @RequestLine("PUT /users/{name}")
    UserDto update(@Param("name") String name, UserDto userDto);

    @RequestLine("DELETE /users/{name}")
    UserDto remove(@Param("name") String name);

}
