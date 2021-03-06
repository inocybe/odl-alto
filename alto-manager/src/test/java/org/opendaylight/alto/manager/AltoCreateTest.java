/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.alto.manager;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Matchers.any;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class AltoCreateTest {
    @Mock(name = "httpClient")
    private HttpClient httpClient;

    @InjectMocks
    @Spy
    private AltoCreate altoCreate = new AltoCreate();

    private final String NETWORK_MAP = "[{\"meta\":{\"vtag\":{\"resource-id\":\"my-alternate-network-map\",\"tag\":\"88e5d857507ed921f157a7c3413af0c4\"}},\"network-map\":{\"dc1\":{\"ipv4\":[\"101.0.0.0/16\"]},\"dc2\":{\"ipv4\":[\"102.0.0.0/16\"]},\"dc3\":{\"ipv4\":[\"103.0.0.0/16\"]},\"dc4\":{\"ipv4\":[\"104.0.0.0/16\"]},\"default\":{\"ipv4\":[\"0.0.0.0/0\"],\"ipv6\":[\"::/0\"]},\"linklocal\":{\"ipv4\":[\"169.254.0.0/16\"],\"ipv6\":[\"FF80::/10\"]},\"loopback\":{\"ipv4\":[\"127.0.0.0/8\"],\"ipv6\":[\"::1/128\"]},\"private\":{\"ipv4\":[\"10.0.0.0/8\",\"172.16.0.0/12\",\"192.168.0.0/16\"],\"ipv6\":[\"FC00::/7\"]},\"user1\":{\"ipv4\":[\"201.0.0.0/16\"]},\"user2\":{\"ipv4\":[\"202.0.0.0/16\"]},\"user3\":{\"ipv4\":[\"203.0.0.0/16\"]},\"user4\":{\"ipv4\":[\"204.0.0.0/16\"]}}},{\"meta\":{\"vtag\":{\"resource-id\":\"my-default-network-map\",\"tag\":\"200dc84a734fb99516eceffcb2c8458a\"}},\"network-map\":{\"default\":{\"ipv4\":[\"0.0.0.0/0\"],\"ipv6\":[\"::/0\"]},\"linklocal\":{\"ipv4\":[\"169.254.0.0/16\"],\"ipv6\":[\"FF80::/10\"]},\"loopback\":{\"ipv4\":[\"127.0.0.0/8\"],\"ipv6\":[\"::1/128\"]},\"mine\":{\"ipv4\":[\"100.0.0.0/8\"]},\"mine1\":{\"ipv4\":[\"100.0.0.0/10\"]},\"mine1a\":{\"ipv4\":[\"100.0.64.0/24\",\"100.0.192.0/24\",\"100.0.1.0/24\"]},\"mine2\":{\"ipv4\":[\"100.64.0.0/10\"]},\"mine3\":{\"ipv4\":[\"100.128.0.0/10\"]},\"peer1\":{\"ipv4\":[\"130.0.0.0/16\",\"128.0.0.0/16\"],\"ipv6\":[\"2001:DB8::/33\"]},\"peer2\":{\"ipv4\":[\"131.0.0.0/16\",\"129.0.0.0/16\"],\"ipv6\":[\"2001:DB8:8000::/33\"]},\"private\":{\"ipv4\":[\"10.0.0.0/8\",\"172.16.0.0/12\",\"192.168.0.0/16\"],\"ipv6\":[\"FC00::/7\"]},\"tran1\":{\"ipv4\":[\"132.0.0.0/16\"]},\"tran2\":{\"ipv4\":[\"135.0.0.0/16\"]}}}]";
    private final String COST_MAP = "[{\"meta\":{\"dependent-vtags\":[{\"resource-id\":\"my-default-network-map\",\"tag\":\"200dc84a734fb99516eceffcb2c8458a\"}],\"cost-type\":{\"cost-mode\":\"numerical\",\"cost-metric\":\"routingcost\"}},\"cost-map\":{\"default\":{\"default\":1.0,\"mine\":75.0,\"mine1\":75.0,\"mine1a\":75.0,\"mine2\":75.0,\"mine3\":75.0,\"private\":75.0},\"linklocal\":{\"linklocal\":1.0},\"loopback\":{\"loopback\":0.0},\"mine\":{\"default\":75.0,\"mine\":1.0,\"mine1\":15.0,\"mine1a\":15.0,\"mine2\":15.0,\"mine3\":15.0,\"peer1\":30.0,\"peer2\":30.0,\"tran1\":50.0,\"tran2\":50.0},\"mine1\":{\"default\":75.0,\"mine\":15.0,\"mine1\":1.0,\"mine1a\":2.5,\"mine2\":5.0,\"mine3\":7.0,\"peer1\":20.0,\"peer2\":25.0,\"tran1\":40.0,\"tran2\":45.0},\"mine1a\":{\"default\":75.0,\"mine\":15.0,\"mine1\":2.0,\"mine1a\":1.0,\"mine2\":7.0,\"mine3\":9.0,\"peer1\":22.0,\"peer2\":24.0,\"tran1\":42.0,\"tran2\":48.0},\"mine2\":{\"default\":75.0,\"mine\":15.0,\"mine1\":5.5,\"mine1a\":7.0,\"mine2\":1.0,\"mine3\":6.0,\"peer1\":23.0,\"peer2\":25.0,\"tran1\":43.0,\"tran2\":46.0},\"mine3\":{\"default\":75.0,\"mine\":15.0,\"mine1\":7.0,\"mine1a\":9.0,\"mine2\":6.0,\"mine3\":1.0,\"peer1\":25.0,\"peer2\":28.0,\"tran1\":45.0,\"tran2\":49.0},\"peer1\":{\"mine\":30.0,\"mine1\":20.0,\"mine1a\":22.0,\"mine2\":23.0,\"mine3\":25.0,\"peer1\":1.0},\"peer2\":{\"mine\":30.0,\"mine1\":25.0,\"mine1a\":24.0,\"mine2\":25.0,\"mine3\":28.0,\"peer2\":1.0},\"private\":{\"default\":75.0,\"private\":1.0},\"tran1\":{\"mine\":50.0,\"mine1\":40.0,\"mine1a\":42.0,\"mine2\":43.0,\"mine3\":45.0,\"tran1\":1.0},\"tran2\":{\"mine\":50.0,\"mine1\":45.0,\"mine1a\":48.0,\"mine2\":46.0,\"mine3\":49.0,\"tran2\":1.0}}},{\"meta\":{\"dependent-vtags\":[{\"resource-id\":\"my-default-network-map\",\"tag\":\"200dc84a734fb99516eceffcb2c8458a\"}],\"cost-type\":{\"cost-mode\":\"numerical\",\"cost-metric\":\"hopcount\"}},\"cost-map\":{\"default\":{\"default\":1,\"mine\":10,\"mine1\":10,\"mine1a\":10,\"mine2\":10,\"mine3\":10,\"private\":10},\"linklocal\":{\"linklocal\":1},\"loopback\":{\"loopback\":0},\"mine\":{\"default\":10,\"mine\":1,\"mine1\":3,\"mine1a\":3,\"mine2\":3,\"mine3\":3,\"peer1\":5,\"peer2\":6,\"tran1\":8,\"tran2\":8},\"mine1\":{\"default\":10,\"mine\":3,\"mine1\":1,\"mine1a\":2,\"mine2\":2,\"mine3\":2,\"peer1\":4,\"peer2\":5,\"tran1\":6,\"tran2\":7},\"mine1a\":{\"default\":10,\"mine\":3,\"mine1\":2,\"mine1a\":1,\"mine2\":2,\"mine3\":3,\"peer1\":5,\"peer2\":6,\"tran1\":7,\"tran2\":8},\"mine2\":{\"default\":10,\"mine\":3,\"mine1\":2,\"mine1a\":2,\"mine2\":1,\"mine3\":2,\"peer1\":4,\"peer2\":5,\"tran1\":6,\"tran2\":7},\"mine3\":{\"default\":10,\"mine\":3,\"mine1\":2,\"mine1a\":3,\"mine2\":2,\"mine3\":1,\"peer1\":4,\"peer2\":5,\"tran1\":6,\"tran2\":7},\"peer1\":{\"mine\":5,\"mine1\":4,\"mine1a\":5,\"mine2\":4,\"mine3\":4,\"peer1\":1},\"peer2\":{\"mine\":6,\"mine1\":5,\"mine1a\":6,\"mine2\":5,\"mine3\":5,\"peer2\":1},\"private\":{\"default\":10,\"private\":1},\"tran1\":{\"mine\":8,\"mine1\":6,\"mine1a\":7,\"mine2\":6,\"mine3\":6,\"tran1\":1},\"tran2\":{\"mine\":8,\"mine1\":7,\"mine1a\":8,\"mine2\":7,\"mine3\":7,\"tran2\":1}}},{\"meta\":{\"dependent-vtags\":[{\"resource-id\":\"my-alternate-network-map\",\"tag\":\"88e5d857507ed921f157a7c3413af0c4\"}],\"cost-type\":{\"cost-mode\":\"numerical\",\"cost-metric\":\"routingcost\"}},\"cost-map\":{\"dc1\":{\"dc1\":0.0,\"dc2\":5.0,\"dc3\":5.0,\"dc4\":5.0,\"default\":50.0,\"user1\":10.0,\"user2\":20.0,\"user3\":30.0,\"user4\":40.0},\"dc2\":{\"dc1\":5.0,\"dc2\":0.0,\"dc3\":5.0,\"dc4\":5.0,\"default\":50.0,\"user1\":20.0,\"user2\":10.0,\"user3\":20.0,\"user4\":30.0},\"dc3\":{\"dc1\":5.0,\"dc2\":5.0,\"dc3\":0.0,\"dc4\":5.0,\"default\":50.0,\"user1\":30.0,\"user2\":20.0,\"user3\":10.0,\"user4\":20.0},\"dc4\":{\"dc1\":5.0,\"dc2\":5.0,\"dc3\":5.0,\"dc4\":0.0,\"default\":50.0,\"user1\":40.0,\"user2\":30.0,\"user3\":20.0,\"user4\":10.0},\"default\":{\"dc1\":50.0,\"dc2\":50.0,\"dc3\":50.0,\"dc4\":50.0,\"default\":0.0},\"user1\":{\"dc1\":10.0,\"dc2\":20.0,\"dc3\":30.0,\"dc4\":40.0,\"user1\":0.0},\"user2\":{\"dc1\":20.0,\"dc2\":10.0,\"dc3\":20.0,\"dc4\":30.0,\"user2\":0.0},\"user3\":{\"dc1\":30.0,\"dc2\":20.0,\"dc3\":10.0,\"dc4\":20.0,\"user3\":0.0},\"user4\":{\"dc1\":40.0,\"dc2\":30.0,\"dc3\":20.0,\"dc4\":10.0,\"user4\":0.0}}},{\"meta\":{\"dependent-vtags\":[{\"resource-id\":\"my-alternate-network-map\",\"tag\":\"88e5d857507ed921f157a7c3413af0c4\"}],\"cost-type\":{\"cost-mode\":\"numerical\",\"cost-metric\":\"hopcount\"}},\"cost-map\":{\"dc1\":{\"dc1\":0,\"dc2\":1,\"dc3\":1,\"dc4\":1,\"default\":8,\"user1\":3,\"user2\":4,\"user3\":5,\"user4\":6},\"dc2\":{\"dc1\":1,\"dc2\":0,\"dc3\":1,\"dc4\":1,\"default\":8,\"user1\":4,\"user2\":3,\"user3\":4,\"user4\":5},\"dc3\":{\"dc1\":1,\"dc2\":1,\"dc3\":0,\"dc4\":1,\"default\":8,\"user1\":5,\"user2\":4,\"user3\":3,\"user4\":4},\"dc4\":{\"dc1\":1,\"dc2\":1,\"dc3\":1,\"dc4\":0,\"default\":8,\"user1\":6,\"user2\":5,\"user3\":4,\"user4\":3},\"default\":{\"dc1\":8,\"dc2\":8,\"dc3\":8,\"dc4\":8,\"default\":0},\"user1\":{\"dc1\":3,\"dc2\":4,\"dc3\":5,\"dc4\":6,\"user1\":0},\"user2\":{\"dc1\":4,\"dc2\":3,\"dc3\":4,\"dc4\":5,\"user2\":0},\"user3\":{\"dc1\":5,\"dc2\":4,\"dc3\":3,\"dc4\":4,\"user3\":0},\"user4\":{\"dc1\":6,\"dc2\":5,\"dc3\":4,\"dc4\":3,\"user4\":0}}}]";
    private final String ENDPOINT_PROPERTY_MAP = "{\"endpoint-properties\":{\"ipv6:ff80:1:2::\":{\"my-alternate-network-map.pid\":\"linklocal\",\"my-default-network-map.pid\":\"linklocal\"},\"ipv6:fc00:1::\":{\"my-alternate-network-map.pid\":\"private\",\"my-default-network-map.pid\":\"private\"},\"ipv6:2001:db8:8000::1\":{\"priv:ietf-type\":\"peer\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"peer2\"},\"ipv4:127.255.255.255\":{\"my-alternate-network-map.pid\":\"loopback\",\"my-default-network-map.pid\":\"loopback\"},\"ipv4:127.0.0.1\":{\"my-alternate-network-map.pid\":\"loopback\",\"my-default-network-map.pid\":\"loopback\"},\"ipv4:104.0.0.1\":{\"my-alternate-network-map.pid\":\"dc4\",\"my-default-network-map.pid\":\"default\"},\"ipv4:103.0.0.1\":{\"my-alternate-network-map.pid\":\"dc3\",\"my-default-network-map.pid\":\"default\"},\"ipv4:102.0.0.1\":{\"my-alternate-network-map.pid\":\"dc2\",\"my-default-network-map.pid\":\"default\"},\"ipv4:101.1.0.1\":{\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"default\"},\"ipv4:101.0.0.1\":{\"my-alternate-network-map.pid\":\"dc1\",\"my-default-network-map.pid\":\"default\"},\"ipv4:100.75.0.1\":{\"priv:ietf-type\":\"mine\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"mine2\"},\"ipv4:0.0.0.1\":{\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"default\"},\"ipv4:10.1.2.3\":{\"my-alternate-network-map.pid\":\"private\",\"my-default-network-map.pid\":\"private\"},\"ipv4:100.0.0.1\":{\"priv:ietf-type\":\"mine\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"mine1\"},\"ipv4:100.0.1.1\":{\"priv:ietf-type\":\"mine\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"mine1a\"},\"ipv4:100.0.192.1\":{\"priv:ietf-type\":\"mine\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"mine1a\"},\"ipv4:100.0.64.1\":{\"priv:ietf-type\":\"mine\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"mine1a\"},\"ipv4:100.130.0.1\":{\"priv:ietf-type\":\"mine\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"mine3\"},\"ipv4:100.200.0.1\":{\"priv:ietf-type\":\"mine\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"mine\"},\"ipv4:128.0.0.1\":{\"priv:ietf-type\":\"peer\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"peer1\"},\"ipv4:129.0.0.1\":{\"priv:ietf-type\":\"peer\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"peer2\"},\"ipv4:130.0.0.1\":{\"priv:ietf-type\":\"peer\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"peer1\"},\"ipv4:131.0.0.1\":{\"priv:ietf-type\":\"peer\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"peer2\"},\"ipv4:132.0.0.1\":{\"priv:ietf-type\":\"transit\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"tran1\"},\"ipv4:135.0.0.1\":{\"priv:ietf-type\":\"transit\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"tran2\"},\"ipv4:169.254.1.2\":{\"my-alternate-network-map.pid\":\"linklocal\",\"my-default-network-map.pid\":\"linklocal\"},\"ipv4:201.0.0.1\":{\"my-alternate-network-map.pid\":\"user1\",\"my-default-network-map.pid\":\"default\"},\"ipv4:201.1.2.3\":{\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"default\"},\"ipv4:202.0.0.1\":{\"my-alternate-network-map.pid\":\"user2\",\"my-default-network-map.pid\":\"default\"},\"ipv4:203.0.0.1\":{\"my-alternate-network-map.pid\":\"user3\",\"my-default-network-map.pid\":\"default\"},\"ipv4:204.0.0.1\":{\"my-alternate-network-map.pid\":\"user4\",\"my-default-network-map.pid\":\"default\"},\"ipv4:99.0.0.1\":{\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"default\"},\"ipv6:::1\":{\"my-alternate-network-map.pid\":\"loopback\",\"my-default-network-map.pid\":\"loopback\"},\"ipv6:::2\":{\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"default\"},\"ipv6:2001:db8::\":{\"priv:ietf-type\":\"peer\",\"my-alternate-network-map.pid\":\"default\",\"my-default-network-map.pid\":\"peer1\"}},\"meta\":{\"dependent-vtags\":[{\"tag\":\"200dc84a734fb99516eceffcb2c8458a\",\"resource-id\":\"my-default-network-map\"},{\"tag\":\"88e5d857507ed921f157a7c3413af0c4\",\"resource-id\":\"my-alternate-network-map\"}]}}";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoExecute() throws Exception {
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        doReturn(NETWORK_MAP).when(altoCreate).readFromFile("network-maps");
        doReturn(COST_MAP).when(altoCreate).readFromFile("cost-maps");
        doReturn(ENDPOINT_PROPERTY_MAP).when(altoCreate).readFromFile("endpoint-property-map");
        doReturn(response).when(httpClient).execute(any(HttpPut.class));
        doReturn(statusLine).when(response).getStatusLine();
        doReturn(200).when(statusLine).getStatusCode();

        altoCreate.resourceType = "network-map";
        altoCreate.resourceFile = "network-maps";
        altoCreate.doExecute();
        verify(httpClient, atLeastOnce()).execute(any(HttpPut.class));
        altoCreate.resourceType = "cost-map";
        altoCreate.resourceFile = "cost-maps";
        altoCreate.doExecute();
        verify(httpClient, atLeastOnce()).execute(any(HttpPut.class));
        altoCreate.resourceType = "endpoint-property-map";
        altoCreate.resourceFile = "endpoint-property-map";
        altoCreate.doExecute();
        verify(httpClient, atLeastOnce()).execute(any(HttpPut.class));
        altoCreate.resourceType = "otherwise";
        try {
            altoCreate.doExecute();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), UnsupportedOperationException.class);
        }
    }
}
