package assemblyline;

import java.io.Console;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        //=============== Initialization ===============
        String[] userInput;

        //=============== Initial message ===============
        IO.print("Assemblyline v2.0 CLIENT%nUse 'help' command to see list of commands.%n%n");
        SocketChannel socketChannel;
        InetSocketAddress serverAddress;
        try {
            serverAddress = new InetSocketAddress("localhost", 80);
        } catch(Exception e) {
            IO.print(e.getMessage());
            //if we can't connect to the server we might as well just give up 
            return;
        }
        IO.print("Server: %s:%d%n", serverAddress.getHostString(), serverAddress.getPort());
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
                    //Request
                    socketChannel = SocketChannel.open();
                    //socketChannel.configureBlocking(false);
                    socketChannel.connect(serverAddress);

                    String text = output.toString() + "\n";
                    byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);

                    while(buffer.hasRemaining()) {
                        socketChannel.write(buffer);
                    }
                    
                    //Response
                    //This is ridicilous
                    //I couldn't easily convert bytebuffer to string, so I did this hell
                    buffer = ByteBuffer.allocate(2048);
                    int bytesRead = socketChannel.read(buffer);
                    List<Byte> noNulls = new ArrayList<>();
                    for (int i = 0; i < buffer.capacity(); i++) {
                        if (buffer.get(i) == 0) {
                            break;
                        }
                        noNulls.add(buffer.get(i));
                    }
                    bytes = new byte[noNulls.size()];
                    for (int i = 0; i < noNulls.size(); i++) {
                        bytes[i] = noNulls.get(i);
                    }
                    socketChannel.close();
                    //React
                    String rawResponse = new String(bytes, "UTF-8");
                    try {
                        JSONObject response = new JSONObject(rawResponse);
                        Command.reactCommand(response);
                    } catch(Exception e) {
                        IO.print(rawResponse);
                    }
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