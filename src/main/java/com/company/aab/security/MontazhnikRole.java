package com.company.aab.security;

import com.company.aab.entity.*;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.security.role.annotation.SpecificPolicy;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "Монтажник", code = MontazhnikRole.CODE)
public interface MontazhnikRole {
    String CODE = "montazhnik-role";

    @MenuPolicy(menuIds = "Ticket.list")
    @ViewPolicy(viewIds = {"Ticket.list", "Avtomobil.detail", "AvtoUsluga.detail", "AvtoUsluga.list", "Chek.detail", "ChekFoto.detail", "Foto.detail", "LoginView", "MainView", "Oborudovanie.detail", "Oborudovanie.list", "OborudovanieFoto.detail", "Peremeshenie.detail", "PFoto.detail", "Ticket.detail", "Zayavka.detail"})
    void screens();

    @EntityAttributePolicy(entityClass = AvtoFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = AvtoFoto.class, actions = EntityPolicyAction.ALL)
    void avtoFoto();

    @EntityAttributePolicy(entityClass = AvtoUsluga.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = AvtoUsluga.class, actions = EntityPolicyAction.ALL)
    void avtoUsluga();

    @EntityAttributePolicy(entityClass = Avtomobil.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Avtomobil.class, actions = EntityPolicyAction.ALL)
    void avtomobil();

    @EntityAttributePolicy(entityClass = Chek.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Chek.class, actions = EntityPolicyAction.ALL)
    void chek();

    @EntityAttributePolicy(entityClass = ChekFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = ChekFoto.class, actions = EntityPolicyAction.ALL)
    void chekFoto();

    @EntityAttributePolicy(entityClass = Foto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Foto.class, actions = EntityPolicyAction.ALL)
    void foto();

    @EntityAttributePolicy(entityClass = Oborudovanie.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Oborudovanie.class, actions = EntityPolicyAction.ALL)
    void oborudovanie();

    @EntityAttributePolicy(entityClass = Kontragent.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Kontragent.class, actions = EntityPolicyAction.ALL)
    void kontragent();

    @EntityAttributePolicy(entityClass = Duty.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Duty.class, actions = EntityPolicyAction.ALL)
    void duty();

    @EntityAttributePolicy(entityClass = OborudovanieFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = OborudovanieFoto.class, actions = EntityPolicyAction.ALL)
    void oborudovanieFoto();

    @EntityAttributePolicy(entityClass = PFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = PFoto.class, actions = EntityPolicyAction.ALL)
    void pFoto();

    @EntityAttributePolicy(entityClass = POborudovanie.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = POborudovanie.class, actions = EntityPolicyAction.ALL)
    void pOborudovanie();

    @EntityAttributePolicy(entityClass = Peremeshenie.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Peremeshenie.class, actions = EntityPolicyAction.ALL)
    void peremeshenie();

    @EntityAttributePolicy(entityClass = Soispolnitel.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Soispolnitel.class, actions = EntityPolicyAction.ALL)
    void soispolnitel();

    @EntityAttributePolicy(entityClass = User.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = User.class, actions = EntityPolicyAction.ALL)
    void user();

    @EntityAttributePolicy(entityClass = Ticket.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Ticket.class, actions = EntityPolicyAction.ALL)
    void ticket();

    @EntityAttributePolicy(entityClass = Usluga.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Usluga.class, actions = EntityPolicyAction.ALL)
    void usluga();

    @EntityAttributePolicy(entityClass = Zayavka.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Zayavka.class, actions = EntityPolicyAction.ALL)
    void zayavka();

    @SpecificPolicy(resources = {"ui.loginToUi", "rest.fileDownload.enabled", "rest.fileUpload.enabled", "rest.enabled"})
    void specific();
}