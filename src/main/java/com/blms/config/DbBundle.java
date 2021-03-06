package com.blms.config;

import com.blms.BlmsConfig;
import com.blms.customer.Customer;
import com.blms.loan.account.LoanAccount;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;

public class DbBundle extends Configuration {

  public HibernateBundle<BlmsConfig> getDbBundle() {
    return new HibernateBundle<>(LoanAccount.class, Customer.class) {
      @Override
      public PooledDataSourceFactory getDataSourceFactory(BlmsConfig configuration) {
        return configuration.getDatabase();
      }
    };
  }

  public MigrationsBundle<BlmsConfig> getMigrationBundle() {
    return new MigrationsBundle<>() {
      @Override
      public DataSourceFactory getDataSourceFactory(BlmsConfig configuration) {
        return configuration.getDatabase();
      }
    };
  }
}
