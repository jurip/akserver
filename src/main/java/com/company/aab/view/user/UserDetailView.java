package com.company.aab.view.user;

import com.company.aab.entity.User;
import com.company.aab.view.main.MainView;
import com.google.common.base.Strings;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import io.jmix.core.EntityStates;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import io.jmix.multitenancy.core.TenantProvider;
import io.jmix.multitenancyflowui.MultitenancyUiSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

@Route(value = "users/:id", layout = MainView.class)
@ViewController("User.detail")
@ViewDescriptor("user-detail-view.xml")
@EditedEntityContainer("userDc")
public class UserDetailView extends StandardDetailView<User> {

    @ViewComponent
    private TypedTextField<String> usernameField;
    @ViewComponent
    private PasswordField passwordField;
    @ViewComponent
    private PasswordField confirmPasswordField;
    @ViewComponent
    private ComboBox<String> timeZoneField;

    @Autowired
    private EntityStates entityStates;
    @ViewComponent
    private MessageBundle messageBundle;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ViewComponent
    private JmixComboBox<String> tenantField;

    @Autowired
    private TenantProvider tenantProvider;

    @Autowired
    private MultitenancyUiSupport multitenancyUiSupport;


    @Subscribe
    public void onInit(final InitEvent event) {

        timeZoneField.setItems(List.of(TimeZone.getAvailableIDs()));
        tenantField.setItems(multitenancyUiSupport.getTenantOptions());
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<User> event) {
        tenantField.setReadOnly(false);
        usernameField.setReadOnly(false);
        passwordField.setVisible(true);
        confirmPasswordField.setVisible(true);
    }
    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        String currentTenantId = tenantProvider.getCurrentUserTenantId();
        if (!currentTenantId.equals(TenantProvider.NO_TENANT)
                && Strings.isNullOrEmpty(tenantField.getValue())) {
            tenantField.setReadOnly(true);
            tenantField.setValue(currentTenantId);
        }
    }
    @Subscribe("tenantField")
    public void onTenantFieldComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixComboBox<String>, String> event) {
        String u = usernameField.getValue();
        if(u!="")
        usernameField.setValue(
                multitenancyUiSupport.getUsernameByTenant(usernameField.getValue(), event.getValue())
        );
    }
    @Subscribe
    public void onReady(final ReadyEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            usernameField.focus();
        }
    }

    @Subscribe
    public void onValidation(final ValidationEvent event) {
        if (entityStates.isNew(getEditedEntity())
                && !Objects.equals(passwordField.getValue(), confirmPasswordField.getValue())) {
            event.getErrors().add(messageBundle.getMessage("passwordsDoNotMatch"));
        }
    }

    @Subscribe
    protected void onBeforeSave(final BeforeSaveEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            getEditedEntity().setPassword(passwordEncoder.encode(passwordField.getValue()));
        }
    }
}