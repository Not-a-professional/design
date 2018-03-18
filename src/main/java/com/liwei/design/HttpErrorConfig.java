package com.liwei.design;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class HttpErrorConfig {
    @Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {
//	            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
	            ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/403");
	            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
	            container.addErrorPages(error403Page,error404Page);
	        }
	    };
	}
}