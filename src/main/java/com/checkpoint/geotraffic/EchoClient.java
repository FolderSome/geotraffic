package com.checkpoint.geotraffic;

import java.io.*;
import java.net.InetAddress;

public class EchoClient {

    private InetAddress inetAddressSource;
    private InetAddress inetAddressDestination;
    private String interfase;

    public EchoClient(InetAddress inetAddressSource, InetAddress inetAddressDestination, String interfase) {
        this.inetAddressSource = inetAddressSource;
        this.inetAddressDestination = inetAddressDestination;
        this.interfase = interfase;
    }

    public void runPython(String scriptPath) throws IOException, InterruptedException {
        String[] cmd = {
                "python",
                scriptPath,
                inetAddressSource.getHostAddress(),
                inetAddressDestination.getHostAddress(),
                interfase,
        };

        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();

        String line;

        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = error.readLine()) != null) {
            System.out.println(line);
        }
        error.close();

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }

        input.close();

        OutputStream outputStream = p.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println();
        printStream.flush();
        printStream.close();
    }

}
