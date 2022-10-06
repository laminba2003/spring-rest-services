package com.spring.training.config;

import com.spring.training.domain.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        User user = new User(jwt.getClaim(Claims.GIVEN_NAME),
                jwt.getClaim(Claims.FAMILY_NAME),
                jwt.getClaim(Claims.EMAIL));
        List<String> roles = Optional.ofNullable(jwt.getClaimAsStringList(Claims.ROLES)).orElse(new ArrayList<>());
        List<GrantedAuthority> authorities = roles.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

}