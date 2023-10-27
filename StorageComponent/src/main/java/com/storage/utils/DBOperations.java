package com.storage.utils;


public interface DBOperations<T> {
    public void set(T object);
    public T get(String key);
}
