package com.simple_crud.ms.service;


public interface ISaveService<T, I> {
    T save(T object);
}
