package com.checkpoint.geotraffic;

import sun.net.util.IPAddressUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class Utils {

    /**
     * Get InetAddress from argument
     *
     * @param arg     argument passed to application
     * @param csvPath path to csv file with ip ranges
     * @return InetAddress
     */
    public static InetAddress getIpFromArg(String arg, String csvPath) {
        InetAddress inetAddress = null;
        if (arg.length() == 3) {
            inetAddress = getIpByCountry(csvPath, arg);
        } else if (IPAddressUtil.isIPv4LiteralAddress(arg)) {
            inetAddress = getInetAddress(arg);
        }
        return inetAddress;
    }

    /**
     * Get InetAddress from String ip
     *
     * @param ip ip as String
     * @return InetAddress
     */
    public static InetAddress getInetAddress(String ip) {
        byte[] address = IPAddressUtil.textToNumericFormatV4(ip);
        InetAddress inetAddress = null;
        try {
            inetAddress = Inet4Address.getByAddress(address);
        } catch (UnknownHostException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return inetAddress;
    }

    /**
     * Get Ip by conunty code
     *
     * @param csvPath     csv file with ip ranges
     * @param countryCode String county cody XXX
     * @return InetAddress
     */
    public static InetAddress getIpByCountry(String csvPath, String countryCode) {
        String line;
        String cvsSplitBy = ",";
        countryCode = "\"" + countryCode + "\"";

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            while ((line = br.readLine()) != null) {
                if (!String.valueOf(line.charAt(0)).equals("#")) {
                    String[] country = line.split(cvsSplitBy);
                    if (countryCode.equals(country[5])) {
                        Random rn = new Random();
                        String ipStart = country[0].replace("\"", "");
                        String ipEnd = country[1].replace("\"", "");
                        int range = Integer.valueOf(ipEnd) - Integer.valueOf(ipStart) + 1;
                        int ipResult = rn.nextInt(range) + Integer.valueOf(ipStart);
                        return getInetAddress(String.valueOf(ipResult));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return null;
    }
}
