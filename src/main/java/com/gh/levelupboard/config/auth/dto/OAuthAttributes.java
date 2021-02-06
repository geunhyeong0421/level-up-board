package com.gh.levelupboard.config.auth.dto;

import com.gh.levelupboard.domain.user.LoginType;
import com.gh.levelupboard.domain.user.Role;
import com.gh.levelupboard.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String registrationId;
    private String name;
    private String email;
    private String picture;


    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String registrationId, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.registrationId = registrationId;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
            Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuthAttributes.builder()
                .registrationId("kakao")
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .registrationId("naver")
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .registrationId("google")
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        LoginType loginType = LoginType.valueOf(registrationId.toUpperCase());
        String loginId = String.valueOf(attributes.get(nameAttributeKey));

        return User.builder()
                .loginType(loginType)
                .loginId(loginId)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }


}
