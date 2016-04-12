package com.igitras.service;

import com.igitras.mvc.dto.UserDto;
import com.igitras.mvc.rest.MergedResponse;
import com.netflix.ribbon.RibbonRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by mason on 4/11/16.
 */
@FeignClient(url = "http://localhost:8878")
@RequestMapping("users")
public interface UserService {

    @RequestMapping(method = RequestMethod.GET)
    RibbonRequest<List<UserDto>> search();

    @RequestMapping(value = "{name}",
                    method = RequestMethod.GET)
    public MergedResponse find(
            @PathVariable("name")
                    String name);

//    @RequestMapping(method = RequestMethod.POST)
//    public UserDto create(@RequestBody UserDto userDto);
//
//    @RequestMapping(value = "{name}",method = RequestMethod.PUT)
//    public UserDto update(@PathVariable("name")String name, @RequestBody UserDto userDto);
//
//    @RequestMapping(value = "{name}",method = RequestMethod.DELETE)
//    public UserDto remove(@PathVariable("name")String name) ;

}
