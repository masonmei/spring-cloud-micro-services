package com.igitras.service;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by mason on 4/12/16.
 */
@FeignClient(name = "actionClient", url = "http://localhost:8868")
public interface ActionService {

    @RequestLine("GET /users/{name}/actions")
    List<String> actions(@Param("name") String username);
}
