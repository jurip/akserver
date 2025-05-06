package com.company.aab.view.avtomobil;

import com.company.aab.app.FlutterServiceBean;
import com.company.aab.entity.Avtomobil;

import com.company.aab.view.main.MainView;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.upload.receiver.FileTemporaryStorageBuffer;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.company.aab.app.Zip.createZipArchive;

@Route(value = "otchets/:id", layout = MainView.class)
@ViewController(id = "Otchet.detail")
@ViewDescriptor(path = "otchet-detail-view.xml")
@EditedEntityContainer("avtomobilDc")
public class OtchetDetailView extends StandardDetailView<Avtomobil> {
    @Autowired
    private FileTemporaryStorageBuffer fileTemporaryStorageBuffer;
    @Autowired
    private FileStorageLocator fileStorageLocator;

    @Subscribe(id = "download", subject = "clickListener")
    public void onDownloadClick(final ClickEvent<JmixButton> event) {
        downloader.download(createZipArchive(avtomobilDc.getItem().getFotos(), fileStorageLocator),"fotos.zip");
    }

    @Subscribe(id = "downloadaFotos", subject = "clickListener")
    public void onDownloadaFotosClick(final ClickEvent<JmixButton> event) {
        downloader.download(createZipArchive(avtomobilDc.getItem().getAvtoFotos(), fileStorageLocator),"foto_objekta.zip");
    }

    @Subscribe(id = "downloadoFotos", subject = "clickListener")
    public void onDownloadoFotosClick(final ClickEvent<JmixButton> event) {
        downloader.download(createZipArchive(avtomobilDc.getItem().getOborudovanieFotos(), fileStorageLocator),"foto_oborudovanija.zip");
    }

    @ViewComponent
    private JmixFormLayout form;
    @ViewComponent
    private JmixButton downloadoFotos;
    @ViewComponent
    private JmixButton downloadaFotos;


    @Autowired
    private FlutterServiceBean flutterServiceBean;
    @ViewComponent
    private HorizontalLayout fotos;
    @ViewComponent
    private HorizontalLayout oFotos;
    @ViewComponent
    private HorizontalLayout аFotos;
    @Autowired
    private FileStorage fileStorage;
    @ViewComponent
    private JmixButton download;


    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Avtomobil dd = avtomobilDc.getItem();
        ImageList cc = new ImageList(fileStorage, downloader, dd.getFotos());
        fotos.add(cc);

        ImageList of = new ImageList(fileStorage, downloader, dd.getOborudovanieFotos());
        oFotos.add(of);

        ImageList af = new ImageList(fileStorage, downloader, dd.getAvtoFotos());
        oFotos.add(af);


    }


    @Autowired
    private UiComponents uiComponents;
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
    @Autowired
    private Downloader downloader;
}