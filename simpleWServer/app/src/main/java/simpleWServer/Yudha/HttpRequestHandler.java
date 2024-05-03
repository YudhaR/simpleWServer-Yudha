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
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

public class HttpRequestHandler {
    private String webDirectory;
    private String logDirectory;
    private int port;
    private logHandler logHandler;
    
    
    public HttpRequestHandler(String webDirectory, String logDirectory, int port, JTextArea logArea) {
        this.webDirectory = webDirectory;
        this.logHandler = new logHandler(logDirectory);
        this.port = port;
    }
    
    public void handleRequest(Socket clientSocket, JTextArea logArea) {
        String path = "";
        String parentPath  = "";
        String filePath = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();

            String request = in.readLine();
            if (request != null && request.startsWith("GET")) {
                String[] parts = request.split(" ");
                String encodedUrl = parts[1];
                String url = URLDecoder.decode(encodedUrl, "UTF-8");
                filePath = webDirectory + url;

                File file = new File(filePath);
                if (file.exists()) {
                    if (file.isDirectory()) {
                        File indexFile = new File(file, "index.html");
                        if (indexFile.exists()) {
                            // Kirimkan konten dari index.html
                            FileInputStream fis = new FileInputStream(indexFile);
                            byte[] data = new byte[(int) indexFile.length()];
                            fis.read(data);
                            fis.close();
                            

                            out.println("HTTP/1.1 302 Found");
                            out.println("Location: http://localhost:" +  port + url + "/index.html");
                            out.println();
                            clientSocket.getOutputStream().write(data, 0, data.length);
                            clientSocket.getOutputStream().flush();
                        }
                        String[] files = file.list();
                        StringBuilder response = new StringBuilder();
                        response.append("<html><body><h1>Directory Listing:</h1><ul>");

                        if (!url.equals("/")) {
                            int lastIndex = url.lastIndexOf("/");
                            parentPath = url.substring(0, lastIndex);
                            if (parentPath.isEmpty()) {
                                parentPath = "/";
                            }
                            response.append("<li><a href=\"http://localhost").append(":").append(port).append(parentPath).append("\">...Back</a></li>");
                        }

                        for (String filename : files) {
                            path = url.equals("/") ? url + filename : url + "/" + filename;
                            response.append("<li><a href=\"http://localhost:").append(port).append(path).append("\">").append(filename).append("</a></li>");
                        }

                        response.append("</ul></body></html>");
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: text/html");
                        out.println("Content-Length: " + response.length());
                        out.println();
                        out.println(response);
                    } else {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] data = new byte[(int) file.length()];
                        fis.read(data);
                        fis.close();
                        filePath = filePath.replace("\\", "/");

                        String contentType = getContentType(filePath);

                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: " + contentType);
                        out.println("Content-Length: " + data.length);
                        out.println();
                        clientSocket.getOutputStream().write(data, 0, data.length);
                        clientSocket.getOutputStream().flush();
                    }
                    String logMessage = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] ";
                    logMessage += clientSocket.getInetAddress().getHostAddress() + " GET " + filePath  + "\">";
                    logHandler.log(logArea, logMessage);
                } else {
                    out.println("HTTP/1.1 404 Not Found");
                    out.println("Content-Type: text/html");
                    out.println();
                    out.println("<h1>404 Not Found</h1>");
                    
                    String logMessage = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] ";
                    logMessage += "404 Not Found: " + clientSocket.getInetAddress().getHostAddress() + " requested: " + filePath;
                    logHandler.log(logArea, logMessage);
                }
            }



            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            logHandler.log(logArea, "Error handling request: " + e.getMessage());
        }
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
            return "text/html";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else if (filePath.endsWith(".pdf")) {
            return "application/pdf";
        } else {
            return "application/octet-stream";
        }
    }
    
}
