package com.igitras.process;

/**
 * @author mason
 */
public interface ProcessChain<T> {

    public void next(T obj);
}
