package com.company.aab.view.avtomobil;

import com.company.aab.entity.Avtomobil;
import com.company.aab.entity.Foto;
import com.company.aab.entity.OborudovanieFoto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
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

    @Supply(to = "fotosDataGrid.file", subject = "renderer")
    private Renderer<Foto> fotosDataGridFileRenderer() {
        return new ComponentRenderer<>(userStep -> {
            Image image = uiComponents.create(Image.class);

            StreamResource streamResource = new StreamResource(
                    userStep.getFile() .getFileName(),
                    () -> fileStorage.openStream(userStep.getFile()));
            image.setSrc(streamResource);
            image.setHeight(50, Unit.PIXELS);

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
            image.setHeight(50, Unit.PIXELS);

            return image;
        });
    }
}