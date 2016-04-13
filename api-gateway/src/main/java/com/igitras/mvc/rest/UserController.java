package com.igitras.mvc.rest;

import com.igitras.mvc.dto.UserDto;
import com.igitras.service.ActionService;
import com.igitras.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by mason on 4/11/16.
 */
@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActionService actionService;

    @RequestMapping(method = RequestMethod.GET)
    public List<MergedResponse> search(@RequestHeader(value = "username", required = false) String username,
            @RequestParam MultiValueMap<String, String> queryMap) {
        DeferredResult<List<MergedResponse>> deferredResult = new DeferredResult<>();


        List<MergedResponse> search = userService.search(username, queryMap);
        decorate(search, (Consumer<MergedResponse>) component -> component.setActions(
                actionService.actions(component.getUsername())));
        return search;
    }

    @RequestMapping(value = "{name}",
            method = RequestMethod.GET)
    public MergedResponse find(@PathVariable("name") String name) {

        return userService.find(name);
    }

    @RequestMapping(value = "{name}/actions", method = RequestMethod.GET)
    public List<String> findActions(@PathVariable("name") String name) {
        return actionService.actions(name);
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @RequestMapping(value = "{name}", method = RequestMethod.PUT)
    public UserDto update(@PathVariable("name") String name, @RequestBody UserDto userDto) {
        return userService.update(name, userDto);
    }

    @RequestMapping(value = "{name}", method = RequestMethod.DELETE)
    public UserDto remove(@PathVariable("name") String name) {
        return userService.remove(name);
    }

    public static <T> void decorate(final List<T> entities, Consumer<T>... decorators) {
        entities.parallelStream()
                .forEach(entity -> decorate(entity, decorators));
    }

    public static <T> void decorate(final T entity, Consumer<T>... decorators) {
        for (Consumer<T> decorator : decorators) {
            decorator.accept(entity);
        }
    }

}
