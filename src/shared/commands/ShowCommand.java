package assemblyline.commands;

import java.util.Enumeration;

import assemblyline.VehicleCollection;
import assemblyline.utils.IO;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Shows all elements of the collection
 */
public class ShowCommand extends Command {
    public JSONObject request(String[] args) {
        return new JSONObject().put("command", "show");
    }

    public JSONObject respond(JSONObject args) {
        Enumeration keys = VehicleCollection.vehicleCollection.keys();
        JSONObject data = new JSONObject();
        while (keys.hasMoreElements()) {
            int k = (int)keys.nextElement();
            data.put(Integer.toString(k), String.format("[%d] %s%n%n", k, VehicleCollection.vehicleCollection.get(k).toString()));
        }
        return new JSONObject().put("command", "show").put("data", data);
    }

    public void react(JSONObject args) {
        JSONObject data = args.getJSONObject("data");
        JSONArray names = data.names();
        if (names == null) {
            IO.print("List is empty%n");
        } else {
            IO.print("List of vehicles:%n");
            for (int i = 0; i < names.length(); i++) {
                IO.print("%s%n", data.getString(names.getString(i)));
            }
        }
    }

    public String getHelp() {
        return String.format("Prints list of vehicles.%n%nUsage: show");
    }
}