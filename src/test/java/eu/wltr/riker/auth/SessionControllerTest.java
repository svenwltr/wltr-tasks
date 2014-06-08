package eu.wltr.riker.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.datatype.joda.JodaModule;

import eu.wltr.riker.auth.SessionControllerTest.WebConfig;
import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;
import eu.wltr.riker.meta.token.Token;
import eu.wltr.riker.meta.token.jackson.TokenModule;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class SessionControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testGetSession() throws Exception {
		this.mockMvc
				.perform(
						get("/session/").accept(
								"application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("application/json;charset=UTF-8"))
				.andExpect(
						jsonPath("$.sessionToken").value("randomToken1"))
				.andExpect(jsonPath("$.sessionExpires").value(123456789000L))
				.andExpect(jsonPath("$.userToken").value("randomToken2"))
				.andExpect(jsonPath("$.userName").value("some username"));

	}

	@Configuration
	@EnableWebMvc
	static class WebConfig extends WebMvcConfigurerAdapter {

		@Bean
		public SessionController sessionController() {
			return new SessionController();
		}

		@Override
		public void addArgumentResolvers(
				List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(new HandlerMethodArgumentResolver() {
				@Override
				public boolean supportsParameter(MethodParameter parameter) {
					return AuthContext.class.equals(parameter
							.getParameterType());
				}

				@Override
				public Object resolveArgument(MethodParameter parameter,
						ModelAndViewContainer mavContainer,
						NativeWebRequest webRequest,
						WebDataBinderFactory binderFactory) throws Exception {
					Session session = new Session();
					session.setToken(new Token("randomToken1"));
					session.setExpires(new DateTime(123456789000L));
					
					User user = new User();
					user.setName("some username");
					user.setToken(new Token("randomToken2"));
					
					return new AuthContext(user, session);

				}

			});

		}

		@Override
		public void configureMessageConverters(
				List<HttpMessageConverter<?>> converters) {
			MappingJackson2HttpMessageConverter jackson;
			jackson = new MappingJackson2HttpMessageConverter();
			jackson.getObjectMapper().registerModule(new JodaModule());
			jackson.getObjectMapper().registerModule(new TokenModule());

			converters.add(jackson);

		}

	}

}
