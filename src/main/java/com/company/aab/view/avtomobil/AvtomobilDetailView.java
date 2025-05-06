package com.company.aab.view.avtomobil;

import com.company.aab.app.FlutterServiceBean;
import com.company.aab.entity.*;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
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
import io.jmix.reports.util.ReportZipUtils;
import io.jmix.reports.util.impl.ReportZipUtilsImpl;
import io.jmix.reports.yarg.reporting.ReportOutputDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.company.aab.app.Zip.createZipArchive;

@Route(value = "avtomobils/:id", layout = MainView.class)
@ViewController("Avtomobil.detail")
@ViewDescriptor("avtomobil-detail-view.xml")
@EditedEntityContainer("avtomobilDc")
public class AvtomobilDetailView extends StandardDetailView<Avtomobil> {
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


    @Subscribe(id = "resendToBipium", subject = "clickListener")
    public void onResendToBipiumClick(final ClickEvent<JmixButton> event) {
        flutterServiceBean.resendAvto(avtomobilDc.getItem().getId().toString());
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

