import sys
from socket import *
from random import randint

def sendeth(ethernet_packet, payload):
    """Send raw Ethernet packet on interface."""
    s = socket(AF_PACKET, SOCK_RAW)
    # From the docs: "For raw packet
    # sockets the address is a tuple (ifname, proto [,pkttype [,hatype]])"
    s.bind((sys.argv[3], 0))
    return s.send(ethernet_packet + payload)

def pack(byte_sequence):
    """Convert list of bytes to byte string."""
    return b"".join(map(chr, byte_sequence))

if __name__ == "__main__":
    # dst=52:54:00:12:35:02, src=fe:ed:fa:ce:be:ef, type=0x0800 (IPv4)
    ethernet_packet = [0x52, 0x54, 0x00, 0x12, 0x35, 0x02, 0xfe, 0xed, 0xfa,
                       0xce, 0xbe, 0xef, 0x08, 0x00]

    # (vg.no), checksum, etc. length 0x002A, UDP 0x11
    ipv4_header = [0x45, 0x00, 0x00, 0x2A, 0x05, 0x9f, 0x40, 0x00, 0x40, 0x11,
                   0x2f, 0x93]

    # add source ip
    src_ip = sys.argv[1].split('.')
    ipv4_header.append(int(src_ip[0]))
    ipv4_header.append(int(src_ip[1]))
    ipv4_header.append(int(src_ip[2]))
    ipv4_header.append(int(src_ip[3]))

    # add destination ip
    dst_ip = sys.argv[2].split('.')
    ipv4_header.append(int(dst_ip[0]))
    ipv4_header.append(int(dst_ip[1]))
    ipv4_header.append(int(dst_ip[2]))
    ipv4_header.append(int(dst_ip[3]))

    # add source port
    src_port = randint(0, 65535)
    print("Generated src_port: %s hex: %s" % (src_port,  hex(src_port)))
    src_port_first_part = src_port / 256
    src_port_second_part = src_port - (256 * src_port_first_part)
    ipv4_header.append(src_port_first_part)
    ipv4_header.append(src_port_second_part)

    # add destination port
    dst_port = randint(0, 65535)
    print("Generated dst_port: %s hex: %s" % (dst_port, hex(dst_port)))
    dst_port_first_part = dst_port / 256
    dst_port_second_part = dst_port - (256 * dst_port_first_part)
    ipv4_header.append(dst_port_first_part)
    ipv4_header.append(dst_port_second_part)

    print("ipv4_header: %s" % ipv4_header)
    # echo request, checksum 0000, etc. length 0016, "Random message"
    data = [0x00, 0x16, 0x00, 0x00, 0x52, 0x61, 0x6e, 0x64,
            0x6f, 0x6d, 0x20, 0x4d, 0x45, 0x53, 0x53, 0x41,
            0x47, 0x45]

    print("type data %s" % type(data))
    payload = "".join(map(chr, ipv4_header + data))
    # Construct Ethernet packet with an IPv4 ICMP PING request as payload
    r = sendeth(pack(ethernet_packet),
                pack(ipv4_header + data))
    print("Sent Ethernet w/IPv4 ICMP PING payload of length %d bytes" % r)