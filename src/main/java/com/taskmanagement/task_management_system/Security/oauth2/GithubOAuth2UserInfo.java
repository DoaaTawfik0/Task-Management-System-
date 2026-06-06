package com.taskmanagement.task_management_system.Security.oauth2;

import java.util.Arrays;
import java.util.Map;

public record GithubOAuth2UserInfo(Map<String, Object> attributes) implements OAuth2UserInfo {

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        // email not provided in GitHub
        return (String) attributes.get("email");
    }

    @Override
    public String getFirstName() {
        String name = (String) attributes.get("name");

        if (name == null || name.isBlank()) {
            return (String) attributes.get("login");
        }

        return name.split("\\s+")[0];
    }

    @Override
    public String getLastName() {

        String name = (String) attributes.get("name");

        if (name == null || name.isBlank()) {
            return "";
        }

        String[] parts = name.split("\\s+");

        if (parts.length <= 1) {
            return "";
        }

        return String.join(" ",
                Arrays.copyOfRange(parts, 1, parts.length));
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}