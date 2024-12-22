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
@Table(name = "SOISPOLNITEL", indexes = {
        @Index(name = "IDX_SOISPOLNITEL_USER", columnList = "USER_ID"),
        @Index(name = "IDX_SOISPOLNITEL_AVTOMOBIL", columnList = "AVTOMOBIL_ID")
})
@Entity
public class Soispolnitel {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "USER_ID")
    @Composition
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @InstanceName
    @Column(name = "USERNAME")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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





    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "AVTOMOBIL_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Avtomobil avtomobil;

    public User getUser() {
        return user;
    }

    public void setAvtomobil(Avtomobil avtomobil) {
        this.avtomobil = avtomobil;
    }

    public Avtomobil getAvtomobil() {
        return avtomobil;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


}