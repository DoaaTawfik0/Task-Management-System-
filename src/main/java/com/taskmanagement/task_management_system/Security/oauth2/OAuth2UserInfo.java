package com.taskmanagement.task_management_system.Security.oauth2;

public interface OAuth2UserInfo {

    String getId();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getImageUrl();
}