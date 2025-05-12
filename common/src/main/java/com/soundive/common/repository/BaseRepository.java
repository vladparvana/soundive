package com.soundive.common.repository;

import com.soundive.common.entity.PersistableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Generic base repository for all entities with additional helper methods.
 */
@NoRepositoryBean
public interface BaseRepository<T extends PersistableEntity, ID> extends JpaRepository<T, ID> {

    // Pagination support
    Page<T> findAll(Pageable pageable);

    @Query("SELECT e FROM #{#entityName} e WHERE e.deleted = false")
    Page<T> findAllByNotDeleted(Pageable pageable);


    // Soft delete logic
    default void softDelete(T entity) {
        entity.setDeleted(true); // Mark the entity as deleted
        save(entity); // Persist the changes
    }

    @Override
    Optional<T> findById(ID id);
}