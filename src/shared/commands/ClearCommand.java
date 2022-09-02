package assemblyline.commands;

import assemblyline.VehicleCollection;

import assemblyline.utils.IO;

import org.json.JSONObject;

public class ClearCommand extends Command {
    public JSONObject request(String[] args) {
        IO.print("Asking server to clear the collection...%n");
        return new JSONObject().put("command", "clear");
    }

    public JSONObject respond(JSONObject args) {
        VehicleCollection.vehicleCollection.clear();
        IO.print("Collection cleared.%n");
        return new JSONObject().put("command", "clear");
    }

    public void react(JSONObject args) {
        IO.print("Collection cleared.%n");
    }

    public String getHelp() {
        return String.format("Clears vehicle collection.%n%nUsage: clear");
    }
}