package com.company.aab.view.kontragent;

import com.company.aab.app.AdresService;
import com.company.aab.entity.Kontragent;
import com.company.aab.view.main.MainView;
import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@Route(value = "kontragents/:id", layout = MainView.class)
@ViewController("Kontragent.detail")
@ViewDescriptor("kontragent-detail-view.xml")
@EditedEntityContainer("kontragentDc")
public class KontragentDetailView extends StandardDetailView<Kontragent> {
    @Autowired
    private AdresService adresService;
    @ViewComponent
    private ComboBox<String> adresField;

    @Install(to = "adresField", subject = "itemsFetchCallback")
    private Stream<String> programmaticComboBoxItemsFetchCallback(Query<Suggestion<Address>, String> query) {
        String enteredValue = query.getFilter()
                .orElse("");

        return adresService.getSuggestionsForAddress(enteredValue)
                .toStream().map(addressSuggestion -> addressSuggestion.getValue())

                .skip(query.getOffset())
                .limit(query.getLimit())
                ;
    }
}