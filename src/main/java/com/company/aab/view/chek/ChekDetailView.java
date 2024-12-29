package com.company.aab.view.chek;

import com.company.aab.entity.Chek;
import com.company.aab.entity.ChekFoto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "cheks/:id", layout = MainView.class)
@ViewController("Chek.detail")
@ViewDescriptor("chek-detail-view.xml")
@EditedEntityContainer("chekDc")
public class ChekDetailView extends StandardDetailView<Chek> {
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private FileStorage fileStorage;

    @Supply(to = "fotosDataGrid.file", subject = "renderer")
    private Renderer<ChekFoto> fotosDataGridFileRenderer() {
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