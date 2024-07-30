package com.iss.auth.adapter;

import com.iss.auth.config.GitHubProperties;
import com.iss.auth.domain.po.User;
import com.iss.auth.encoder.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Component("github")
@RequiredArgsConstructor
public class GitHubLoginAdapter implements Login3rdAdapter {

    private final GitHubProperties gitHubProperties;
    private final RestTemplate restTemplate;

    @Override
    public User login(String authCode) {
        try {
            String accessToken = exchangeAuthCodeForAccessToken(authCode);
            return fetchGitHubUser(accessToken);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to authenticate with GitHub.", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred during GitHub login.", e);
        }
    }

    private String exchangeAuthCodeForAccessToken(String authCode) {
        String url = String.format("%s?client_id=%s&client_secret=%s&code=%s",
                gitHubProperties.getTokenUri(), gitHubProperties.getClientId(),
                gitHubProperties.getClientSecret(), authCode);

        try {
            @SuppressWarnings("unchecked")
            Map<String, String> tokenResponse = restTemplate.postForObject(url, null, Map.class);
            System.out.println(tokenResponse);
            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                throw new RuntimeException("Invalid response from GitHub token endpoint.");
            }

            String accessToken = tokenResponse.get("access_token");
            if (accessToken == null) {
                throw new RuntimeException("Failed to obtain access token from GitHub.");
            }

            return accessToken;
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to exchange auth code for access token with GitHub.", e);
        }
    }

    private User fetchGitHubUser(String accessToken) {
        String url = gitHubProperties.getUserInfoUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to fetch GitHub user information.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("login")) {
                throw new RuntimeException("Invalid response from GitHub user info endpoint.");
            }

            User user = new User();
            String username = (String) responseBody.get("login");
            String temporaryPassword = PasswordGenerator.generateRandomPassword(12);
            user.setUsername(username); // 使用登录名作为用户名
            user.setPassword(temporaryPassword);
            return user;
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch GitHub user information.", e);
        }
    }
}
