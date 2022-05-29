package assemblyline.commands;

import java.util.Enumeration;

import assemblyline.VehicleCollection;
import assemblyline.utils.IO;
import assemblyline.vehicles.Vehicle;

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
            data.put(Integer.toString(k), VehicleCollection.vehicleCollection.get(k).toJSON());
        }
        return new JSONObject().put("command", "show").put("data", data);
    }

    public void react(JSONObject args) {
        JSONObject data = args.getJSONObject("data");
        JSONArray names = data.names();
        //String.format("[%d] %s%n%n", k, VehicleCollection.vehicleCollection.get(k).toString())
        if (names == null) {
            IO.print("List is empty%n");
        } else {
            IO.print("List of vehicles:%n");
            for (int i = 0; i < names.length(); i++) {
                String k = names.getString(i);
                Vehicle vehicle = Vehicle.fromJSON(data.getJSONObject(k), false);
                IO.print("[%s] %s%n%n", k, vehicle.toString());
            }
        }
    }

    public String getHelp() {
        return String.format("Prints list of vehicles.%n%nUsage: show");
    }
}