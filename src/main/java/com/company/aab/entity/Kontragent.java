package com.company.aab.entity;

import io.jmix.core.annotation.TenantId;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@JmixEntity
@Table(name = "KONTRAGENT")
@Entity
public class Kontragent {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "NAZVANIE")
    private String nazvanie;

    @Column(name = "ADRES")
    private String adres;

    @Column(name = "CONTACT_NAME")
    private String contact_name;

    @Column(name = "CONTACT_NUMBER")
    private String contact_number;

    @Column(name = "MANAGER_NAME")
    private String manager_name;

    @Column(name = "MANAGER_NUMBER")
    private String manager_number;

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getManager_number() {
        return manager_number;
    }

    public void setManager_number(String manager_number) {
        this.manager_number = manager_number;
    }

    @Column(name = "TENANT_ATTRIBUTE")
    @TenantId
    private String tenantAttribute;

    public String getTenantAttribute() {
        return tenantAttribute;
    }

    public void setTenantAttribute(String tenantAttribute) {
        this.tenantAttribute = tenantAttribute;
    }

    public String getNazvanie() {
        return nazvanie;
    }

    public void setNazvanie(String nazvanie) {
        this.nazvanie = nazvanie;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}