package com.company.aab.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.annotation.TenantId;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "POBORUDOVANIE", indexes = {
        @Index(name = "IDX_POBORUDOVANIE_AVTOMOBIL", columnList = "PEREMESHENIE_ID")
})
@Entity
public class POborudovanie {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "BARCODE")
    private String code;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "PEREMESHENIE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Peremeshenie peremeshenie;

    @Column(name = "TENANT_ATTRIBUTE")
    @TenantId
    private String tenantAttribute;

    public String getTenantAttribute() {
        return tenantAttribute;
    }

    public void setTenantAttribute(String tenantAttribute) {
        this.tenantAttribute = tenantAttribute;
    }


    public Peremeshenie getPeremeshenie() {
        return peremeshenie;
    }

    public void setPeremeshenie(Peremeshenie peremeshenie) {
        this.peremeshenie = peremeshenie;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}