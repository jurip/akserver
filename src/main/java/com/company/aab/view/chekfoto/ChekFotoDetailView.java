package com.company.aab.view.chekfoto;

import com.company.aab.entity.ChekFoto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "chekFotoes/:id", layout = MainView.class)
@ViewController("ChekFoto.detail")
@ViewDescriptor("chek-foto-detail-view.xml")
@EditedEntityContainer("chekFotoDc")
public class ChekFotoDetailView extends StandardDetailView<ChekFoto> {
}