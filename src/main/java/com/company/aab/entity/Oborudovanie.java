package com.company.aab.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "OBORUDOVANIE", indexes = {
        @Index(name = "IDX_OBORUDOVANIE_AVTOMOBIL", columnList = "AVTOMOBIL_ID")
})
@Entity
public class Oborudovanie {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "BARCODE")
    private String code;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "AVTOMOBIL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Avtomobil avtomobil;

    public Avtomobil getAvtomobil() {
        return avtomobil;
    }

    public void setAvtomobil(Avtomobil avtomobil) {
        this.avtomobil = avtomobil;
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