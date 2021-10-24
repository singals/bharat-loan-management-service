package com.blms;

import com.blms.config.DbBundle;
import com.blms.customer.CustomerDAO;
import com.blms.customer.CustomerResource;
import com.blms.health.BlmsHealthCheck;
import com.blms.ping.PingResource;
import io.dropwizard.Application;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlmsApp extends Application<BlmsConfig> {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlmsApp.class);

  private HibernateBundle<BlmsConfig> dbConfigBundle;
  private MigrationsBundle<BlmsConfig> migrationBundle;

  public static void main(String[] args) throws Exception {
    new BlmsApp().run(args);
  }

  @Override
  public String getName() {
    return "bharat-loan-management-service";
  }

  @Override
  public void initialize(Bootstrap<BlmsConfig> bootstrap) {
    DbBundle dbBundle = new DbBundle();
    dbConfigBundle = dbBundle.getDbBundle();
    migrationBundle = dbBundle.getMigrationBundle();
    bootstrap.addBundle(dbConfigBundle);
    bootstrap.addBundle(migrationBundle);
  }

  @Override
  public void run(BlmsConfig config, Environment env) {
    try {
      CustomerDAO customerDAO = new CustomerDAO(dbConfigBundle.getSessionFactory());

      env.healthChecks().register("healthCheck", new BlmsHealthCheck());
      env.jersey().register(new PingResource());
      env.jersey().register(new CustomerResource(customerDAO));
    } catch (Exception ex) {
      LOGGER.error("Unhandled error", ex);
    }
  }
}
