package com.example.gachonhub.config;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.security.OAuth2LogOutHandler;
import com.example.gachonhub.security.RestAuthenticationEntryPoint;
import com.example.gachonhub.security.TokenAuthenticationFilter;
import com.example.gachonhub.security.oauth.CustomOAuth2UserService;
import com.example.gachonhub.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.gachonhub.security.oauth.OAuth2AuthenticationFailureHandler;
import com.example.gachonhub.security.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final CustomOAuth2UserService customOAuth2UserService;
    //spring security의 DefaultOAuth2UserService를 상속, loadUser() 메서드를 implements
    //이 메서드는 공급자로부터 access token을 얻은 다음 호출
    //공급자로부터 사용자의 세부사항을 fetch한다. 이미 데이터베이스에 동일한 메일의 사용자가 존재한다면 그의 세부사항을 업데이트, 그렇지 않으면 새로운 유저 등록

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    //oauth2 : csrf 공격을 막기 위해 상태 파라미터 쓸 것을 권장
    //인증 시, 인증 요청에 파라미터를 실어 보낸다. oauth2 공급자는 OAuth2  콜백으로 바꾸어 리턴한다.
    //반환받은 state 파라미커 값을 최초에 받은 것과 비교?
    //not match : 요청 거부
    //따라서 인증 값을 리턴 받은 수에도 상태 파라미터를 가지고 있어야 한다. -> redirect_uri과 같은 것에 short-lived cookie로 저장

    private final OAuth2LogOutHandler oAuth2LogOutHandler;


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**/swagger-resources/**", "/**/swagger-resources",
                "/v2/api-docs", "/webjars/**", "/swagger-ui.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("security config -> configure2");

        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/login",
                        "/any-role-test",
                        "/login/oauth2/**",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",    //여기에 요청 api 포함해두면 프론트에 따로 뭔가를 넘길 필요없이 프론트가 요청하면 이동하도록 하기
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/question/**")
                .permitAll()
                .antMatchers("/auth/**", "/oauth2/**")
                .permitAll()
                .antMatchers("/required-authorization-test")
                .hasAnyRole(User.Role.USER.name(), User.Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/*") //인증 완료, 사용자 코드 포함 => access token에 대한 authorization code 교환 => customoauth2userservice 호출
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService) //인증된 사용자의 세부사항 작성
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler) //access token 생성 => 이후 uri 접근이 왔을 떄 refresh token을 주면 되는건가?
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(oAuth2LogOutHandler)
                .logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler());

        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
