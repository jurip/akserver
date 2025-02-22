package com.company.aab.view.avtomobil;

import com.company.aab.entity.AvtoFoto;
import com.company.aab.entity.Avtomobil;
import com.company.aab.entity.Foto;
import com.company.aab.entity.OborudovanieFoto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "avtomobils/:id", layout = MainView.class)
@ViewController("Avtomobil.detail")
@ViewDescriptor("avtomobil-detail-view.xml")
@EditedEntityContainer("avtomobilDc")
public class AvtomobilDetailView extends StandardDetailView<Avtomobil> {
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private FileStorage fileStorage;
    @ViewComponent
    private InstanceContainer<Avtomobil> avtomobilDc;
    @Override
    public boolean hasUnsavedChanges() {
        return isReadOnly() ? false : super.hasUnsavedChanges();
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreSave(final DataContext.PreSaveEvent event) {
        if (isReadOnly())
            event.preventSave();
    }
    @Subscribe("button")
    protected void onButtonClick(final ClickEvent<JmixButton> event) {
        UI.getCurrent().getPage().open("https://maps.yandex.ru/?text=" +
                avtomobilDc.getItem().getLat()+
                "+" +
                avtomobilDc.getItem().getLng() +
                "", "_blank");
    }

    @Supply(to = "fotosDataGrid.file", subject = "renderer")
    private Renderer<Foto> fotosDataGridFileRenderer() {
        return new ComponentRenderer<>(userStep -> {
            Image image = uiComponents.create(Image.class);

            StreamResource streamResource = new StreamResource(
                    userStep.getFile() .getFileName(),
                    () -> fileStorage.openStream(userStep.getFile()));
            image.setSrc(streamResource);
            image.setHeight(150, Unit.PIXELS);

            return image;
        });
    }

    @Supply(to = "avtoFotosDataGrid.file", subject = "renderer")
    private Renderer<AvtoFoto> avtoFotosDataGridFileRenderer() {
        return new ComponentRenderer<>(userStep -> {
            Image image = uiComponents.create(Image.class);

            StreamResource streamResource = new StreamResource(
                    userStep.getFile() .getFileName(),
                    () -> fileStorage.openStream(userStep.getFile()));
            image.setSrc(streamResource);
            image.setHeight(150, Unit.PIXELS);

            return image;
        });
    }

    @Supply(to = "oborudovanieFotosDataGrid.file", subject = "renderer")
    private Renderer<OborudovanieFoto> oborudovanieFotosDataGridFileRenderer() {
        return new ComponentRenderer<>(userStep -> {
            Image image = uiComponents.create(Image.class);

            StreamResource streamResource = new StreamResource(
                    userStep.getFile() .getFileName(),
                    () -> fileStorage.openStream(userStep.getFile()));
            image.setSrc(streamResource);
            image.setHeight(150, Unit.PIXELS);

            return image;
        });
    }
}