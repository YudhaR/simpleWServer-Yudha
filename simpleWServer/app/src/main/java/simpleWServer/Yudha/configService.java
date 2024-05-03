/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simpleWServer.Yudha;

/**
 *
 * @author Lenovo
 */

import java.io.*;

public class configService {
    public static String[] loadConfig() {
        String[] configArray = new String[3]; // Array to store configuration values
        try (BufferedReader br = new BufferedReader(new FileReader("config\\config.txt"))) {
            String line;
            int index = 0; // Index for storing values in configArray
            File configFile = new File("config\\config.txt");
            if (configFile.length() == 0) {
                createConfig();
            }
            while ((line = br.readLine()) != null && index < 3) {
                String value = line.trim();
                if (!value.isEmpty()) {
                    configArray[index++] = value;
                }
            }
        } catch (IOException e) {
            System.err.println("Error while loading configuration: " + e.getMessage());
        }
        return configArray;
    }

    // Method to save configuration to a text file
    public static void saveConfig(int port, String webDirectory, String logDirectory) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("config\\config.txt"))) {
            bw.write(Integer.toString(port));
            bw.newLine();
            bw.write(webDirectory);
            bw.newLine();
            bw.write(logDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
    public static void createConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("config\\config.txt"))) {
            bw.write("8080");
            bw.newLine();
            bw.write("D:\\Web\\Files");
            bw.newLine();
            bw.write("logs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
    public static void dirConfig() {
        String confDirectory = "config";
        File confDir = new File(confDirectory);
        if (!confDir.exists()) {
            if (confDir.mkdirs()) {
                System.out.println("Configuration directory created: " + confDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create configuration directory.");
                return;
            }
        }

        File configFile = new File(confDir, "config.txt");
        if (!configFile.exists()) {
              createConfig();
//            try (BufferedWriter bw = new BufferedWriter(new FileWriter(configFile))) {
//                // Default configuration
//                bw.write("8080");
//                bw.newLine();
//                bw.write("D:\\Web\\Files");
//                bw.newLine();
//                bw.write("D:\\Project\\tugasPBO\\simpleWServer-Yudha\\app\\logs");
//            } catch (IOException e) {
//                System.err.println("Error while creating default configuration file: " + e.getMessage());
//            }
        }
    } 
}
