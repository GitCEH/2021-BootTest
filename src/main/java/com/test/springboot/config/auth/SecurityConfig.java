package com.test.springboot.config.auth;

import com.test.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOauth2UserService customOAuth2Service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //1줄 (~disable()) : h2-console 화면을 사용하기 위해 해당 옵션들을 disable
        //authorizeRequests : URL별 권한 관리 설정 옵션 시작점 : 이것이 선언 되어야 antMatchers 옵션 사용가능
        //antMatchers : 권한 관리 대상 지정 옵션 , URL.HTTP 메소드별로 관리 가능
        //anyRequest : 설정된 값들 이외의 나머지 URL
        http.csrf().disable().headers().frameOptions().disable()
                .and().authorizeRequests().antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**").permitAll().antMatchers("/api/v1/**")
                .hasRole(Role.USER.name()).anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/")
                .and().oauth2Login().userInfoEndpoint().userService(customOAuth2Service);
    }

}
