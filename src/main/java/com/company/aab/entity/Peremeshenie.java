package com.company.aab.entity;

import io.jmix.core.annotation.TenantId;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "PEREMESHENIE")
@Entity
public class Peremeshenie {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "TENANT_ATTRIBUTE")
    @TenantId
    private String tenantAttribute;

    public String getTenantAttribute() {
        return tenantAttribute;
    }

    public void setTenantAttribute(String tenantAttribute) {
        this.tenantAttribute = tenantAttribute;
    }

    @Composition
    @OneToMany(mappedBy = "peremeshenie")
    private List<PFoto> fotos;

    @Composition
    @OneToMany(mappedBy = "peremeshenie")
    private List<POborudovanie> barcode;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "DATE_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "COMMENT")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<PFoto> getFotos() {
        return fotos;
    }

    public void setFotos(List<PFoto> fotos) {
        this.fotos = fotos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<POborudovanie> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<POborudovanie> barcode) {
        this.barcode = barcode;
    }
}