package com.herozero.util;

import com.herozero.model.User;
import com.herozero.model.EmissionRecord;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class DbInit {

    @Inject
    private DbManager dbManager;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        EntityManager em = dbManager.getEntityManager();
        try {
            em.getTransaction().begin();
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            if (users.isEmpty()) {
                User scientist = new User("scientist", "password123", "SCIENTIST");
                User publisher = new User("publisher", "admin123", "PUBLISHER");
                
                em.persist(scientist);
                em.persist(publisher);
                
                em.persist(new EmissionRecord("Deutschland", "DE", 2020, 644310.0, "APPROVED", scientist));
                em.persist(new EmissionRecord("Deutschland", "DE", 2021, 675000.0, "APPROVED", scientist));
                em.persist(new EmissionRecord("Deutschland", "DE", 2022, 660000.0, "APPROVED", scientist));
                
                em.persist(new EmissionRecord("Vereinigte Staaten", "US", 2020, 4713000.0, "APPROVED", scientist));
                em.persist(new EmissionRecord("Vereinigte Staaten", "US", 2021, 4900000.0, "APPROVED", scientist));
                
                em.persist(new EmissionRecord("China", "CN", 2020, 11600000.0, "APPROVED", scientist));
                em.persist(new EmissionRecord("China", "CN", 2021, 12100000.0, "APPROVED", scientist));

                em.persist(new EmissionRecord("Italien", "IT", 2020, 303000.0, "APPROVED", scientist));
                em.persist(new EmissionRecord("Italien", "IT", 2021, 318000.0, "APPROVED", scientist));
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
