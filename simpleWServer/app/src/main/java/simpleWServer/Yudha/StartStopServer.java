/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simpleWServer.Yudha;

/**
 *
 * @author Lenovo
 */

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class StartStopServer {
    private ServerSocket serverSocket;
    private boolean serverRunning;
    private int port;
    private String webDirectory;
    private String logDirectory;
    private logHandler logHandler;
    
    public StartStopServer(int port, String webDirectory, String logDirectory) {
        this.port = port;
        this.webDirectory = webDirectory;
        this.logHandler = new logHandler(logDirectory);
    }
    
    public void startServer(JTextField portField, JTextField directoryField, JTextField directoryLogs, JButton startButton, JButton stopButton, JTextArea logArea) {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            logHandler.log(logArea, "Invalid port number");
            return;
        }
        String webDirectory = directoryField.getText();
        String logDirectory = directoryLogs.getText();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    serverSocket = new ServerSocket(port, 0, InetAddress.getByName("0.0.0.0"));
                    serverRunning = true;
                    logHandler.log(logArea, "Server started on port " + port);
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    while (serverRunning) {
                        Socket clientSocket = serverSocket.accept();
                        HttpRequestHandler requestHandler = new HttpRequestHandler(webDirectory, logDirectory, port, logArea);
                        requestHandler.handleRequest(clientSocket, logArea);
                    }
                } catch (IOException e) {
                    logHandler.log(logArea, "Error starting server: " + e.getMessage());
                }
                return null;
            }
        };

        worker.execute();
    }

    public void stopServer(JButton startButton, JButton stopButton, JTextArea logArea) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                serverRunning = false;
                logHandler.log(logArea, "Server stopped");
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            } catch (IOException e) {
                logHandler.log(logArea, "Error stopping server: " + e.getMessage());
            }
        } else {
        logHandler.log(logArea, "Server is already stopped");
        }
    }
}
