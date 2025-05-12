package com.soundive.common.entity;

import com.soundive.common.annotation.ExcludeFromDto;
import com.soundive.common.annotation.SoftDelete;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.type.*;

/**
 * Base class for persistent entities with soft delete filter.
 */
@Getter
@Setter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
public abstract class PersistableEntity extends BaseEntity {

    private final static int CURRENT_VERSION = 1;

    @ExcludeFromDto
    @Version
    @Builder.Default
    @Column(name = "VERSION", nullable = false)
    private int version=CURRENT_VERSION;


    @Builder.Default
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled = true;

    @ExcludeFromDto
    @SoftDelete
    @Builder.Default
    @Column(name = "DELETED", nullable = false)
    private boolean deleted = false;

    @ExcludeFromDto
    @Builder.Default
    @Column(name = "SYSTEM", nullable = false)
    private boolean system = false;

    public PersistableEntity(BaseIdStrategy idStrategy) {
        super(idStrategy);
    }

    protected PersistableEntity() {}
}