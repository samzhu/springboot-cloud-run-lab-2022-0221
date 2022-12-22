package com.example.demo.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedisConfig {

  // @Bean
  // @ConditionalOnProperty(name = "spring.redis.ssl", havingValue = "true")
  // public LettuceClientConfigurationBuilderCustomizer builderCustomizer() {
  //   log.info("use ssl and disable ssl verification");
  //   return builder -> builder.useSsl().disablePeerVerification();
  // }
}
