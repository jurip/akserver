package com.company.aab.view.invite;

import com.company.aab.entity.Invite;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.action.SecuredBaseAction;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.Action;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;

@Route(value = "invites", layout = MainView.class)
@ViewController(id = "Invite.list")
@ViewDescriptor(path = "invite-list-view.xml")
@LookupComponent("invitesDataGrid")
@DialogMode(width = "64em")
public class InviteListView extends StandardListView<Invite> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<Invite> invitesDc;

    @ViewComponent
    private InstanceContainer<Invite> inviteDc;

    @ViewComponent
    private InstanceLoader<Invite> inviteDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private DataGrid<Invite> invitesDataGrid;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Subscribe
    public void onInit(final InitEvent event) {
        invitesDataGrid.getActions().forEach(action -> {
            if (action instanceof SecuredBaseAction secured) {
                secured.addEnabledRule(() -> listLayout.isEnabled());
            }
        });
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe("invitesDataGrid.create")
    public void onInvitesDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        Invite entity = dataContext.create(Invite.class);
        inviteDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("invitesDataGrid.edit")
    public void onInvitesDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        Invite item = inviteDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        invitesDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        inviteDc.setItem(null);
        inviteDl.load();
        updateControls(false);
    }

    @Subscribe(id = "invitesDc", target = Target.DATA_CONTAINER)
    public void onInvitesDcItemChange(final InstanceContainer.ItemChangeEvent<Invite> event) {
        Invite entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            inviteDl.setEntityId(entity.getId());
            inviteDl.load();
        } else {
            inviteDl.setEntityId(null);
            inviteDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(Invite entity) {
        ViewValidation viewValidation = getViewValidation();
        ValidationErrors validationErrors = viewValidation.validateUiComponents(form);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        validationErrors.addAll(viewValidation.validateBeanGroup(UiCrossFieldChecks.class, entity));
        return validationErrors;
    }

    private void updateControls(boolean editing) {
        UiComponentUtils.getComponents(form).forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
        invitesDataGrid.getActions().forEach(Action::refreshState);
    }

    private ViewValidation getViewValidation() {
        return getApplicationContext().getBean(ViewValidation.class);
    }
}