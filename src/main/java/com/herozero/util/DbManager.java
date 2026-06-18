package com.herozero.util;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;

@ApplicationScoped
public class DbManager implements Serializable {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("heroZeroPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
