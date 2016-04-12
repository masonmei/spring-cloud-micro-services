package com.igitras.service;

import com.igitras.mvc.dto.UserDto;
import com.igitras.mvc.rest.MergedResponse;
import com.netflix.ribbon.proxy.annotation.Http;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by mason on 4/11/16.
 */
@FeignClient(name = "userService",url = "http://localhost:8878")
@RequestMapping("users")
public interface UserService {

    @RequestMapping(method = RequestMethod.GET, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    List<MergedResponse> search();

    @RequestMapping(value = "{name}",
            method = RequestMethod.GET)
    public MergedResponse find(@PathVariable("name") String name);

    @Headers({"Content-Type: {contentType}"})
    @RequestMapping(method = RequestMethod.POST)
    public MergedResponse create(UserDto userDto);
    //
    //    @RequestMapping(value = "{name}",method = RequestMethod.PUT)
    //    public UserDto update(@PathVariable("name")String name, @RequestBody UserDto userDto);
    //
    //    @RequestMapping(value = "{name}",method = RequestMethod.DELETE)
    //    public UserDto remove(@PathVariable("name")String name) ;

}
