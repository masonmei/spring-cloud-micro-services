package com.igitras.mvc.rest;

import com.igitras.mvc.dto.UserDto;
import com.igitras.service.ActionService;
import com.igitras.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

import java.util.List;
import java.util.stream.Collectors;

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
    public Observable<List<MergedResponse>> search() {
        return userService.search().toObservable()
                .flatMap(Observable::from)
                .map(MergedResponse::new)
                .doOnNext(mergedResponse -> mergedResponse.setActions(actionService.actions(mergedResponse.getUsername())))
                .toList();
    }

    @RequestMapping(value = "{name}",
                    method = RequestMethod.GET)
    public MergedResponse find(
            @PathVariable("name")
                    String name) {
        Observable<MergedResponse> userDtoObservable = Observable.create(new Observable.OnSubscribe<MergedResponse>() {
            @Override
            public void call(Subscriber<? super MergedResponse> subscriber) {
                subscriber.onNext(userService.find(name));
            }
        });

        return userService.find(name);
    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public UserDto create(@RequestBody UserDto userDto){
//        return userService.create(userDto);
//    }
//
//    @RequestMapping(value = "{name}",method = RequestMethod.PUT)
//    public UserDto update(@PathVariable("name")String name, @RequestBody UserDto userDto){
//        return userService.update(name, userDto);
//    }
//
//    @RequestMapping(value = "{name}",method = RequestMethod.DELETE)
//    public UserDto remove(@PathVariable("name")String name) {
//        return  userService.remove(name);
//    }

}
