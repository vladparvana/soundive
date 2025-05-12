package com.soundive.common.service.impl;

import com.soundive.common.dto.BaseDto;
import com.soundive.common.entity.PersistableEntity;
import com.soundive.common.exception.EntityNotFoundException;
import com.soundive.common.mapper.BaseMapper;
import com.soundive.common.repository.BaseRepository;
import com.soundive.common.service.BaseService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * Generic service implementation for CRUD operations.
 */
public abstract class BaseServiceImpl<E extends PersistableEntity, D extends BaseDto, ID> implements BaseService<D, ID> {

    protected final BaseRepository<E, ID> repository;
    protected final BaseMapper<E, D> mapper;

    protected BaseServiceImpl(BaseRepository<E, ID> repository, BaseMapper<E, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public D create(@Valid D dto) {
        if(repository.findById((ID) dto.getId()).isPresent())
            throw new EntityNotFoundException("Entity with ID " + dto.getId() + " already exists.");
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public Optional<D> findById(ID id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public List<D> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public D update(ID id, @Valid D dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entity with ID " + id + " not found.");
        }
        dto.setId(id.toString());
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void delete(ID id) {
        repository.findById(id).ifPresent(repository::softDelete);
    }
}