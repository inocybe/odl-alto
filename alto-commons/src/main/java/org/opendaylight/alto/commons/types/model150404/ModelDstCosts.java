package org.opendaylight.alto.commons.types.model150404;

import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.rev150404.cost.map.map.DstCosts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.rev150404.cost.map.map.DstCostsKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.service.types.rev150404.PidName;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.DataContainer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDstCosts implements DstCosts {

  @JsonProperty("alto-service:dst")
  public String dst = null;
  
  @JsonProperty("alto-service:cost")
  public Object cost = null;
  
  @JsonIgnore
  @Override
  public Class<? extends DataContainer> getImplementedInterface() {
    return DstCosts.class;
  }

  @JsonIgnore
  @Override
  public <E extends Augmentation<DstCosts>> E getAugmentation(Class<E> arg0) {
    return null;
  }

  @JsonIgnore
  @Override
  public PidName getDst() {
    return new PidName(dst);
  }

  @JsonIgnore
  @Override
  public DstCostsKey getKey() {
    return new DstCostsKey(getDst());
  }

}