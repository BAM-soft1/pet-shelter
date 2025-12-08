package org.pet.backendpetshelter.Service;

import org.pet.backendpetshelter.DTO.DogFactResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Collections;

@Service
public class DogFactService {
    private final RestClient restClient;

    public DogFactService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://dogapi.dog/api/v2").build();
    }

    public List<String> getDogFacts(int limit) {
        try {
            DogFactResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/facts").queryParam("limit", limit).build())
                    .retrieve()
                    .body(DogFactResponse.class);
            
            if (response != null && response.getData() != null) {
                return response.getData().stream()
                        .map(data -> data.getAttributes().getBody())
                        .toList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
