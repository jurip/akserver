package com.company.aab.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "DUTY")
@Entity
public class Duty {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "PRIORITET", length = 1)
    private String prioritet;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_FROM")
    private Date date_from;

    @Column(name = "DATE_UNTIL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_until;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "FIO")
    private String fio;

    @Column(name = "USERNAME")
    private String username;

    public String getPrioritet() {
        return prioritet;
    }

    public void setPrioritet(String prioritet) {
        this.prioritet = prioritet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate_until() {
        return date_until;
    }

    public void setDate_until(Date date_until) {
        this.date_until = date_until;
    }

    public void setDate_from(Date date_from) {
        this.date_from = date_from;
    }

    public Date getDate_from() {
        return date_from;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}