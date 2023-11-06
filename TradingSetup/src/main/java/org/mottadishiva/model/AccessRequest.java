package org.mottadishiva.model;

import lombok.*;

@Getter
@Builder
@Setter
@ToString
public class AccessRequest {
    private final String app_key;
    private final String api_secret;
    private final String access_token;
}
