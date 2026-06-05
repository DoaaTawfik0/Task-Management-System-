package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Exception.Email.MissingEmailException;
import com.taskmanagement.task_management_system.Security.oauth2.OAuth2UserInfo;
import com.taskmanagement.task_management_system.Security.oauth2.OAuth2UserInfoFactory;
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

        String nameAttributeKey;

        OAuth2User oauthUser = super.loadUser(request);
//        System.out.println(oauthUser);

        String registrationId = request.getClientRegistration()
                .getRegistrationId();

        OAuth2UserInfo userInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(
                        registrationId,
                        oauthUser.getAttributes()
                );

        String email = userInfo.getEmail();

        if(email == null || email.isBlank()){
            throw new MissingEmailException(
                    userInfo.getId(),
                    registrationId,
                    userInfo.getFirstName()
            );
        }


        if ("github".equalsIgnoreCase(registrationId)) {
            nameAttributeKey = "id";
        } else {
            nameAttributeKey = "email";
        }

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                oauthUser.getAttributes(),
                nameAttributeKey
        );
    }
}