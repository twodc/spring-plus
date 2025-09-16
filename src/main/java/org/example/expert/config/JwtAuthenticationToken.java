package org.example.expert.config;

import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

// JWT 인증을 위한 커스텀 Authentication 객체
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    // 실제 로그인한 사용자 정보를 담는 객체
    private final AuthUser authUser;

    public JwtAuthenticationToken(AuthUser authUser) {
        super(authUser.getAuthorities()); // 부모 클래스에 사용자 권한 정보 전달
        this.authUser = authUser;
        setAuthenticated(true); // 인증 완료 처리
    }

    // JWT 방식에서는 비밀번호 같은 자격 증명이 불필요함으로 null 반환
    @Override
    public Object getCredentials() {
        return null;
    }

    // 인증된 사용자 정보 반환
    @Override
    public Object getPrincipal() {
        return authUser;
    }
}
