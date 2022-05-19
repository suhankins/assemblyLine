package assemblyline;

import java.util.Arrays;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import assemblyline.commands.Command;
import assemblyline.utils.ErrorMessages;
import assemblyline.utils.FeatureNotImplementedException;
import assemblyline.utils.FileManager;
import assemblyline.utils.IO;

/**
* The server part of AssemblyLine
*
* @author  Dimitri Sukhankin
* @since   2022-02-14 
*/
public class Server {
    public static void main(String[] args) {
        //=============== Initialization ===============
        String[] userInput;
        //=============== Save file loading routine ===============
        try {
            if (args.length > 0) { 
                FileManager.loadSave(args[0]);
            }
        } catch (FeatureNotImplementedException exception) {
            IO.print("File loading routine is still not implemented! Implement it already, you idiot!%n");
        } catch (Exception exception) {
            IO.print(ErrorMessages.TEMPLATE, exception.getMessage());
            //If there was some error with loading the collection, we better get rid of what we already loaded
            VehicleCollection.vehicleCollection.clear();
            VehicleCollection.initializationDate = null;
        }

        //=============== Starting up the server ===============
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(80);
        } catch (Exception exception) {
            IO.print(ErrorMessages.TEMPLATE, exception.getMessage());
            return;
        }

        //=============== Initial message ===============
        IO.print("Assemblyline v2.0%nListening to port %d%n%n", serverSocket.getLocalPort());

        //=============== Handling user input ===============
        while (true) {
            try {
                /*
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()
                    )
                );
                System.out.print(reader.readLine());
                */
                userInput = IO.nextLine().split(" ");
                //All commands are lower case, and I don't want people to suffer from not knowing it
                userInput[0] = userInput[0].toLowerCase();
                if (userInput.length > 1) {
                    Command.executeCommand(userInput[0], Arrays.copyOfRange(userInput, 1, userInput.length));
                } else {
                    Command.executeCommand(userInput[0]);
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