package com.soundive.common.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;


@Aspect
@Component
public class HibernateFilterAspect {

    private final ObjectProvider<EntityManager> entityManagerProvider;

    public HibernateFilterAspect(ObjectProvider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Before("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
    public void applySoftDeleteFilter() {
        EntityManager entityManager = entityManagerProvider.getIfAvailable();
        if (entityManager == null) {
            return;
        }

        try {
            Session session = entityManager.unwrap(Session.class);
            Filter filter = session.enableFilter("deletedFilter");
            filter.setParameter("isDeleted", false);
        } catch (Exception e) {
            // Logging opțional
            System.err.println("Could not apply soft-delete filter: " + e.getMessage());
        }
    }
}
