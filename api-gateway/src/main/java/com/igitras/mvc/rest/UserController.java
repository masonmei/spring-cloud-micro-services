package com.igitras.mvc.rest;

import com.igitras.mvc.dto.UserDto;
import com.igitras.service.ActionService;
import com.igitras.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

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
    public DeferredResult<List<MergedResponse>> search() {
        DeferredResult<List<MergedResponse>> deferredResult = new DeferredResult<>();
        Observable.from(userService.search())
                .doOnNext(mergedResponse -> {
                    List<String> actions = actionService.actions(mergedResponse.getUsername());
                    mergedResponse.setActions(actions);
                })
                .toList()
                .subscribe(deferredResult::setResult);
        return deferredResult;
    }

    @RequestMapping(value = "{name}",
            method = RequestMethod.GET)
    public MergedResponse find(@PathVariable("name") String name) {
        Observable<MergedResponse> userDtoObservable = Observable.create(new Observable.OnSubscribe<MergedResponse>() {
            @Override
            public void call(Subscriber<? super MergedResponse> subscriber) {
                subscriber.onNext(userService.find(name));
            }
        });

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
