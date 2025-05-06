package com.company.aab.view.avtomobil;

import com.company.aab.entity.WithFile;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileStorage;
import io.jmix.flowui.download.Downloader;

import java.util.List;

public class ImageList extends Composite<HorizontalLayout> {


    public ImageList(FileStorage fs, Downloader downloader, List<? extends WithFile> list) {

        Div hl = new Div();


        for (WithFile f : list) {
            if (f.getFile().getFileName().endsWith("jpg")
                    || f.getFile().getFileName().endsWith("png")) {
                Anchor a = new Anchor();

                Image image = new Image();
                image.setAlt("Скачать");
                StreamResource streamResource = new StreamResource(
                        f.getFile().getFileName(),
                        () -> fs.openStream(f.getFile()));

                image.setSrc(streamResource);
                image.setHeight("400px");
                a.setHref(streamResource);
                a.add(image);

                hl.add(a);
            } else {
                Button b = new Button();
                b.setText("Скачать");
                b.addClickListener(buttonClickEvent -> downloader.download(f.getFile()));
                hl.add(b);


            }

        }


        Scroller s = new Scroller(hl);
        s.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        s.setWidth("100%");

        getContent().add(s);
    }

    @Override
    protected HorizontalLayout initContent() {
        HorizontalLayout content = super.initContent();
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        return content;
    }


}
