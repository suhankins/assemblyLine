package assemblyline;

import java.util.Arrays;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import assemblyline.commands.Command;
import assemblyline.utils.ErrorMessages;
import assemblyline.utils.FeatureNotImplementedException;
import assemblyline.utils.FileManager;
import assemblyline.utils.IO;

import org.json.JSONObject;

/**
* The server part of AssemblyLine
*
* @author  Dimitri Sukhankin
* @since   2022-02-14 
*/
public class Server {
    /**
     * Port on which server should be initilialized. 
     */
    private static int port = 80;
    public static void main(String[] args) {
        //=============== Initialization ===============
        JSONObject userInput;
        //=============== Save file loading routine ===============
        try {
            if (args.length > 0) { 
                FileManager.loadSave(args[0]);
            }
        } catch (Exception exception) {
            IO.print(ErrorMessages.TEMPLATE, exception.getMessage());
            //If there was some error with loading the collection, we better get rid of what we already loaded
            VehicleCollection.vehicleCollection.clear();
            VehicleCollection.initializationDate = null;
        }

        //=============== Starting up the server ===============
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception exception) {
            IO.print(ErrorMessages.TEMPLATE, exception.getMessage());
            return;
        }

        //=============== Initial message ===============
        IO.print("Assemblyline v2.0 SERVER%nListening to port %d%n%n", serverSocket.getLocalPort());

        //=============== Handling user input ===============
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()
                    )
                );
                userInput = new JSONObject(reader.readLine());
                IO.print("User requested command %s%n", userInput.getString("command"));
                JSONObject output;
                output = Command.respondCommand(userInput);
                if (output != null) {
                    BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), "UTF-8")
                    );

                    writer.append(output.toString());
                    
                    writer.close();
                    IO.print("Reponded to user%n");
                }
            } catch(java.util.InputMismatchException e) {
                IO.print("ERROR: Wrong type of data was inputted%n");
            } catch(Exception e) {
                //Apparently that can happen /shrug
                if (e.getMessage() == null) {
                    IO.print(ErrorMessages.TEMPLATE, e.getClass());
                } else {
                    IO.print(ErrorMessages.TEMPLATE, e.getMessage());
                }
            }
        }
    }
}