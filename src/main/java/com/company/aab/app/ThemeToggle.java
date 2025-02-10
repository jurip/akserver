package com.company.aab.app;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

@Tag("theme-toggle")
@JsModule("./src/component/theme-toggle/theme-toggle.js")
public class ThemeToggle extends ComboBox<String> {

    public ThemeToggle() {
        super("dd", "1","2");
    }



}