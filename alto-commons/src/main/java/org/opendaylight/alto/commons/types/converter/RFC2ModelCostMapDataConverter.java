/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.commons.types.converter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.opendaylight.alto.commons.helper.Converter;
import org.opendaylight.alto.commons.types.model150404.ModelDstCosts;

public class RFC2ModelCostMapDataConverter
    extends Converter<Map<String, Object>, List<ModelDstCosts>> {

  public RFC2ModelCostMapDataConverter() {
  }

  public RFC2ModelCostMapDataConverter(Map<String, Object> _in) {
      super(_in);
  }

  @Override
  protected Object _convert() {
    List<ModelDstCosts> dstCostsList = new LinkedList<ModelDstCosts>();
    for (String dst : in().keySet()) {
      //TODO: Should support different implementations
      ModelDstCosts dstCosts = new ModelDstCosts();
      dstCosts.dst = dst;
      dstCosts.costDefault = in().get(dst).toString();
      dstCostsList.add(dstCosts);
    }
    return dstCostsList;
  }
}
