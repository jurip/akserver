package com.company.aab.view.chek;

import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@ViewController("MyOnboardingView")
@ViewDescriptor("my-onboarding-view.xml")
@Route(value = "my-onboarding")
@DialogMode(width = "AUTO", height = "AUTO")
public class MyOnboardingView extends StandardView {
    private  String qr;
    public void setHtml(String username) {

    }
}
