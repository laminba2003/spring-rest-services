package com.spring.training.config;

import com.spring.training.domain.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;
import java.util.stream.Collectors;

public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<GrantedAuthority> authorities = jwt.getClaimAsStringList("roles").stream().
                map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(createUser(jwt), null, authorities);
    }

    private User createUser(Jwt jwt) {
        return new User(jwt.getClaim("given_name"),
                jwt.getClaim("family_name"),
                jwt.getClaim("email"));
    }

}