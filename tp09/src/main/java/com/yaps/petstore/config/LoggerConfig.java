package com.yaps.petstore.config;

import java.lang.reflect.Field;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;

/**
 * Inject specific loggers in components.
 * Interesting example of "prototype" beans.
 * <p> from https://medium.com/simars/inject-loggers-in-spring-java-or-kotlin-87162d02d068
 * <ul>
 * <li> Nice : avoids copy/paste ;
 * <li> A bit complex to read as it uses introspection and Aspect Oriented Programming.
 * <li> The author suggests passing the value through a constructor ; it's a bit problematic for me because
 *      logging is really a cross-cutting concern, and should not appear in the explicit dependencies of a class.
 * </ul>
 */
@Configuration
public class LoggerConfig {
    @Configuration
    public class LoggingConfiguration {
        @Bean
        @Scope("prototype")
        public Logger logger(final InjectionPoint ip) {
            // Note the <Class<?>> before map. It's there to give the return type of "map",
            // because the inference algorithm find it sometimes a bit too difficult to infer the correct type.
            // If this code is broken in two elements and the result of Optional.of(ip.getMethodParameter()).<Class<?>>map(MethodParameter::getContainingClass)
            // is assigned to a variable, the correct type is infered and <Class<?>> is not needed.
            
            return LoggerFactory.getLogger(
                    Optional.ofNullable(ip.getMethodParameter()).<Class<?>>map(MethodParameter::getContainingClass)
                            .orElseGet(() -> Optional.ofNullable(ip.getField())
                                    .map(Field::getDeclaringClass)
                                    .orElseThrow(IllegalArgumentException::new)));
        }
    }
}
