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

@ResourceRole(name = "Менеджер", code = NachMontazhnikovRole.CODE)
public interface NachMontazhnikovRole {
    String CODE = "nachmontazhnikov";

    @MenuPolicy(menuIds = {"Zayavka.list", "Avtomobil.list", "Kontragent.list", "Peremeshenie.list", "Usluga.list", "Chek.list"})
    @ViewPolicy(viewIds = {"Kontragent.list", "Usluga.list", "Zayavka.list", "Avtomobil.list", "User.list", "Ticket.list", "Chek.list", "Peremeshenie.list", "sec_ResourceRoleModel.list", "sec_RowLevelRoleModel.list", "mten_Tenant.list", "datatl_entityInspectorListView", "Chek.detail", "MainView", "User.detail", "BlankView", "LoginView", "Usluga.detail", "Zayavka.detail", "Avtomobil.detail", "AvtoUsluga.detail", "AvtoUsluga.list", "Oborudovanie.detail", "Oborudovanie.list", "entityInfoView", "datatl_entityInspectorDetailView", "inputDialog", "multiValueSelectDialog", "mten_Tenant.detail", "sec_ResourceRoleModel.detail", "sec_ResourceRoleModel.lookup", "sec_RowLevelRoleModel.detail", "sec_RowLevelRoleModel.lookup", "resetPasswordView", "changePasswordView", "sec_EntityAttributeResourcePolicyModel.detail", "sec_EntityResourcePolicyModel.detail", "sec_ViewResourcePolicyModel.detail", "sec_GraphQLResourcePolicyModel.detail", "sec_MenuResourcePolicyModel.detail", "sec_ViewResourcePolicyModel.create", "sec_MenuResourcePolicyModel.create", "sec_ResourcePolicyModel.detail", "sec_EntityAttributeResourcePolicyModel.create", "sec_SpecificResourcePolicyModel.detail", "sec_EntityResourcePolicyModel.create", "roleAssignmentView", "sec_RowLevelPolicyModel.detail", "sec_UserSubstitution.detail", "sec_UserSubstitution.view", "Ticket.detail", "Kontragent.detail", "Peremeshenie.detail", "Foto.detail", "OborudovanieFoto.detail", "ChekFoto.detail", "PFoto.detail"})
    void screens();

    @EntityAttributePolicy(entityClass = Avtomobil.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Avtomobil.class, actions = EntityPolicyAction.ALL)
    void avtomobil();
    @EntityAttributePolicy(entityClass = Usluga.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Usluga.class, actions = EntityPolicyAction.ALL)
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

    @EntityAttributePolicy(entityClass = User.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = User.class, actions = EntityPolicyAction.ALL)
    void user();

    @EntityAttributePolicy(entityClass = Soispolnitel.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Soispolnitel.class, actions = EntityPolicyAction.ALL)
    void soispolnitel();

    @EntityAttributePolicy(entityClass = Ticket.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Ticket.class, actions = EntityPolicyAction.ALL)
    void ticket();

    @EntityAttributePolicy(entityClass = AvtoFoto.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = AvtoFoto.class, actions = EntityPolicyAction.ALL)
    void avtoFoto();

    @EntityAttributePolicy(entityClass = Kontragent.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Kontragent.class, actions = EntityPolicyAction.ALL)
    void kontragent();

    @SpecificPolicy(resources = "ui.loginToUi")
    void specific();

    @EntityAttributePolicy(entityClass = Oborudovanie.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Oborudovanie.class, actions = EntityPolicyAction.ALL)
    void oborudovanie();
}