package com.company.aab.entity;

import io.jmix.core.FileRef;
import io.jmix.core.annotation.TenantId;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@JmixEntity
@Table(name = "TICKET")
@Entity
public class Ticket {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "KONTAKT_NOMER")
    private String kontakt_nomer;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "FILE_", length = 1024)
    private FileRef file;

    @Column(name = "KLIENT")
    private String klient;

    @Column(name = "KONTAKT")
    private String kontakt;

    @Column(name = "OPISANIE")
    private String opisanie;

    @Column(name = "TENANT_ATTRIBUTE")
    @TenantId
    private String tenantAttribute;

    public String getKontakt_nomer() {
        return kontakt_nomer;
    }

    public void setKontakt_nomer(String kontakt_nomer) {
        this.kontakt_nomer = kontakt_nomer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTenantAttribute() {
        return tenantAttribute;
    }

    public void setTenantAttribute(String tenantAttribute) {
        this.tenantAttribute = tenantAttribute;
    }


    public String getOpisanie() {
        return opisanie;
    }

    public void setOpisanie(String opisanie) {
        this.opisanie = opisanie;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public String getKlient() {
        return klient;
    }

    public void setKlient(String klient) {
        this.klient = klient;
    }

    public FileRef getFile() {
        return file;
    }

    public void setFile(FileRef file) {
        this.file = file;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}