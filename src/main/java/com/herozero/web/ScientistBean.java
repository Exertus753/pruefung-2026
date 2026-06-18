package com.herozero.web;

import com.herozero.model.EmissionRecord;
import com.herozero.model.User;
import com.herozero.util.DbManager;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ScientistBean implements Serializable {

    @Inject
    private DbManager dbManager;

    @Inject
    private LoginBean loginBean;

    private String countryName;
    private String countryCode;
    private int year;
    private double co2Value;

    private EmissionRecord selectedRecord;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getCo2Value() {
        return co2Value;
    }

    public void setCo2Value(double co2Value) {
        this.co2Value = co2Value;
    }

    public EmissionRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(EmissionRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public List<EmissionRecord> getAllRecords() {
        EntityManager em = dbManager.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM EmissionRecord e ORDER BY e.countryName ASC, e.year DESC", EmissionRecord.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<EmissionRecord> getPendingRecords() {
        EntityManager em = dbManager.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM EmissionRecord e WHERE e.status = 'PENDING' ORDER BY e.countryName ASC, e.year DESC", EmissionRecord.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public String save() {
        EntityManager em = dbManager.getEntityManager();
        try {
            em.getTransaction().begin();
            User scientist = loginBean.getLoggedInUser();
            
            if (selectedRecord == null) {
                EmissionRecord record = new EmissionRecord(countryName, countryCode, year, co2Value, "PENDING", scientist);
                em.persist(record);
            } else {
                EmissionRecord managedRecord = em.find(EmissionRecord.class, selectedRecord.getId());
                managedRecord.setCountryName(countryName);
                managedRecord.setCountryCode(countryCode);
                managedRecord.setYear(year);
                managedRecord.setCo2Value(co2Value);
                managedRecord.setStatus("PENDING");
                em.merge(managedRecord);
            }
            
            em.getTransaction().commit();
            resetForm();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public void edit(EmissionRecord record) {
        this.selectedRecord = record;
        this.countryName = record.getCountryName();
        this.countryCode = record.getCountryCode();
        this.year = record.getYear();
        this.co2Value = record.getCo2Value();
    }

    public void delete(EmissionRecord record) {
        EntityManager em = dbManager.getEntityManager();
        try {
            em.getTransaction().begin();
            EmissionRecord managedRecord = em.find(EmissionRecord.class, record.getId());
            if (managedRecord != null) {
                em.remove(managedRecord);
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

    public void approve(EmissionRecord record) {
        EntityManager em = dbManager.getEntityManager();
        try {
            em.getTransaction().begin();
            EmissionRecord managedRecord = em.find(EmissionRecord.class, record.getId());
            if (managedRecord != null) {
                managedRecord.setStatus("APPROVED");
                em.merge(managedRecord);
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

    public void reject(EmissionRecord record) {
        delete(record);
    }

    public void cancel() {
        resetForm();
    }

    private void resetForm() {
        this.selectedRecord = null;
        this.countryName = null;
        this.countryCode = null;
        this.year = 0;
        this.co2Value = 0.0;
    }
}
