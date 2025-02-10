package com.company.aab.view.avtofoto;

import com.company.aab.entity.AvtoFoto;
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

@Route(value = "avtofotoes/:id", layout = MainView.class)
@ViewController("AvtoFoto.detail")
@ViewDescriptor("avtofoto-detail-view.xml")
@EditedEntityContainer("avtofotoDc")
public class AvtoFotoDetailView extends StandardDetailView<AvtoFoto> {
    @ViewComponent
    private InstanceContainer<AvtoFoto> avtofotoDc;
    @Autowired
    private Downloader downloader;

    @Subscribe(id = "Download", subject = "singleClickListener")
    public void onDownloadClick(final ClickEvent<JmixButton> event) {
        FileRef fileRef =  avtofotoDc.getItem().getFile();
        downloader.download(fileRef);

    }
}