package com.igitras.mvc.rest;

import com.igitras.mvc.dto.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

/**
 * Created by mason on 4/11/16.
 */
@RestController
@RequestMapping("users")
public class UserController {

    private List<UserDto> users = new ArrayList<>(100);

    @PostConstruct
    public void init() {
        for (int i = 0; i < 10; i++) {
            UserDto userDto = new UserDto();
            userDto.setUsername("UserName" + i);
            userDto.setAlias("别名" + i);
            users.add(userDto);
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> search(@RequestParam(value = "query", required = false, defaultValue = "") String query) {
        System.out.println(query);
        return users.stream()
                .filter(userDto -> userDto.getUsername().contains(query))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    public UserDto find(@PathVariable("name")String name){
        for (UserDto user : users) {
            if(user.getUsername().equals(name)){
                return user;
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserDto create(@RequestBody UserDto userDto){
        for (UserDto user : users) {
            if(user.getUsername().equals(userDto.getUsername())){
                throw new IllegalArgumentException("User already exist.");
            }
        }
        users.add(userDto);
        return  userDto;
    }

    @RequestMapping(value = "{name}",method = RequestMethod.PUT)
    public UserDto update(@PathVariable("name")String name, @RequestBody UserDto userDto){
        for (UserDto user : users) {
            if(user.getUsername().equals(name)) {
                users.remove(user);
                users.add(userDto);
                return userDto;
            }
        }
        throw new IllegalArgumentException("User not exist.");
    }

    @RequestMapping(value = "{name}",method = RequestMethod.DELETE)
    public UserDto remove(@PathVariable("name")String name) {

        for (UserDto user : users) {
            if(user.getUsername().equals(name)) {
                users.remove(user);
                return user;
            }
        }

        throw new IllegalArgumentException("Not found");
    }


}

