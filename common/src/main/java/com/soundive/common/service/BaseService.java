package com.soundive.common.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic service interface for CRUD operations.
 */
public interface BaseService<D, ID> {

    // Create a new entity
    D create(D dto);

    // Find an entity by its ID
    Optional<D> findById(ID id);

    // Retrieve all entities
    List<D> findAll();

    // Update an existing entity
    D update(ID id, D dto);

    // Soft delete an entity by its ID
    void delete(ID id);
}