package com.yaps.petstore.config;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Évite d'écrire un contrôleur pour la page d'accueil.
 * (mais rien n'interdit d'en utiliser un quand même, hein).
 * 
 * Cette classe est actuellement désactivée (pour la réactiver, décommenter @Configuration).
 * En effet, un contrôleur est plus souple.
 */
// @Configuration
public class DefaultViewConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

}
