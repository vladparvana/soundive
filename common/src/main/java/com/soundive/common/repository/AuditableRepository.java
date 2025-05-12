package com.soundive.common.repository;

import com.soundive.common.entity.AuditableEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repository interface for Auditable entities.
 */
public interface AuditableRepository<T extends AuditableEntity, ID> extends BaseRepository<T, ID> {

    // Methods excluding soft-deleted entities
    @Query("SELECT e FROM #{#entityName} e WHERE e.createdAt > :date AND e.deleted = false")
    List<T> findByCreatedAtAfter(Instant date);

    @Query("SELECT e FROM #{#entityName} e WHERE e.updatedAt BETWEEN :start AND :end AND e.deleted = false")
    List<T> findByUpdatedAtBetween(Instant start, Instant end);
}