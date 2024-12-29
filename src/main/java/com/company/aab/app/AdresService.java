package com.company.aab.app;

import com.kuliginstepan.dadata.client.DadataClient;
import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;
import com.kuliginstepan.dadata.client.domain.address.AddressRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AdresService {
    @Autowired
    private DadataClient client;

    public Flux<Suggestion<Address>> getSuggestionsForAddress(String query) {
        return client.suggestAddress(AddressRequestBuilder.create(query).build());
    }
}