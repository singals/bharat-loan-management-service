package com.blms;

import com.blms.resources.PingResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlmsApp extends Application<BlmsConfig> {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlmsApp.class);

  public static void main(String[] args) throws Exception {
    new BlmsApp().run(args);
  }

  @Override
  public void run(BlmsConfig config, Environment env) {
    try{
      env.jersey().register(new PingResource());
    }catch (Exception ex){
      LOGGER.error("Unhandled error", ex);
    }

  }
}
