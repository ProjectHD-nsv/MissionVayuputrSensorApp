package me.electronicsboy.missionvayuputr.sensor.app.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
Based on https://github.com/ElectroBoy404NotFound/YAPCA/blob/33-todo-move-processing-to-server-side-app-rewrite/app/src/main/java/me/electronicsboy/yapca/util/Client.java
 */
public class Client {
    private Socket socket;
    private boolean connected = false;
    private boolean inerror = false;
    private Exception error;
    private PrintWriter printWriter;


    public Client(DataListener dl, String ip, int port) {
        new Thread(() -> {
            try {
                Client.this.socket = new Socket(ip, port);
                System.out.println("Connected");
                printWriter = new PrintWriter(socket.getOutputStream());
                connected = true;
            } catch (IOException e) {
                inerror = true;
                error = e;
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

            BufferedReader bf;
            try {
                bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                while(true) {
                    String line;
                    while ((line = bf.readLine()) != null) {
                        try {
                            dl.gotData(new JSONObject(line));
                        } catch (Exception e) {
                            sendDataNonBlocking(new JSONObject().put("status", 1).put("resend", true).toString());
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "Client Data receive Thread").start();
    }

    public boolean isConnected() {
        return connected;
    }
    public boolean isInError() {
        return inerror;
    }
    public Exception getError() {
        return error;
    }
    public void sendDataNonBlocking(String data) {
        new Thread(() -> {
            while(printWriter == null) continue;
            printWriter.println(data);
            printWriter.flush();
        }, "DataSendThread").start();
    }
    public interface DataListener {
        void gotData(JSONObject data) throws JSONException;
    }
}