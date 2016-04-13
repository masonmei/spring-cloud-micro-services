package com.igitras.mvc.rest;

/**
 * @author mason
 */
@FunctionalInterface
public interface Decorator<T> {

    void decorate(T component);
}
