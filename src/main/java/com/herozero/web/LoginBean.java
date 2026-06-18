package com.herozero.web;

import com.herozero.model.User;
import com.herozero.util.DbManager;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private DbManager dbManager;

    private String username;
    private String password;
    private User loggedInUser;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public String login() {
        EntityManager em = dbManager.getEntityManager();
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            
            this.loggedInUser = user;
            return "backend?faces-redirect=true";
        } catch (NoResultException e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ungültige Anmeldedaten", "Ungültiger Benutzername oder Passwort."));
            return null;
        } finally {
            em.close();
        }
    }

    public String logout() {
        this.loggedInUser = null;
        this.username = null;
        this.password = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public boolean isScientist() {
        return loggedInUser != null && "SCIENTIST".equals(loggedInUser.getRole());
    }

    public boolean isPublisher() {
        return loggedInUser != null && "PUBLISHER".equals(loggedInUser.getRole());
    }
}
