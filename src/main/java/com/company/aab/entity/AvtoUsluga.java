package com.company.aab.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.annotation.TenantId;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "AVTO_USLUGA", indexes = {
        @Index(name = "IDX_AVTO_USLUGA_USLUGA", columnList = "USLUGA_ID"),
        @Index(name = "IDX_AVTO_USLUGA_AVTOMOBIL", columnList = "AVTOMOBIL_ID")
})
@Entity
public class AvtoUsluga {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "USLUGA_ID")
    @Composition
    @OneToOne(fetch = FetchType.LAZY)
    private Usluga usluga;

    @Column(name = "TENANT_ATTRIBUTE")
    @TenantId
    private String tenantAttribute;

    public void setUsluga(Usluga usluga) {
        this.usluga = usluga;
    }

    public String getTenantAttribute() {
        return tenantAttribute;
    }

    public void setTenantAttribute(String tenantAttribute) {
        this.tenantAttribute = tenantAttribute;
    }

    @InstanceName
    @Column(name = "TITLE")
    private String title;

    @Column(name = "KOLICHESTVO")
    private Integer kolichestvo;


    @Column(name = "SVERH")
    private Integer sverh;



    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "AVTOMOBIL_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Avtomobil avtomobil;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAvtomobil(Avtomobil avtomobil) {
        this.avtomobil = avtomobil;
    }

    public Avtomobil getAvtomobil() {
        return avtomobil;
    }

    public Usluga getUsluga() {
        return usluga;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getKolichestvo() {
        return kolichestvo;
    }

    public void setKolichestvo(Integer kolichestvo) {
        this.kolichestvo = kolichestvo;
    }

    public Integer getSverh() {
        return sverh;
    }

    public void setSverh(Integer sverh) {
        this.sverh = sverh;
    }
}