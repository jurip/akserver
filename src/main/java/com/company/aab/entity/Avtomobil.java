package com.company.aab.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "AVTOMOBIL", indexes = {
        @Index(name = "IDX_AVTOMOBIL_ZAYAVKA", columnList = "ZAYAVKA_ID")
})
@Entity
public class Avtomobil {
    public static final String VYPOLNENA = "VYPOLNENA";

    public static final String NOVAYA = "NOVAYA";

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "DATE_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Composition
    @OneToMany(mappedBy = "avtomobil")
    private List<Oborudovanie> barcode;

    @Column(name = "STATUS")
    private String status = Avtomobil.NOVAYA;

    @Composition
    @OneToMany(mappedBy = "avtomobil")
    private List<Foto> fotos;

    @Composition
    @OneToMany(mappedBy = "avtomobil")
    private List<OborudovanieFoto> oborudovanieFotos;

    @Composition
    @OneToMany(mappedBy = "avtomobil")
    private List<AvtoUsluga> performance_service;

    @Column(name = "NOMER")
    private String nomer;

    @Column(name = "MARKA")
    private String marka;
    @Column(name = "NOMER_AG")
    private String nomerAG;

    public String getNomerAG() {
        return nomerAG;
    }

    public void setNomerAG(String nomerAG) {
        this.nomerAG = nomerAG;
    }

    @Column(name = "USERNAME")
    private String username;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "ZAYAVKA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Zayavka zayavka;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Oborudovanie> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<Oborudovanie> barcode) {
        this.barcode = barcode;
    }

    public List<AvtoUsluga> getPerformance_service() {
        return performance_service;
    }

    public void setPerformance_service(List<AvtoUsluga> performance_service) {
        this.performance_service = performance_service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public Zayavka getZayavka() {
        return zayavka;
    }

    public void setZayavka(Zayavka zayavka) {
        this.zayavka = zayavka;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getNomer() {
        return nomer;
    }

    public void setNomer(String nomer) {
        this.nomer = nomer;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<OborudovanieFoto> getOborudovanieFotos() {
        return oborudovanieFotos;
    }

    public void setOborudovanieFotos(List<OborudovanieFoto> oborudovanieFotos) {
        this.oborudovanieFotos = oborudovanieFotos;
    }
}