package com.taskmanagement.task_management_system.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        OAuth2User oauthUser = super.loadUser(request);
        System.out.println(oauthUser);

        String registrationId = request.getClientRegistration()
                .getRegistrationId();

        String nameAttributeKey;

        if ("github".equalsIgnoreCase(registrationId)) {
            nameAttributeKey = "id";
        } else {
            nameAttributeKey = "email";
        }

//        OAuth2UserInfo userInfo =
//                OAuth2UserInfoFactory.getOAuth2UserInfo(
//                        registrationId,
//                        oauthUser.getAttributes()
//                );

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                oauthUser.getAttributes(),
                nameAttributeKey
        );
    }
}