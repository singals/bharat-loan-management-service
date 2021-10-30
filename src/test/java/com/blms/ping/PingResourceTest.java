package com.blms.ping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class PingResourceTest {

  public static final ResourceExtension RESOURCES =
      ResourceExtension.builder().addResource(new PingResource()).build();

  @Test
  void testPing() {
    Response response = RESOURCES.target("/ping").request(MediaType.APPLICATION_JSON_TYPE).get();

    assertEquals(Status.OK, response.getStatusInfo());
    assertEquals("pong", response.readEntity(String.class));
  }
}
