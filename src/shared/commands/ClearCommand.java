package assemblyline.commands;

import assemblyline.VehicleCollection;

import assemblyline.utils.IO;

import org.json.JSONObject;

public class ClearCommand extends Command {
    public JSONObject request(String[] args) {
        IO.print("Asking server to clear the collection...%n");
        JSONObject output = new JSONObject().put("command", "clear");
        return output;
    }

    public JSONObject respond(JSONObject args) {
        VehicleCollection.vehicleCollection.clear();
        IO.print("Collection cleared.%n");
        JSONObject output = new JSONObject().put("command", "clear");
        return output;
    }

    public void react(JSONObject args) {
        IO.print("Collection cleared.%n");
    }

    public String getHelp() {
        return String.format("Clears vehicle collection.%n%nUsage: clear");
    }
}