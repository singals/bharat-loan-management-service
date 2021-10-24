package com.blms;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlmsConfig extends Configuration {

  @Valid @NotNull private DataSourceFactory database = new DataSourceFactory();
}
