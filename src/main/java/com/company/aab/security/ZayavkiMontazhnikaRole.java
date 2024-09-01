package com.company.aab.security;

import com.company.aab.entity.Zayavka;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;

@RowLevelRole(name = "zayavkiMontazhnika", code = ZayavkiMontazhnikaRole.CODE)
public interface ZayavkiMontazhnikaRole {
    String CODE = "zayavki-montazhnika";

    @JpqlRowLevelPolicy(
            entityClass = Zayavka.class,
            where = "{E}.username = :current_user_username")
    void zayavka();

}