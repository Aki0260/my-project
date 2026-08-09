package com.example.spring_traing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Training Application entry point.
 */
@SpringBootApplication
public final class SpringTraingApplication {

  /**
   * Private constructor for utility class.
   */
  private SpringTraingApplication() {
  }

  /**
   * Application main method.
   *
   * @param args command line arguments
   */
  public static void main(final String[] args) {
    SpringApplication.run(SpringTraingApplication.class, args);
  }
}
