package com.company.aab.view.avtomobil;

import com.company.aab.entity.*;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.component.upload.FileStorageUploadField;
import io.jmix.flowui.component.valuepicker.EntityPicker;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "avtomobils/:id", layout = MainView.class)
@ViewController("Avtomobil.detail")
@ViewDescriptor("avtomobil-detail-view.xml")
@EditedEntityContainer("avtomobilDc")
public class AvtomobilDetailView extends StandardDetailView<Avtomobil> {
    @ViewComponent
    private CollectionPropertyContainer<Foto> fotosDc;
    @ViewComponent
    private DataContext dataContext;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private FileStorage fileStorage;
    @ViewComponent
    private FormLayout newF;
    @ViewComponent
    private FileStorageUploadField fileU;
    @ViewComponent
    private InstanceContainer<Avtomobil> avtomobilDc;
    @ViewComponent
    private JmixButton vypolnena;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        vypolnena.setVisible(!avtomobilDc.getItem().getStatus().equals(Avtomobil.VYPOLNENA));
    }

    @Subscribe
    public void onInit(final InitEvent event) {

        fileU.addFileUploadSucceededListener(event1 -> {
                Foto f = dataContext.create(Foto.class);
                f.setAvtomobil(getEditedEntity());
                f.setFile(event1.getSource().getValue());
                fotosDc.getMutableItems().add(f);
            }
        );
    }

    @Supply(to = "fotosDataGrid.imageCol", subject = "renderer")
    private Renderer<Foto> fotosDataGridImageColRenderer() {
        return new ComponentRenderer<>(foto -> {
            FileRef fileRef = foto.getFile();
            if (fileRef != null) {
                Image image = uiComponents.create(Image.class);
                image.setWidth("30px");
                image.setHeight("30px");
                StreamResource streamResource = new StreamResource(
                        fileRef.getFileName(),
                        () -> fileStorage.openStream(fileRef));
                image.setSrc(streamResource);
                image.setClassName("user-picture");

                return image;
            } else {
                return null;
            }
        });
    }

    @Supply(to = "fotosDataGrid.deleteIm", subject = "renderer")
    private Renderer<Foto> fotosDataGridDeleteImRenderer() {
        return new ComponentRenderer<>(foto -> {
            Button d = uiComponents.create(Button.class);
            d.setText("Удалить");
            d.addClickListener(event -> {
                fotosDc.getMutableItems().remove(foto);
            }
            );
            return d;
        });
    }



    @Subscribe(id = "vypolnena", subject = "clickListener")
    public void onVypolnenaClick(final ClickEvent<JmixButton> event) {
        avtomobilDc.getItem().setStatus(Avtomobil.VYPOLNENA);
    }




}