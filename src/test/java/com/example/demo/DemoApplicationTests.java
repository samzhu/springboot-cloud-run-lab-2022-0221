package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@ActiveProfiles("unittest")
@ExtendWith(SpringExtension.class)
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

  @Container
  private static final PostgreSQLContainer database =
      new PostgreSQLContainer("postgres:14.2").withDatabaseName("testdb");

  @Container
  public static final GenericContainer redis =
      new GenericContainer(DockerImageName.parse("redis:6.2.6")).withExposedPorts(6379);

  @Autowired private TestRestTemplate testRestTemplate;

  @DynamicPropertySource
  static void mysqlProperties(DynamicPropertyRegistry registry) {
    log.debug(
        "database url={}, username={}, password={}",
        database.getJdbcUrl(),
        database.getUsername(),
        database.getPassword());
    registry.add("spring.datasource.url", database::getJdbcUrl);
    registry.add("spring.datasource.username", database::getUsername);
    registry.add("spring.datasource.password", database::getPassword);
    log.debug("redis host={}, port={}", redis.getHost(), redis.getFirstMappedPort());
    registry.add("spring.redis.host", redis::getHost);
    registry.add("spring.redis.port", redis::getFirstMappedPort);
  }

  @Test
  @DisplayName("All test")
  public void contextLoads() {
    System.out.println("test");
    assertAll("All test", () -> assertEquals(1, 1));
  }
}
