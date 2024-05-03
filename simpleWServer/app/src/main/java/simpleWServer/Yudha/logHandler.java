/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simpleWServer.Yudha;

/**
 *
 * @author Lenovo
 */
import javax.swing.JTextArea;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class logHandler {
    private String logDirectory;

    public logHandler(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    public void log(JTextArea logArea, String message) {
        logArea.append(message + "\n");
        scrollToBottom(logArea);
        String logsDirectory = logDirectory;
        try {
            File logDir = new File(logsDirectory);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            String logFileName = "access_log_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt";
            String logFilePath = logDir.getAbsolutePath() + File.separator + logFileName;
            FileWriter fileWriter = new FileWriter(logFilePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(message + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Error writing log: " + e.getMessage());
        }
    }

    private void scrollToBottom(JTextArea logArea) {
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }   
}
