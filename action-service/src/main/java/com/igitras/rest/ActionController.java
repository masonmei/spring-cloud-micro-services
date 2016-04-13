package com.igitras.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mason on 4/11/16.
 */
@RestController
@RequestMapping("users/{username}/actions")
public class ActionController {

    @RequestMapping(method = RequestMethod.GET)
    public List<String> search(
            @PathVariable("username")
                    String username) {
        return Arrays.asList("Test", "DO", username);
    }

}

