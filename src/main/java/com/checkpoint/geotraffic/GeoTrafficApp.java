package com.checkpoint.geotraffic;

import java.io.File;
import java.net.InetAddress;

import static com.checkpoint.geotraffic.Constants.*;

public class GeoTrafficApp {

    private static EchoClient client;

    public static void main(String[] args) throws Exception {
        String path = new File(SCRIPT_SEND_ETH_NAME).getAbsolutePath();
        String csvPath = new File(CSV_WITH_IP_RANGES).getAbsolutePath();

        int argsSize = args.length;
        switch (argsSize) {
            case 1:
                if (args[0].equals("-h"))
                    System.out.println(HELP_MESSAGE);
                else
                    System.out.println(UNKNOWN_PARAMETER + HELP_MESSAGE);
                break;
            case 3:
                InetAddress sourceIp = Utils.getIpFromArg(args[0], csvPath);
                InetAddress destinationIp = Utils.getIpFromArg(args[1], csvPath);
                if (sourceIp != null && destinationIp != null) {
                    client = new EchoClient(sourceIp, destinationIp, args[2]);
                    client.runPython(path);
                } else
                    System.out.println(INVALID_IP + HELP_MESSAGE);
                break;
            default:
                System.out.println(WRONG_NUMBER_PARAMETERS + HELP_MESSAGE);
                break;
        }
    }

}

