package com.github.security.configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.github.security.authcatication.JwtAuthenticationFailureHandler;
import com.github.security.filters.LoginAuthenticationFilter;
import com.github.security.service.JwtUserDetailsService;


public class LoginConfigurer<T extends LoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
	
	private LoginAuthenticationFilter loginAuthenticationFilter;
	private JwtUserDetailsService jwtUserDetailsService;
	
	public LoginConfigurer(JwtUserDetailsService jwtUserDetailsService) {
		loginAuthenticationFilter = new LoginAuthenticationFilter();
		this.jwtUserDetailsService = jwtUserDetailsService;
	}
	
	@Override
	public void configure(B builder) throws Exception {
		loginAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
		loginAuthenticationFilter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
		loginAuthenticationFilter.setJwtUserDetailsService(jwtUserDetailsService);
		
		loginAuthenticationFilter = postProcess(loginAuthenticationFilter);
		builder.addFilterAfter(loginAuthenticationFilter, LogoutFilter.class);
	}
	
	public LoginConfigurer<T, B> authenticationSuccessHandler(AuthenticationSuccessHandler successHandler){
		loginAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
		return this;
	}

}
