package com.herozero.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.io.Serializable;

@Entity
@Table(name = "emission_records")
public class EmissionRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "emission_year", nullable = false)
    private int year;

    @Column(name = "co2_value", nullable = false)
    private double co2Value;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "scientist_id")
    private User scientist;

    public EmissionRecord() {
    }

    public EmissionRecord(String countryName, String countryCode, int year, double co2Value, String status, User scientist) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.year = year;
        this.co2Value = co2Value;
        this.status = status;
        this.scientist = scientist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getScientist() {
        return scientist;
    }

    public void setScientist(User scientist) {
        this.scientist = scientist;
    }
}
