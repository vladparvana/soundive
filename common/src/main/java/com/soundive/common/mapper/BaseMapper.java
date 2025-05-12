package com.soundive.common.mapper;


import java.util.List;

/**
 * Base Mapper for converting between Entity and DTO objects.
 */
public interface BaseMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> dtos);
}