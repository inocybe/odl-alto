/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.commons.types.rfc7285;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Network Map: defined in RFC 7285 section 11.2.1
 * */
public class RFC7285NetworkMap {

    public static class Meta extends Extensible {

        @JsonProperty("vtag")
        public RFC7285VersionTag vtag = new RFC7285VersionTag();

    }

    /**
     * used for filtered-network-map, RFC7285 secion 11.3.1
     * */
    public static class Filter {

        @JsonProperty("pids")
        public List<String> pids;
        @JsonProperty("address-types")
        public List<String> addressTypes;
    }

    @JsonProperty("meta")
    public Meta meta = new Meta();

    @JsonProperty("network-map")
    public Map<String, RFC7285Endpoint.AddressGroup> map
                    = new LinkedHashMap<String, RFC7285Endpoint.AddressGroup>();
}
