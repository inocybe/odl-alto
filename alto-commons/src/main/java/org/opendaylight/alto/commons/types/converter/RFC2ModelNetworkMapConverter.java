/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.commons.types.converter;

import org.opendaylight.alto.commons.helper.Converter;
import org.opendaylight.alto.commons.types.model150404.ModelNetworkMap;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285NetworkMap;

public class RFC2ModelNetworkMapConverter
        extends Converter<RFC7285NetworkMap, ModelNetworkMap> {

    protected RFC2ModelNetworkMapDataConverter conv = new RFC2ModelNetworkMapDataConverter();

    public RFC2ModelNetworkMapConverter() {
    }

    public RFC2ModelNetworkMapConverter(RFC7285NetworkMap _in) {
        super(_in);
    }

    @Override
    public Object _convert() {
        ModelNetworkMap out = new ModelNetworkMap();
        out.rid = in().meta.vtag.rid;
        out.tag = in().meta.vtag.tag;
        out.map = conv.convert(in().map);
        return out;
    }
}
