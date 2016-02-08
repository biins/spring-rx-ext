package org.biins.spring.ext.rx.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Janys
 */

@EnableWebMvc
@EnableAutoConfiguration
@Configuration
public class ObservableSpringConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void init() {
        final List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(requestMappingHandlerAdapter.getReturnValueHandlers());
        handlers.add(0, observableHandlerMethodReturnValueHandler());
        requestMappingHandlerAdapter.setReturnValueHandlers(handlers);
    }

    @Bean
    public ObservableHandlerMethodReturnValueHandler observableHandlerMethodReturnValueHandler() {
        return new ObservableHandlerMethodReturnValueHandler();
    }

    @Bean
    public ObservableStringController controller() {
        return new ObservableStringController();
    }
}
