package assemblyline;

import java.net.InetSocketAddress;
import java.util.Arrays;

import assemblyline.client.utils.Comms;
import assemblyline.commands.Command;
import assemblyline.utils.ErrorMessages;
import assemblyline.utils.IO;

import org.json.JSONObject;

/**
* The client part of AssemblyLine
 * By default tries to connect to localhost:5400
*
* @author  Dimitri Sukhankin
* @since   2022-02-14 
*/
public class Client {
    public static void main(String[] args) {
        Command.client = true;
        //=============== Initialization ===============
        String[] userInput;

        //=============== Initial message ===============
        IO.print("Assemblyline v2.0 CLIENT%nUse 'help' command to see list of commands.%n%n");
        try {
            String hostname = "localhost";
            int port = 5400;
            if (args.length > 0) {
                hostname = args[0];
            }
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
            Comms.serverAddress = new InetSocketAddress(hostname, port);
        } catch(Exception e) {
            IO.print(e.getMessage());
            //if we can't connect to the server we might as well just give up 
            return;
        }
        IO.print("Server: %s:%d%n", Comms.serverAddress.getHostString(), Comms.serverAddress.getPort());
        //=============== Handling user input ===============
        while (true) {
            IO.print("> ");
            userInput = IO.nextLine().split(" ");

            //All commands are lower case, and I don't want people to suffer from not knowing it
            userInput[0] = userInput[0].toLowerCase();

            try {
                JSONObject output = null;
                if (userInput.length > 1) {
                    output = Command.requestCommand(userInput[0], Arrays.copyOfRange(userInput, 1, userInput.length));
                } else {
                    output = Command.requestCommand(userInput[0]);
                }
                //Some command (like exit) don't require any communication with the server
                if (output != null) {
                    JSONObject response = Comms.sendAndReceive(output.toString());
                    //probably poorly handled error
                    if (response == null) {
                        continue;
                    }
                    //definitively well handled error
                    if (response.has("error")) {
                        IO.print("%s%n", response.getString("error"));
                    }
                    Command.reactCommand(response);
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