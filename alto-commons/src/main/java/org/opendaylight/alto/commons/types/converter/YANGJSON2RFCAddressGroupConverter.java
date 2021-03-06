/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.commons.types.converter;

import org.opendaylight.alto.commons.helper.Converter;

import org.opendaylight.alto.commons.types.rfc7285.RFC7285Endpoint;

import com.fasterxml.jackson.databind.JsonNode;

public class YANGJSON2RFCAddressGroupConverter extends Converter<JsonNode, RFC7285Endpoint.AddressGroup> {
    public YANGJSON2RFCAddressGroupConverter() {
    }

    public YANGJSON2RFCAddressGroupConverter(JsonNode _in) {
        super(_in);
    }

    @Override
    protected Object _convert() {
        JsonNode node = this.in();
        RFC7285Endpoint.AddressGroup ag = new RFC7285Endpoint.AddressGroup();

        for (JsonNode address: node) {
            JsonNode prefixes = address.get("endpointPrefix");
            assert prefixes.isArray();
            for (JsonNode prefix: prefixes) {
                JsonNode ipv4 = prefix.get("ipv4Prefix");
                JsonNode ipv6 = prefix.get("ipv6Prefix");
                if ((ipv4 != null) && (!ipv4.isNull())) {
                    ag.ipv4.add(ipv4.get("value").asText());
                }
                if ((ipv6 != null) && (!ipv6.isNull())) {
                    ag.ipv6.add(ipv6.get("value").asText());
                }
            }
        }
        return ag;
    }

}
