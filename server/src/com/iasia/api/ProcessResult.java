package com.iasia.api;

public class ProcessResult<T> {

    public final T result;
    public final long elapsed;

    public ProcessResult(T result, long elapsed) {
        this.result = result;
        this.elapsed = elapsed;
    }
}
