package com.company.aab.view.peremeshenie;

import com.company.aab.entity.Peremeshenie;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "peremeshenies/:id", layout = MainView.class)
@ViewController("Peremeshenie.detail")
@ViewDescriptor("peremeshenie-detail-view.xml")
@EditedEntityContainer("peremeshenieDc")
public class PeremeshenieDetailView extends StandardDetailView<Peremeshenie> {
}