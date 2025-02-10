package com.company.aab.app;

import com.vaadin.flow.component.*;

import java.util.Optional;

@Tag("input")
public class TextField extends Component {

    public TextField(String value) {
        getElement().setProperty("value",value);
    }
    @Synchronize("change")
    public String getValue() {
        return getElement().getProperty("value");
    }
    public void setValue(String value) {
        getElement().setProperty("value", value);
    }
    private static PropertyDescriptor<String,
                Optional<String>> PLACEHOLDER = PropertyDescriptors
            .optionalAttributeWithDefault("placeholder", "sdsd");

    public Optional<String> getPlaceholder() {
        return get(PLACEHOLDER);
    }
    public void setPlaceholder(String placeholder) {
        set(PLACEHOLDER, placeholder);
    }
}