/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.alto.basic.endpointcostservice.helper;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Ipv4Prefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Ipv6Prefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.service.model.endpointcost.rfc7285.rev151021.Ipv4AddressData;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.service.model.endpointcost.rfc7285.rev151021.Ipv6AddressData;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.service.model.endpointcost.rfc7285.rev151021.TypedAddressData;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPPrefixHelper {
    private int bits;
    private int ipv4Subnet;
    private int ipv4Mask;
    private int[] ipv6Subnet = new int[4];
    private int[] ipv6Mask = new int[4];
    private String prefix;
    private boolean initialized = false;

    /**
     * Initial IPPrefixHelper.
     * @param ipPrefix
     * @throws UnknownHostException
     */
    public IPPrefixHelper(IpPrefix ipPrefix) throws UnknownHostException {
        initializeIpv4(ipPrefix.getIpv4Prefix());
        initializeIpv6(ipPrefix.getIpv6Prefix());
    }

    /**
     * Initial IPPrefixHelper
     * @param ipv4Prefix
     * @throws UnknownHostException
     */
    public IPPrefixHelper(Ipv4Prefix ipv4Prefix) throws UnknownHostException {
        initializeIpv4(ipv4Prefix);
    }

    private void initializeIpv4(Ipv4Prefix ipv4Prefix) throws UnknownHostException {
        if (ipv4Prefix != null) {
            subNet(ipv4Prefix);
            this.prefix = ipv4Prefix.getValue();
            this.initialized = true;
        }
    }

    /**
     * Initial IPPrefixHelper
     * @param ipv6Prefix
     * @throws UnknownHostException
     */
    public IPPrefixHelper(Ipv6Prefix ipv6Prefix) throws UnknownHostException {
        initializeIpv6(ipv6Prefix);
    }

    private void initializeIpv6(Ipv6Prefix ipv6Prefix) throws UnknownHostException {
        if (ipv6Prefix != null) {
            subNet(ipv6Prefix);
            this.prefix = ipv6Prefix.getValue();
            this.initialized = true;
        }
    }

    private void subNet(Ipv6Prefix ipv6Prefix) throws UnknownHostException {
        String[] value = ipv6Prefix.getValue().split("[/]");
        this.bits = Integer.parseInt(value[1]);
        ipv6Mask();
        this.ipv6Subnet = ipv6ToInt(value[0]);
    }

    public static int[] ipv6ToInt(String address) throws UnknownHostException {
        byte[] b = toBytes(address);
        int addr1 = ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16)
                | ((b[2] & 0xFF) << 8) | ((b[3] & 0xFF) << 0);

        int addr2 = ((b[4] & 0xFF) << 24) | ((b[5] & 0xFF) << 16)
                | ((b[6] & 0xFF) << 8) | ((b[7] & 0xFF) << 0);

        int addr3 = ((b[8] & 0xFF) << 24) | ((b[9] & 0xFF) << 16)
                | ((b[10] & 0xFF) << 8) | ((b[11] & 0xFF) << 0);

        int addr4 = ((b[12] & 0xFF) << 24) | ((b[13] & 0xFF) << 16)
                | ((b[14] & 0xFF) << 8) | ((b[15] & 0xFF) << 0);
        int[] addrs = { addr1, addr2, addr3, addr4 };
        return addrs;
    }
    public static int ipv4ToInt(String addr) throws UnknownHostException {
        Inet4Address a = (Inet4Address) InetAddress.getByName(addr);
        byte[] b = a.getAddress();
        int addrInt = ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16)
                | ((b[2] & 0xFF) << 8) | ((b[3] & 0xFF) << 0);
        return addrInt;
    }

    private static byte[] toBytes(String address) throws UnknownHostException {
        Inet6Address addr = (Inet6Address) InetAddress.getByName(address);
        return addr.getAddress();
    }

    private void ipv6Mask() {
        if (this.bits <= 32) {
            this.ipv6Mask[0] = -1 << (32 - this.bits);
            this.ipv6Mask[1] = 0;
            this.ipv6Mask[2] = 0;
            this.ipv6Mask[3] = 0;
        }

        if (this.bits > 32 && this.bits <= 64) {
            this.ipv6Mask[0] = -1;
            this.ipv6Mask[1] = -1 << (64 - this.bits);
            this.ipv6Mask[2] = 0;
            this.ipv6Mask[3] = 0;
        }

        if (this.bits > 64 && this.bits <= 96) {
            this.ipv6Mask[0] = -1;
            this.ipv6Mask[1] = -1;
            this.ipv6Mask[2] = -1 << (96 - this.bits);
            this.ipv6Mask[3] = 0;
        }

        if (this.bits > 96 && this.bits <= 128) {
            this.ipv6Mask[0] = -1;
            this.ipv6Mask[1] = -1;
            this.ipv6Mask[2] = -1;
            this.ipv6Mask[3] = -1 << (128 - this.bits);
        }
    }

    private void subNet(Ipv4Prefix ipv4Prefix) throws UnknownHostException {
        String[] value = ipv4Prefix.getValue().split("/");
        this.bits = Integer.parseInt(value[1]);
        this.ipv4Mask = -1 << (32 - this.bits);
        this.ipv4Subnet = ipv4ToInt(value[0]);
    }

    /**
     * Check if two address are matches
     * @param address
     * @return the result of match
     * @throws UnknownHostException
     */
    public boolean match(TypedAddressData address)
            throws UnknownHostException {
        if (this.initialized == false) {
            return this.initialized;
        }

        if (address.getAddress() instanceof Ipv4AddressData) {
            int addr = ipv4ToInt(((Ipv4AddressData) address.getAddress()).getIpv4().getValue().replace("ipv4:", ""));
            return ((ipv4Subnet & ipv4Mask) == (addr & ipv4Mask));
        }

        if (address.getAddress() instanceof Ipv6AddressData) {
            int[] addr = ipv6ToInt(((Ipv6AddressData) address.getAddress()).getIpv6().getValue().replace("ipv6:", ""));
            return ((ipv6Subnet[0] & ipv6Mask[0]) == (addr[0] & ipv6Mask[0]))
                    && ((ipv6Subnet[1] & ipv6Mask[1]) == (addr[1] & ipv6Mask[1]))
                    && ((ipv6Subnet[2] & ipv6Mask[2]) == (addr[2] & ipv6Mask[2]))
                    && ((ipv6Subnet[3] & ipv6Mask[3]) == (addr[3] & ipv6Mask[3]));
        }
        return false;
    }

    public int getSubnetLength() {
        return this.bits;
    }

    public String getIpPrefix() {
        return this.prefix;
    }
}