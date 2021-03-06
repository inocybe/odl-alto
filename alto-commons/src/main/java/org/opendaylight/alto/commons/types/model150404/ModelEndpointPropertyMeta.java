/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.commons.types.model150404;

import java.util.LinkedList;
import java.util.List;

import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.service.types.rev150404.endpoint.property.map.Meta;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.service.types.rev150404.dependent.vtags.DependentVtags;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.DataContainer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelEndpointPropertyMeta implements Meta {

  @JsonProperty("alto-service:dependent-vtags")
  public List<ModelDependentVtag> dependentVtags = new LinkedList<ModelDependentVtag>();

  @Override
  public Class<? extends DataContainer> getImplementedInterface() {
    return Meta.class;
  }

  @Override
  public List<DependentVtags> getDependentVtags() {
    return new LinkedList<DependentVtags>(dependentVtags);
  }

  @Override
  public <E extends Augmentation<Meta>> E getAugmentation(Class<E> arg0) {
    return null;
  }
}
