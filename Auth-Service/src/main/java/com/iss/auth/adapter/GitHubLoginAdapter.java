package com.iss.auth.adapter;

import com.iss.auth.config.GitHubProperties;
import com.iss.auth.domain.po.User;
import com.iss.auth.encoder.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubLoginAdapter implements Login3rdAdapter {

    private final GitHubProperties gitHubProperties;

    private final RestTemplate restTemplate;

    @Override
    public User login(String authCode) {
        String accessToken = exchangeAuthCodeForAccessToken(authCode);
        return fetchGitHubUser(accessToken);
    }

    private String exchangeAuthCodeForAccessToken(String authCode) {
        String url = String.format("%s?client_id=%s&client_secret=%s&code=%s",
                gitHubProperties.getTokenUri(), gitHubProperties.getClientId(),
                gitHubProperties.getClientSecret(), authCode);

        @SuppressWarnings("unchecked")
        Map<String, String> tokenResponse = restTemplate.postForObject(url, null, Map.class);
        return tokenResponse.get("access_token");
    }

    private User fetchGitHubUser(String accessToken) {
        String url = gitHubProperties.getUserInfoUri() + "?access_token=" + accessToken;

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        User user = new User();
        String username = (String) response.get("login");
        String temporaryPassword = PasswordGenerator.generateRandomPassword(12);
        user.setUsername(username); // 使用登录名作为用户名
        user.setPassword(temporaryPassword);
        return user;
    }

}
