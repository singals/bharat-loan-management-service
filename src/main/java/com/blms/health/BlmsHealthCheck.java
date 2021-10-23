package com.blms.health;

import com.codahale.metrics.health.HealthCheck;

public class BlmsHealthCheck extends HealthCheck {

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}
