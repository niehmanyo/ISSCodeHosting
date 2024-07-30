package com.iss.gateway.filters;

import com.iss.common.cache.TimedCache;
import com.iss.common.exception.UnauthorizedException;
import com.iss.gateway.config.AuthProperties;
import com.iss.common.utils.JwtTool;
import com.iss.gateway.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final JwtProperties jwtProperties;
    private final TimedCache<String, TokenPair> tokenCache = new TimedCache<>(60); // 60 seconds

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 1.获取request
//        ServerHttpRequest request = exchange.getRequest();
//        // 2.判断是否需要做登录拦截
//        if(isExclude(request.getPath().toString())){
//            // 放行
//            return chain.filter(exchange);
//        }
//        // 3.获取token
//        String token = null;
//        List<String> headers = request.getHeaders().get("authorization");
//        if (headers != null && !headers.isEmpty()) {
//            token = headers.get(0);
//        }
//        // 4.校验并解析token
//        Long userId = null;
//        try {
//            userId = jwtTool.parseToken(token);
//        } catch (UnauthorizedException e)
//        {
//            // 拦截，设置响应状态码为401
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//        // 5.传递用户信息
//        String userInfo = userId.toString();
//        ServerWebExchange swe = exchange.mutate()
//                .request(builder -> builder.header("user-info", userInfo))
//                .build();
//        // 6.放行
//        return chain.filter(swe);
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取request
        ServerHttpRequest request = exchange.getRequest();
        // 2.判断是否需要做登录拦截
        if (isExclude(request.getPath().toString())) {
            // 放行
            return chain.filter(exchange);
        }
        // 3.获取token
        String accessToken = getTokenFromHeaders(request, "Access-Token");
        String refreshToken = getTokenFromHeaders(request, "Refresh-Token");

        Long userId = null;
        try {
            userId = jwtTool.parseToken(accessToken);
        } catch (UnauthorizedException e) {
            // 如果访问令牌无效，尝试使用刷新令牌
            if (refreshToken != null) {
                TokenPair tokenPair = tokenCache.get(refreshToken);
                if (tokenPair != null) {
                    // 缓存中有新令牌，直接返回
                    ServerHttpResponse response = exchange.getResponse();
                    response.getHeaders().add("New-Access-Token", tokenPair.getAccessToken());
                    response.getHeaders().add("New-Refresh-Token", tokenPair.getRefreshToken());
                    return response.setComplete();
                } else {
                    try {
                        userId = jwtTool.parseToken(refreshToken);

                        // 生成新的访问令牌和刷新令牌
                        String newAccessToken = jwtTool.createToken(userId, jwtProperties.getAccessTokenTTL());
                        String newRefreshToken = jwtTool.createToken(userId, jwtProperties.getRefreshTokenTTL());

                        // 缓存新令牌
                        tokenCache.put(refreshToken, new TokenPair(newAccessToken, newRefreshToken), 60);

                        // 更新响应头，将新令牌发送给前端
                        ServerHttpResponse response = exchange.getResponse();
                        response.getHeaders().add("New-Access-Token", newAccessToken);
                        response.getHeaders().add("New-Refresh-Token", newRefreshToken);

                        // 返回响应，前端收到新令牌后需要重新发起请求
                        return response.setComplete();
                    } catch (UnauthorizedException ex) {
                        // 如果刷新令牌也无效，返回401
                        return unauthorizedResponse(exchange);
                    }
                }
            } else {
                // 如果没有刷新令牌，返回401
                return unauthorizedResponse(exchange);
            }
        }

        // 5.传递用户信息
        String userInfo = userId.toString();
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();

        // 6.放行
        return chain.filter(swe);
    }

    private boolean isExclude(String path) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, path)) {
                return true;
            }
        }
        return false;
    }

    private String getTokenFromHeaders(ServerHttpRequest request, String headerName) {
        List<String> headers = request.getHeaders().get(headerName);
        if (headers != null && !headers.isEmpty()) {
            return headers.get(0).replace("Bearer ", "");
        }
        return null;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private static class TokenPair {
        private final String accessToken;
        private final String refreshToken;

        public TokenPair(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}
