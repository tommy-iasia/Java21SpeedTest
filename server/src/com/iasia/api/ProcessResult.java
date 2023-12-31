package com.iasia.api;

public record ProcessResult<T>(T result, long elapsed) {}
