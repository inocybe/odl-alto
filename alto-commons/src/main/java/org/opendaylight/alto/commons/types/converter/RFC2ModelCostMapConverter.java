/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.commons.types.converter;

import java.util.LinkedList;

import org.opendaylight.alto.commons.helper.Converter;
import org.opendaylight.alto.commons.types.model150404.ModelCostMap;
import org.opendaylight.alto.commons.types.model150404.ModelCostMapData;
import org.opendaylight.alto.commons.types.model150404.ModelCostMapMeta;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285CostMap;

public class RFC2ModelCostMapConverter extends
        Converter<RFC7285CostMap, ModelCostMap> {

    protected RFC2ModelCostMapMetaConverter metaConv = new RFC2ModelCostMapMetaConverter();
    protected RFC2ModelCostMapDataConverter dataConv = new RFC2ModelCostMapDataConverter();

    public RFC2ModelCostMapConverter() {
    }

    public RFC2ModelCostMapConverter(RFC7285CostMap _in) {
        super(_in);
    }

    @Override
    protected Object _convert() {
        ModelCostMap out = new ModelCostMap();
        out.rid = ModelCostMapMeta.getCostMapResourceId(
                in().meta.netmap_tags.get(0).rid, in().meta.costType.metric,
                in().meta.costType.mode);

        // TODO: replace the dummy one in the future
        out.tag = "da65eca2eb7a10ce8b059740b0b2e3f8eb1d4786";

        out.meta = metaConv.convert(in().meta);
        out.map = new LinkedList<ModelCostMapData>();
        for (String src : in().map.keySet()) {
            ModelCostMapData data = new ModelCostMapData();
            data.src = src;
            data.dstCosts = dataConv.convert(in().map.get(src));
            out.map.add(data);
        }
        return out;
    }
}
