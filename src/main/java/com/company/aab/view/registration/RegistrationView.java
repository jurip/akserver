package com.company.aab.view.registration;


import com.company.aab.app.RegistrationBean;
import com.company.aab.entity.*;
import com.company.aab.security.FullAccessRole;
import com.company.aab.security.NachMontazhnikovRole;
import com.company.aab.view.main.MainView;
import com.google.common.collect.ImmutableMap;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.impl.QueryParamValuesManager;
import io.jmix.core.security.AccessDeniedException;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.email.EmailInfo;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import io.jmix.flowui.Actions;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.textfield.JmixEmailField;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.facet.UrlQueryParametersFacet;
import io.jmix.flowui.facet.urlqueryparameters.AbstractUrlQueryParametersBinder;
import io.jmix.flowui.facet.urlqueryparameters.FilterUrlQueryParametersSupport;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import io.jmix.flowui.xml.facet.UrlQueryParametersFacetProvider;
import io.jmix.fullcalendarflowui.facet.urlqueryparameters.FullCalendarUrlQueryParametersBinderProvider;
import io.jmix.multitenancy.core.TenantProvider;
import io.jmix.multitenancy.entity.Tenant;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import io.jmix.securityflowui.authentication.AuthDetails;
import io.jmix.securityflowui.authentication.LoginViewSupport;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Validator;

import java.util.*;

import static org.reflections.Reflections.log;

@AnonymousAllowed
@Route(value = "registration")
@ViewController(id = "RegistrationView")
@ViewDescriptor(path = "registration-view.xml")
public class RegistrationView extends StandardView {
    @ViewComponent
    private JmixEmailField email;
    @ViewComponent
    private TypedTextField<Object> company;

    @Autowired
    private Emailer emailer;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private TenantProvider tenantProvider;
    @Autowired
    private Metadata metadata;
    @Autowired
    private RegistrationBean registrationBean;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Notifications notifications;
    @Autowired
    private LoginViewSupport loginViewSupport;
    @Autowired
    private UnconstrainedDataManager dataManager;
    @Autowired
    private Actions actions;
    @Autowired
    private ViewNavigators viewNavigators;
    @ViewComponent
    private TypedTextField<Object> invite;
    @ViewComponent
    private TypedTextField<Object> pass1;
    @Autowired
    private ViewValidation viewValidation;
    @ViewComponent
    private JmixFormLayout form;
    @ViewComponent
    private TypedTextField<Object> login;
    @ViewComponent
    private TypedTextField<Object> pass2;

    private class SampleUrlQueryParametersBinder extends AbstractUrlQueryParametersBinder {

        public SampleUrlQueryParametersBinder() {

        }

        @Override
        public void updateState(QueryParameters queryParameters) {


        }

        @Override
        public Component getComponent() {
            return null;
        }
    }


    @Subscribe(id = "create", subject = "clickListener")
    public void onCreateClick(final ClickEvent<JmixButton> event) {

        //systemAuthenticator.withSystem(() -> {

        ValidationErrors errors = viewValidation.validateUiComponents(form);
        if (!errors.isEmpty()) {
            viewValidation.showValidationErrors(errors);
            return;
        }
        UUID id = null;
        try {
           id  = UUID.fromString(invite.getValue());
        }catch (Exception e){
            notifications.create("Неправильный промокод").show();
            return;
        }

         List<Invite> inv = dataManager.load(Invite.class)
                 .query("select u from Invite u where u.id = :id")
                 .parameter("id", id ).list();


        if(inv.isEmpty()){
            notifications.create("Промокод не найден").show();
            return;
        }
        if(pass1.isEmpty()){
            notifications.create("Пароль не введен").show();
            return;
        }
        if(pass1.getValue()!=pass2.getValue()){
            notifications.create("Пароль не совпадает").show();
            return;
        }
        if(login.isEmpty()){
            notifications.create("Логин не введен").show();
            return;
        }


        company.setValue(company.getValue().trim().replaceAll("\\s+",""));

        if (!registrationBean.hasCompany(company.getValue())) {
                registrationBean.newCompany(company.getValue());

                User user = dataManager.create(User.class);
                user.setUsername(company.getValue()+"|"+login.getValue());
                //String pass = RandomStringUtils.random(6, true, true);
                user.setPassword(passwordEncoder.encode(pass1.getValue()));
                user.setTenant(company.getValue());
                user.setActive(true);
                List<Object> toSave = new ArrayList<>();
                toSave.add(user);
                toSave.add(createRoleAssignment(user, FullAccessRole.CODE, RoleAssignmentRoleType.RESOURCE));

                dataManager.saveAll(toSave);

            firstLogin(user, pass1.getValue(), inv);
           // viewNavigators.listView(this, User.class).navigate();
            //notifications.create("Сохраните пароль администратора admin: "+ pass).withDuration(6000).show();





            /*    dataManager.save(n);

                notifications.create("Создана компания");
                EmailInfo emailInfo = EmailInfoBuilder.create()
                        .setAddresses(email.getValue())
                        .setSubject("регистрация в системе Эвомонтаж")
                        .setFrom(null)
                        .setBody("  Создан аккаунт ")
                        .build();
                emailer.sendEmailAsync(emailInfo);*/

            }
        else {
            notifications.create("Конпания с таким названием уже существует").show();
        }
           // return "ok";
       // });
    }

    private void firstLogin(User user, String pass, List<Invite> inv) {
        try {
            loginViewSupport.authenticate(
                    AuthDetails.of(user.getUsername(), pass)

            );
        } catch (final BadCredentialsException | DisabledException | LockedException | AccessDeniedException e) {
            log.warn("Login failed for user '{}': {}", user.getUsername(), e.toString());

        }

        Usluga u = dataManager.create(Usluga.class);
        u.setCode("1");
        u.setTitle("монтаж");
        u.setTenantAttribute(company.getValue());
        dataManager.save(u);

        u = dataManager.create(Usluga.class);
        u.setCode("2");
        u.setTitle("демонтаж");
        u.setTenantAttribute(company.getValue());
        dataManager.save(u);

        u = dataManager.create(Usluga.class);
        u.setCode("3");
        u.setTitle("тарировка");
        u.setTenantAttribute(company.getValue());
        dataManager.save(u);

        u = dataManager.create(Usluga.class);
        u.setCode("4");
        u.setTitle("ДУТ");
        u.setTenantAttribute(company.getValue());
        dataManager.save(u);

        u = dataManager.create(Usluga.class);
        u.setCode("5");
        u.setTitle("контроллер");
        u.setTenantAttribute(company.getValue());
        dataManager.save(u);

        Kontragent k = dataManager.create(Kontragent.class);
        k.setAdres("Простоквашино д. 1");
        k.setContact_name("Вася");
        k.setContact_number("112");
        k.setManager_name("Коля");
        k.setManager_number("123");
        k.setNazvanie("Рога и Копыта");
        k.setTenantAttribute(company.getValue());
        dataManager.save(k);

        k = dataManager.create(Kontragent.class);
        k.setAdres("Простоквашино д. 2");
        k.setContact_name("Вася");
        k.setContact_number("113");
        k.setManager_name("Коля");
        k.setManager_number("124");
        k.setNazvanie("Ромашка");
        k.setTenantAttribute(company.getValue());
        dataManager.save(k);

        Zayavka z = dataManager.create(Zayavka.class);
        z.setNomer("Первая тестовая");
        z.setUser(user);
        z.setClient(k.getNazvanie());
        z.setUsername(user.getUsername());
        z.setNachalo(new Date());
        z.setStatus("NOVAYA");
        z.setTenantAttribute(company.getValue());
        dataManager.save(z);


        dataManager.remove(inv.get(0));
    }

    private RoleAssignmentEntity createRoleAssignment(User user, String roleCode, String roleType) {
        RoleAssignmentEntity roleAssignmentEntity = dataManager.create(RoleAssignmentEntity.class);
        roleAssignmentEntity.setRoleCode(roleCode);
        roleAssignmentEntity.setUsername(user.getUsername());
        roleAssignmentEntity.setRoleType(roleType);
        return roleAssignmentEntity;
    }
}