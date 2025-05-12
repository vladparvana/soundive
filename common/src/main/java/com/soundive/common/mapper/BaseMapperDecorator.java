package com.soundive.common.mapper;

import com.soundive.common.annotation.ExcludeFromDto;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic BaseMapper Decorator to handle field exclusions marked with @ExcludeFromDto.
 */
@Slf4j
public abstract class BaseMapperDecorator<E, D> implements BaseMapper<E, D> {

    protected BaseMapper<E, D> delegate;

    public void setDelegate(BaseMapper<E, D> delegate) {
        this.delegate = delegate;
    }


    @Override
    public D toDto(E entity) {
        D dto = delegate.toDto(entity);
        excludeFromDtoFields(entity, dto);
        return dto;
    }

    @Override
    public E toEntity(D dto) {
        return delegate.toEntity(dto); // Nu excludem nimic aici
    }

    @Override
    public List<D> toDtoList(List<E> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<E> toEntityList(List<D> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Exclude fields annotated with @ExcludeFromDto using reflection.
     */
    protected void excludeFromDtoFields(E entity, D dto) {
        if (entity == null || dto == null) return;

        Class<?> currentClass = entity.getClass();
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcludeFromDto.class)) {
                    try {
                        field.setAccessible(true);
                        Field dtoField = dto.getClass().getDeclaredField(field.getName());
                        dtoField.setAccessible(true);
                        dtoField.set(dto, null);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        System.out.println("Error processing field: " + field.getName() + " - " + e.getMessage());
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }
}