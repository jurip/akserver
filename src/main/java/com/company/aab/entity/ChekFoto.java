package com.company.aab.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "CHEK_FOTO", indexes = {
        @Index(name = "IDX_CHEK_FOTO_CHEK", columnList = "CHEK_ID")
})
@Entity
public class ChekFoto {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "FILE_", length = 1024)
    private FileRef file;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "CHEK_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chek chek;

    public Chek getChek() {
        return chek;
    }

    public void setChek(Chek chek) {
        this.chek = chek;
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