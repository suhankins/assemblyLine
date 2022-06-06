package assemblyline;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import assemblyline.client.utils.Comms;
import assemblyline.commands.Command;
import assemblyline.utils.ErrorMessages;
import assemblyline.utils.FeatureNotImplementedException;
import assemblyline.utils.FileManager;
import assemblyline.utils.IO;

import org.json.JSONObject;

/**
* The client part of AssemblyLine
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
            Comms.serverAddress = new InetSocketAddress("localhost", 80);
        } catch(Exception e) {
            IO.print(e.getMessage());
            //if we can't connect to the server we might as well just give up 
            return;
        }
        IO.print("Server: %s:%d%n", Comms.serverAddress.getHostString(), Comms.serverAddress.getPort());
        //=============== Handling user input ===============
        while (true) {
            System.out.print("> ");
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