package com.herozero.web;

import com.herozero.model.EmissionRecord;
import com.herozero.util.DbManager;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class CitizenBean implements Serializable {

    @Inject
    private DbManager dbManager;

    private String selectedCountry;

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public List<String> getCountries() {
        EntityManager em = dbManager.getEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT e.countryName FROM EmissionRecord e WHERE e.status = 'APPROVED'", String.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<EmissionRecord> getRecords() {
        if (selectedCountry == null || selectedCountry.isEmpty()) {
            return new ArrayList<>();
        }
        EntityManager em = dbManager.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM EmissionRecord e WHERE e.countryName = :country AND e.status = 'APPROVED' ORDER BY e.year DESC", EmissionRecord.class)
                    .setParameter("country", selectedCountry)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public EmissionRecord getLatestRecord() {
        List<EmissionRecord> list = getRecords();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
