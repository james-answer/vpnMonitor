package com.answerdigital.vpnmonitor;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class VpnMonitorApp {

    public static void main(String args[]) {
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            VpnMonitorApp app = new VpnMonitorApp();

            @Override
            public void run() {
                if (!app.attemptConnectionToSpine()) {
                    app.restartService();
                }
            }
        };
        timer.scheduleAtFixedRate(myTask, 2000, 600000); // Check every 10 minutess
        while (true) {
            try {
                Thread.sleep(3600000);
            } catch (Exception e) {
                // Do Nothing, this stops the application using processing power when it is not trying to re-start the server
            }

        }
    }

    public boolean attemptConnectionToSpine() {
        boolean canConnect = true;
        try {
            System.out.println("Attempt to connect to Spine...");
            Socket socket = new Socket("192.168.54.6", 443);
            System.out.println("Connected to Spine.");
            socket.close();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            canConnect = false;
            System.out.println("Connection Failed - " + e.getMessage());
        }
        return canConnect;
    }

    public void restartService() {
        try {
            System.out.println("Attempt to re-start service...");
            Process process = Runtime.getRuntime().exec("net stop OpenVPNService");
            process.waitFor();
            process = Runtime.getRuntime().exec("net start OpenVPNService");
            process.waitFor();
            System.out.println("Done - " + process.exitValue());
        } catch (Exception e) {
            System.out.println("Failed to restart service - " + e.getMessage());
        }
    }
}
