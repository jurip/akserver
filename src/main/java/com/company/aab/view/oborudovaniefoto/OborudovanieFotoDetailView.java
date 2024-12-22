package com.company.aab.view.oborudovaniefoto;

import com.company.aab.entity.OborudovanieFoto;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "oborudovanieFotoes/:id", layout = MainView.class)
@ViewController("OborudovanieFoto.detail")
@ViewDescriptor("oborudovanie-foto-detail-view.xml")
@EditedEntityContainer("oborudovanieFotoDc")
public class OborudovanieFotoDetailView extends StandardDetailView<OborudovanieFoto> {
}