package assemblyline.server.utils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import assemblyline.commands.Command;
import assemblyline.utils.ErrorMessages;
import assemblyline.utils.IO;

import org.json.JSONObject;

public class Comms extends Thread {

    public void run() {
        JSONObject userInput;
        // =============== Starting up the server ===============
        ServerSocket serverSocket;
        try {
            /**
             * Port on which server should be initilialized.
             */
            int port = 5400;
            serverSocket = new ServerSocket(port);
        } catch (Exception exception) {
            IO.print(ErrorMessages.TEMPLATE, exception.getMessage());
            return;
        }

        // =============== Initial message ===============
        try {
            IO.print("Assemblyline v2.0 SERVER%nListening to port %d%n%s%n%n", serverSocket.getLocalPort(), InetAddress.getLocalHost().toString());
        } catch(Exception e) {
            return;
        }
        // =============== Handling user input ===============
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
                userInput = new JSONObject(reader.readLine());
                IO.print("User requested command %s%n", userInput.getString("command"));
                try {
                    JSONObject output;
                    output = Command.respondCommand(userInput, false);
                    if (output != null) {
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

                        writer.append(output.toString());

                        writer.close();
                        IO.print("Responded to user%n");
                    }
                } catch (Exception e) {
                    BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

                    String errorText;

                    if (e.getMessage() == null) {
                        errorText = String.format(ErrorMessages.TEMPLATE, e.getClass());
                    } else {
                        errorText = String.format(ErrorMessages.TEMPLATE, e.getMessage());
                    }

                    writer.append(errorText);

                    writer.close();
                    IO.print(errorText);
                }
            } catch (Exception e) {
                if (e.getMessage() == null) {
                    IO.print(ErrorMessages.TEMPLATE, e.getClass());
                } else {
                    IO.print(ErrorMessages.TEMPLATE, e.getMessage());
                }
            }
        }
    }
}