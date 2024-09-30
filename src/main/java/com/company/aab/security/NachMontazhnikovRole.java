package com.company.aab.security;

import com.company.aab.entity.*;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "nachmontazhnikov", code = NachMontazhnikovRole.CODE, scope = "UI")
public interface NachMontazhnikovRole {
    String CODE = "nachmontazhnikov";

    @MenuPolicy(menuIds = {"Zayavka.list","Avtomobil.list"})
    @ViewPolicy(viewIds = {"Zayavka.list", "Avtomobil.detail", "Avtomobil.list", "Zayavka.detail", "UslugaVariant.detail", "UslugaVariant.list"})
    void screens();

    @EntityAttributePolicy(entityClass = Avtomobil.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Avtomobil.class, actions = EntityPolicyAction.ALL)
    void avtomobil();
    @EntityAttributePolicy(entityClass = Usluga.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Usluga.class, actions = EntityPolicyAction.READ)
    void usluga();
    @EntityAttributePolicy(entityClass = POborudovanie.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = POborudovanie.class, actions = EntityPolicyAction.ALL)
    void poborudovanie();
    @EntityAttributePolicy(entityClass = PFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = PFoto.class, actions = EntityPolicyAction.ALL)
    void pfoto();
    @EntityAttributePolicy(entityClass = Peremeshenie.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Peremeshenie.class, actions = EntityPolicyAction.ALL)
    void peremeshenie();
    @EntityAttributePolicy(entityClass = Oborudovanie.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Oborudovanie.class, actions = EntityPolicyAction.ALL)
    void oborudovanie();
    @EntityAttributePolicy(entityClass = Duty.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Duty.class, actions = EntityPolicyAction.ALL)
    void duty();
    @EntityAttributePolicy(entityClass = ChekFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = ChekFoto.class, actions = EntityPolicyAction.ALL)
    void chekFoto();
    @EntityAttributePolicy(entityClass = Chek.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Chek.class, actions = EntityPolicyAction.ALL)
    void chek();
    @EntityAttributePolicy(entityClass = AvtoUsluga.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = AvtoUsluga.class, actions = EntityPolicyAction.ALL)
    void avtoUsluga();

    @EntityAttributePolicy(entityClass = Foto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Foto.class, actions = EntityPolicyAction.ALL)
    void foto();

    @EntityAttributePolicy(entityClass = OborudovanieFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = OborudovanieFoto.class, actions = EntityPolicyAction.ALL)
    void oborudovanieFoto();

    @EntityAttributePolicy(entityClass = Zayavka.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Zayavka.class, actions = EntityPolicyAction.ALL)
    void zayavka();

    @EntityAttributePolicy(entityClass = User.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = User.class, actions = EntityPolicyAction.READ)
    void user();

}