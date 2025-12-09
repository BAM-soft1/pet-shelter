package org.pet.backendpetshelter.DTO;

import lombok.Data;
import java.util.List;

@Data
public class DogFactResponse {
    private List<DogFactData> data;

    @Data
    public static class DogFactData {
        private String id;
        private String type;
        private DogFactAttributes attributes;
    }

    @Data
    public static class DogFactAttributes {
        private String body;
    }
}
