package assemblyline;

import java.util.Arrays;

import assemblyline.server.utils.Comms;
import assemblyline.commands.Command;
import assemblyline.utils.ErrorMessages;
import assemblyline.utils.FileManager;
import assemblyline.utils.IO;

import org.json.JSONObject;

/**
 * The server part of AssemblyLine
 *
 * @author Dimitri Sukhankin
 * @since 2022-02-14
 */
public class Server {
    public static void main(String[] args) {
        // =============== Initialization ===============
        Command.client = false;
        String[] userInput;
        // =============== Save file loading routine ===============
        try {
            if (args.length > 0) {
                FileManager.loadSave(args[0]);
            }
        } catch (Exception exception) {
            IO.print(ErrorMessages.TEMPLATE, exception.getMessage());
            // If there was some error with loading the collection, we better get rid of
            // what we already loaded
            VehicleCollection.vehicleCollection.clear();
            VehicleCollection.initializationDate = null;
        }

        Comms p = new Comms();
        p.start();

        while (true) {
            userInput = IO.nextLine().split(" ");

            userInput[0] = userInput[0].toLowerCase();

            try {
                JSONObject output = null;
                if (userInput.length > 1) {
                    output = Command.requestCommand(userInput[0], Arrays.copyOfRange(userInput, 1, userInput.length));
                } else {
                    output = Command.requestCommand(userInput[0]);
                }
                if (output != null) {
                    output = Command.respondCommand(output, true);
                    if (output != null) {
                        Command.reactCommand(output);
                    }
                }
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