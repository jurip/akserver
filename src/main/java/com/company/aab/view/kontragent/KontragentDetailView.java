package com.company.aab.view.kontragent;

import com.company.aab.entity.Kontragent;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "kontragents/:id", layout = MainView.class)
@ViewController("Kontragent.detail")
@ViewDescriptor("kontragent-detail-view.xml")
@EditedEntityContainer("kontragentDc")
public class KontragentDetailView extends StandardDetailView<Kontragent> {
}