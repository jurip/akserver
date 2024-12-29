package com.company.aab.view.foto;

import com.company.aab.entity.Foto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.FileRef;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "fotoes/:id", layout = MainView.class)
@ViewController("Foto.detail")
@ViewDescriptor("foto-detail-view.xml")
@EditedEntityContainer("fotoDc")
public class FotoDetailView extends StandardDetailView<Foto> {
    @ViewComponent
    private InstanceContainer<Foto> fotoDc;
    @Autowired
    private Downloader downloader;

    @Subscribe(id = "Download", subject = "singleClickListener")
    public void onDownloadClick(final ClickEvent<JmixButton> event) {
        FileRef fileRef =  fotoDc.getItem().getFile();
        downloader.download(fileRef);

    }
}