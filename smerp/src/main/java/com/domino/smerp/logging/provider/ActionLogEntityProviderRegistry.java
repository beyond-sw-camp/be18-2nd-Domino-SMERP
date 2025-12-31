package com.domino.smerp.logging.provider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ActionLogEntityProviderRegistry {

    private final Map<String, ActionLogEntityProvider> providerMap;

    public ActionLogEntityProviderRegistry(
        List<ActionLogEntityProvider> providers
    ) {
        this.providerMap = providers.stream()
            .collect(Collectors.toMap(
                ActionLogEntityProvider::getEntity,
                Function.identity()
            ));
    }

    public ActionLogEntityProvider getProvider(String entity) {
        return providerMap.get(entity);
    }
}
