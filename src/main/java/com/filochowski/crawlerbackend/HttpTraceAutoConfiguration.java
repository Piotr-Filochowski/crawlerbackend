package com.filochowski.crawlerbackend;

import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceProperties;
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.actuate.web.trace.reactive.HttpTraceWebFilter;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "management.trace.http", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(HttpTraceProperties.class)
public class HttpTraceAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(HttpTraceRepository.class)
  public InMemoryHttpTraceRepository traceRepository() {
    return new InMemoryHttpTraceRepository();
  }

  @Bean
  @ConditionalOnMissingBean
  public HttpExchangeTracer httpExchangeTracer(HttpTraceProperties traceProperties) {
    return new HttpExchangeTracer(traceProperties.getInclude());
  }

  @ConditionalOnWebApplication(type = Type.SERVLET)
  static class ServletTraceFilterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HttpTraceFilter httpTraceFilter(HttpTraceRepository repository,
        HttpExchangeTracer tracer) {
      return new HttpTraceFilter(repository, tracer);
    }

  }

  @ConditionalOnWebApplication(type = Type.REACTIVE)
  static class ReactiveTraceFilterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HttpTraceWebFilter httpTraceWebFilter(HttpTraceRepository repository,
        HttpExchangeTracer tracer, HttpTraceProperties traceProperties) {
      return new HttpTraceWebFilter(repository, tracer,
          traceProperties.getInclude());
    }

  }

}