package com.company.aab.app;

import io.jmix.core.security.AuthorizedUrlsProvider;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class GreetingAuthorizedUrlsProvider implements AuthorizedUrlsProvider {

    @Override
    public Collection<String> getAuthenticatedUrlPatterns() {
        return List.of("/dlyaChajnikov/**");
    }

    @Override
    public Collection<String> getAnonymousUrlPatterns() {
        return List.of();
    }
}