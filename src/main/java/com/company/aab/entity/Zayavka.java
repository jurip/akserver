package com.company.aab.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "ZAYAVKA")
@Entity
public class Zayavka {
    public static final String VYPOLNENA = "VYPOLNENA";

    public static final String NOVAYA = "NOVAYA";

    public static final String UDALENA = "UDALENA";


    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "SERVICE")
    private String service;

    @Column(name = "COMMENT_ADDRESS")
    private String comment_address;

    @Column(name = "MESSAGE", length = 2550)
    private String message;

    @Column(name = "CLIENT")
    private String client;

    @Column(name = "CONTACT_NAME")
    private String contact_name;

    @Column(name = "CONTACT_NUMBER")
    private String contact_number;

    @Column(name = "MANAGER_NAME")
    private String manager_name;

    @Column(name = "MANAGER_NUMBER")
    private String manager_number;

    @Column(name = "END_DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end_date_time;

    @Column(name = "USERNAME")
    private String username;

    @Composition
    @OneToMany(mappedBy = "zayavka")
    private List<Avtomobil> avtomobili;

    @Column(name = "NOMER")
    private String nomer;

    @Column(name = "NACHALO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nachalo;


    @Column(name = "ADRES")
    private String adres;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    public Date getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(Date end_date_time) {
        this.end_date_time = end_date_time;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getManager_number() {
        return manager_number;
    }

    public void setManager_number(String manager_number) {
        this.manager_number = manager_number;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment_address() {
        return comment_address;
    }

    public void setComment_address(String comment_address) {
        this.comment_address = comment_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Avtomobil> getAvtomobili() {
        return avtomobili;
    }

    public void setAvtomobili(List<Avtomobil> avtomobili) {
        this.avtomobili = avtomobili;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Date getNachalo() {
        return nachalo;
    }

    public void setNachalo(Date nachalo) {
        this.nachalo = nachalo;
    }

    public String getNomer() {
        return nomer;
    }

    public void setNomer(String nomer) {
        this.nomer = nomer;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}