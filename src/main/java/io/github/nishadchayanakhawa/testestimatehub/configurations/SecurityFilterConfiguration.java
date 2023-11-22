package io.github.nishadchayanakhawa.testestimatehub.configurations;

//import section
//spring libraries
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import io.github.nishadchayanakhawa.testestimatehub.model.Role;

//spring-security libraries
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * <b>Class Name</b>: SecurityFilterConfiguration<br>
 * <b>Description</b>: Spring security filter configuration.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Service
public class SecurityFilterConfiguration {
	private static final String H2_CONSOLE_CONTEXT_MATCHER = "/h2-console/**";
	private static final String CONFIGURATION_API_CONTEXT_MATCHER = TestEstimateHubConstants.CONFIGURATION_API + "/**";

	@Bean
	@Order(2)
	@Profile("!dev")
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(request -> request
						.requestMatchers(
								AntPathRequestMatcher.antMatcher(HttpMethod.GET, CONFIGURATION_API_CONTEXT_MATCHER))
								.hasAnyRole(Role.TEST_MANAGER.toString(), Role.ADMIN.toString())
						.requestMatchers(
								AntPathRequestMatcher.antMatcher(HttpMethod.PUT, CONFIGURATION_API_CONTEXT_MATCHER))
								.hasRole(Role.ADMIN.toString())
						.requestMatchers(
								AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, CONFIGURATION_API_CONTEXT_MATCHER))
								.hasRole(Role.ADMIN.toString()).requestMatchers("/home")
						.hasAnyRole(Role.ADMIN.toString(), Role.TESTER.toString(), Role.TEST_LEAD.toString(),
								Role.TEST_MANAGER.toString())
						.requestMatchers("/configuration/**")
							.hasAnyRole(Role.ADMIN.toString(), Role.TEST_MANAGER.toString())
						.requestMatchers("/login")
							.permitAll()
						.requestMatchers("/publicContent/**")
							.permitAll()
						.anyRequest()
							.authenticated())
				.formLogin(login -> login.loginPage("/login").permitAll().defaultSuccessUrl("/home", true)
						.failureUrl("/login?error=true"))
				.logout(logout -> logout.logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true)
						.deleteCookies("JSESSIONID"))
				.csrf().and().build();
	}

	/**
	 * <b>Method Name</b>: h2ConsoleSecurityFilterChain<br>
	 * <b>Description</b>: Disables csrf and header frame validation and
	 * authentication for h2-console. Access to console will be authenticated by h2
	 * db setup.<br>
	 * 
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Order(1)
	SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
		return
		// for h2-console,
		http.securityMatcher(AntPathRequestMatcher.antMatcher(H2_CONSOLE_CONTEXT_MATCHER)).authorizeHttpRequests(auth ->
		// disable spring security
		auth.requestMatchers(AntPathRequestMatcher.antMatcher(H2_CONSOLE_CONTEXT_MATCHER)).permitAll())
				// disable CSRF
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher(H2_CONSOLE_CONTEXT_MATCHER)))
				// disable header frame requirement
				.headers(headers -> headers.frameOptions().disable()).build();
	}
}