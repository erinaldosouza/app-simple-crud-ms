package com.simple_crud.ms.services;

/**
 *
 * @param <I> - Identifier type
 * @param <T> - Entity type
 */
public interface IAppGenericCrudInterface<T, I> {
    void crate(T object);
    T findById(I id);
    void deleteById(I id);
}
