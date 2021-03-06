/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.manager;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "alto", name = "set", description = "Set property")
public class AltoSet extends AltoManager {
  private static final Logger log = LoggerFactory.getLogger(AltoSet.class);

  @Argument(index = 0, name = "property-name", description = "Property Name", required = true, multiValued = false)
  String property = null;

  @Argument(index = 1, name = "property-value", description = "Property Value", required = true, multiValued = false)
  String value = null;

  public AltoSet() {
    super();
  }

  @Override
  protected Object doExecute() throws Exception {
    if (AltoManagerConstants.DEFAULT_NETWORK_MAP_PROPERTY.equals(property)) {
      if (!ifNetworkMapExist(value)) {
        throw new RuntimeException("Network map \"" + value + "\" does not exist.");
      }
      setDefaultNetworkMap();
    } else {
      throw new UnsupportedOperationException("Unsupported property \"" + property + "\".");
    }
    return null;
  }

  private boolean ifNetworkMapExist(String resourceId) throws IOException {
    HttpResponse response = httpGet(AltoManagerConstants.NETWORK_MAP_URL + resourceId);
    logResponse(response);
    int statusCode = response.getStatusLine().getStatusCode();
    return (statusCode == 200);
  }

  private void setDefaultNetworkMap() throws IOException {
    log.info("Setting default network map");
    httpPut(AltoManagerConstants.IRD_DEFAULT_NETWORK_MAP_URL, queryData(value));
  }

  private String queryData(String resourceId) {
    return "{\"alto-service:default-alto-network-map\":{\"alto-service:resource-id\":\"" + resourceId + "\"}}";
  }
}
