package com.simple_crud.ms.service;

public interface IFindService<T, I> {
    T findById(I id);
}
