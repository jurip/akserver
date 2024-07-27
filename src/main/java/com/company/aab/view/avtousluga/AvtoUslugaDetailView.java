package com.company.aab.view.avtousluga;

import com.company.aab.entity.AvtoUsluga;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "avtoUslugas/:id", layout = MainView.class)
@ViewController("AvtoUsluga.detail")
@ViewDescriptor("avto-usluga-detail-view.xml")
@EditedEntityContainer("avtoUslugaDc")
public class AvtoUslugaDetailView extends StandardDetailView<AvtoUsluga> {
}