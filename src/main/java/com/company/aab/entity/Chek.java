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
@Table(name = "CHEK")
@Entity
public class Chek {
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
    @OneToMany(mappedBy = "chek")
    private List<ChekFoto> fotos;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "QR")
    private String qr;

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @Column(name = "DATE_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "COMMENT_")
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

    public List<ChekFoto> getFotos() {
        return fotos;
    }

    public void setFotos(List<ChekFoto> fotos) {
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
}