package com.company.aab.view.zayavka;

import com.company.aab.entity.Avtomobil;
import com.company.aab.entity.Foto;
import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.action.list.EditAction;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.editor.DataGridEditor;
import io.jmix.flowui.component.upload.FileStorageUploadField;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;


@Route(value = "zayavkas/:id", layout = MainView.class)
@ViewController("Zayavka.detail")
@ViewDescriptor("zayavka-detail-view.xml")
@EditedEntityContainer("zayavkaDc")
public class ZayavkaDetailView extends StandardDetailView<Zayavka> {

    @ViewComponent
    private DataContext dataContext;
    @Autowired
    private UiComponents uiComponents;
    @ViewComponent
    private CollectionPropertyContainer<Avtomobil> avtomobiliDc;
    @ViewComponent
    private DataGrid<Avtomobil> avtomobiliDataGrid;
    @ViewComponent
    private InstanceContainer<Zayavka> zayavkaDc;


    @Subscribe(id = "button", subject = "clickListener")
    public void onButtonClick(final ClickEvent<JmixButton> event) {

        Zayavka z = getEditedEntity();

        Avtomobil a = dataContext.create(Avtomobil.class);
        a.setZayavka(z);
        a.setMarka("Не определена");
        a.setNomer("Без номера");
        avtomobiliDc.getMutableItems().add(a);

    }

    @Subscribe(id = "saveAndCloseBtn", subject = "clickListener")
    public void onSaveAndCloseBtnClick(final ClickEvent<JmixButton> event) {
        if(zayavkaDc.getItem().getStatus()==null)
            zayavkaDc.getItem().setStatus(Zayavka.NOVAYA);
    }

}