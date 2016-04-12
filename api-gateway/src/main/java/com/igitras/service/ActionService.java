package com.igitras.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by mason on 4/12/16.
 */
@FeignClient(url = "http://localhost:8868")
@RequestMapping("users/{username}/action")
public interface ActionService {

    @RequestMapping(method = RequestMethod.GET)
    public List<String> actions(@PathVariable("username") String username);
}
