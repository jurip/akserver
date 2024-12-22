package com.company.aab.view.foto;

import com.company.aab.entity.Foto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "fotoes/:id", layout = MainView.class)
@ViewController("Foto.detail")
@ViewDescriptor("foto-detail-view.xml")
@EditedEntityContainer("fotoDc")
public class FotoDetailView extends StandardDetailView<Foto> {
}