package com.company.aab.view.pfoto;

import com.company.aab.entity.PFoto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "pFotoes/:id", layout = MainView.class)
@ViewController("PFoto.detail")
@ViewDescriptor("p-foto-detail-view.xml")
@EditedEntityContainer("pFotoDc")
public class PFotoDetailView extends StandardDetailView<PFoto> {
}