package org.mottadishiva.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mottadishiva.model.AccessRequest;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {

    ObjectMapper objectMapper = new ObjectMapper();

    public AccessRequest getAuthentication() {
        JsonNode rootNode = readJsonFile("config/cred.json");
        return generateAccessRequest(rootNode);
    }

    private JsonNode readJsonFile(String s) {
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(new File("config/cred.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rootNode;
    }

    private AccessRequest generateAccessRequest(JsonNode rootNode) {
        String app_key = rootNode.get("app_key").asText();
        String api_secret = rootNode.get("api_secret").asText();
        String access_token = rootNode.get("access_token").asText();


        System.out.println("app_key: " + app_key);
        System.out.println("api_secret: " + api_secret);
        System.out.println("access_token: " + access_token);

        return AccessRequest.builder().app_key(app_key).api_secret(api_secret).access_token(access_token).build();
    }

}
