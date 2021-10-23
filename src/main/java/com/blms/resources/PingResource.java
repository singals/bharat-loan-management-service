package com.blms.resources;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public class PingResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(PingResource.class);

  @GET
  public String ping() {
    return "pong";
  }
}
