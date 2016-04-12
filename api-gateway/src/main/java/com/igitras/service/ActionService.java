package com.igitras.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by mason on 4/12/16.
 */
@FeignClient(name = "actionService", url = "http://localhost:8868")
public interface ActionService {

    @RequestMapping(value = "users/{name}/actions", method = RequestMethod.GET)
    List<String> actions(@PathVariable("name") String username);
}
