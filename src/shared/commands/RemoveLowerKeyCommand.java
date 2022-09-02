package assemblyline.commands;

import java.util.Enumeration;

import assemblyline.VehicleCollection;

import assemblyline.utils.ErrorMessages;
import assemblyline.utils.IO;

import org.json.JSONArray;
import org.json.JSONObject;

public class RemoveLowerKeyCommand extends Command {
    public JSONObject request(String[] args) {
        isArgumentGiven(args);
        return new JSONObject().put("command", "remove_lower_key").put("data", args[0]);
    }
    
    public JSONObject respond(JSONObject args, boolean trusted) {
        if (VehicleCollection.isEmpty()) {
            IO.print("%s%n", ErrorMessages.COLLECTION_IS_EMPTY);
            return new JSONObject().put("error", ErrorMessages.COLLECTION_IS_EMPTY);
        }

        int key = args.getInt("data");

        JSONArray removed = new JSONArray();

        Enumeration keys = VehicleCollection.vehicleCollection.keys();
        IO.print("Removing keys lower than %d:%n", key);
        while (keys.hasMoreElements()) {
            int k = (int)keys.nextElement();
            if (k < key) {
                VehicleCollection.vehicleCollection.remove(k);
                IO.print("Key %d was removed%n", k);
                removed.put(k);
            }
        }
        IO.print("Every key lower than %d has been successfully removed.%n", key);
        return new JSONObject().put("command", "remove_lower_key").put("data", removed);
    }

    public void react(JSONObject args) {
        JSONArray removed = args.getJSONArray("data");
        for (int i = 0; i < removed.length(); i++) {
            IO.print("Key %d was removed%n", removed.getInt(i));
        }
    }

    public String getHelp() {
        return String.format("Replace fields of a car stored at a given key if new values are lower.%n%nUsage: replace_if_lower [key]");
    }
}