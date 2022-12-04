package com.yaps.petstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration commentée de la sécurité.
 * 
 * Voir https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter pour mise à jour 
 * de la configuration. La classe WebSecurityConfigurerAdapter est dépréciée.
 * 
 */
@Configuration
/* Pas d'annotation sur les contrôleurs pour l'instant !!
   NE PAS ACTIVER.
@EnableGlobalMethodSecurity( 
	prePostEnabled = false, 
	securedEnabled = true, 
	jsr250Enabled = false
) 
 */ 
public class WebSecurityConfig  {

	/**
	 * Cette méthode est surtout intéressante parce qu'elle permet de
	 * "court-circuiter" Spring Security.
	 * 
	 * <p>
	 * On l'utilise principalement pour désactiver les tests sur certaines URL
	 * <em>avant</em> qu'on n'en arrive à vérifier en profondeur les choses.
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
		// @formatter:off 
		return (WebSecurity web) -> 
			web.ignoring()
			   .antMatchers("/css/**");
		// @formatter:on
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// @formatter:off 
		http
		.authorizeRequests()			
			// Ajoutez vos règles ici...
			.anyRequest().permitAll()
		.and()
			.formLogin()
				.loginPage("/login")
		.and()
			.anonymous() //  un utilisateur non connecté a automatiquement le rôle "ROLE_ANONYMOUS"						
		;
		// @formatter:on
		return http.build();
	}
}
