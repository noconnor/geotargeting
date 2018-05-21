package com.github.noconnor.geotargeting.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonProperty(required = true)
    double latitude;
    @JsonProperty(required = true)
    double longitude;
    @JsonProperty(value = "user_id", required = true)
    long userId;
    @JsonProperty(required = true)
    String name;
}
