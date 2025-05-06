package com.company.aab.view.peremeshenie;

import com.company.aab.entity.Chek;
import com.company.aab.entity.PFoto;
import com.company.aab.entity.Peremeshenie;
import com.company.aab.view.avtomobil.ImageList;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.company.aab.app.Zip.createZipArchive;

@Route(value = "peremeshenies/:id", layout = MainView.class)
@ViewController("Peremeshenie.detail")
@ViewDescriptor("peremeshenie-detail-view.xml")
@EditedEntityContainer("peremeshenieDc")
public class PeremeshenieDetailView extends StandardDetailView<Peremeshenie> {
    @Autowired
    private FileStorageLocator fileStorageLocator;

    @Subscribe(id = "downloadFotos", subject = "clickListener")
    public void onDownloadFotosClick(final ClickEvent<JmixButton> event) {
        downloader.download(createZipArchive(peremeshenieDc.getItem().getFotos(), fileStorageLocator),"foto_peremeshenija.zip");

    }
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private Downloader downloader;
    @ViewComponent
    private HorizontalLayout fotos;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Peremeshenie dd = peremeshenieDc.getItem();
        ImageList cc = new ImageList(fileStorage, downloader, dd.getFotos());
        fotos.add(cc);
    }

    @ViewComponent
    private InstanceContainer<Peremeshenie> peremeshenieDc;
}