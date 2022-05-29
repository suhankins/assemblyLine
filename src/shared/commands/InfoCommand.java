package assemblyline.commands;

import java.time.LocalDate;

import assemblyline.VehicleCollection;
import assemblyline.utils.IO;

import org.json.JSONObject;

/**
 * prints info about the collection
 */
public class InfoCommand extends Command {
    public JSONObject request(String[] args) {
        return new JSONObject().put("command", "info");
    }

    public JSONObject respond(JSONObject args) {
        if (VehicleCollection.isEmpty()) {
            return new JSONObject().put("command", "info");
        } else {
            return new JSONObject().put("command", "info")
                .put("size", VehicleCollection.vehicleCollection.size())
                .put("initializationDate", VehicleCollection.initializationDate.toString());
        }
    }

    public void react(JSONObject args) {
        if (args.isNull("initializationDate")) {
            IO.print("Collection was never initialized.%n");
            return;
        }
        int size = args.getInt("size");
        LocalDate initializationDate = LocalDate.parse(args.getString("initializationDate"));
        IO.print("Size of the collection: %d%n", size);
        IO.print("Initialization date: %s%n", initializationDate.toString());
    }

    public String getHelp() {
        return String.format("Prints information about current vehicle collection.%n%nUsage: info");
    }
}