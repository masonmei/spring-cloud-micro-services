package com.igitras.mvc.rest;

import com.igitras.mvc.dto.UserDto;

import java.util.List;

/**
 * Created by mason on 4/12/16.
 */
public class MergedResponse extends UserDto {

    public MergedResponse(UserDto userDto) {
        setUsername(userDto.getUsername());
        setAlias(userDto.getAlias());
    }

    public MergedResponse() {
    }

    private List<String> actions;

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}
