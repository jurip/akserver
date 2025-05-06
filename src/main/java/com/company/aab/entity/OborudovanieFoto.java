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
@Table(name = "OBORUDOVANIE_FOTO",  indexes = {
        @Index(name = "IDX_OBORUDOVANIE_FOTO_AVTOMOBIL", columnList = "AVTOMOBIL_ID")
})
@Entity
public class OborudovanieFoto implements WithFile {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    @Column(name = "FILE_", length = 1024)
    private FileRef file;

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
    @JoinColumn(name = "AVTOMOBIL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Avtomobil avtomobil;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    public Avtomobil getAvtomobil() {
        return avtomobil;
    }

    public void setAvtomobil(Avtomobil avtomobil) {
        this.avtomobil = avtomobil;
    }

    public FileRef getFile() {
        return file;
    }

    public void setFile(FileRef file) {
        this.file = file;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }


}