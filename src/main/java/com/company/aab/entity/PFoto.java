package com.company.aab.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.FileRef;
import io.jmix.core.annotation.TenantId;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "PFOTO", indexes = {
        @Index(name = "IDX_PFOTO_PEREMESHENIE", columnList = "PEREMESHENIE_ID")
})
@Entity
public class PFoto {
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

    @Column(name = "FILE_", length = 1024)
    private FileRef file;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "PEREMESHENIE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Peremeshenie peremeshenie;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Peremeshenie getPeremeshenie() {
        return peremeshenie;
    }

    public void setPeremeshenie(Peremeshenie p) {
        this.peremeshenie = p;
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