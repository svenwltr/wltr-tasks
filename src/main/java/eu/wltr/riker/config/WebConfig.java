package eu.wltr.riker.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.datatype.joda.JodaModule;

import eu.wltr.riker.auth.AuthContextResolver;
import eu.wltr.riker.auth.AuthInterceptor;
import eu.wltr.riker.meta.token.jackson.TokenModule;


@Configuration
@EnableWebMvc
@ComponentScan("eu.wltr.riker")
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthInterceptor authInterceptor;

	@Autowired
	private AuthContextResolver authContextResolver;

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter jackson;
		jackson = new MappingJackson2HttpMessageConverter();
		jackson.getObjectMapper().registerModule(new JodaModule());
		jackson.getObjectMapper().registerModule(new TokenModule());

		converters.add(jackson);

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor);

	}

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(authContextResolver);
	}

}
