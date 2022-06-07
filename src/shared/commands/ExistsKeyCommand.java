package assemblyline.commands;

import java.util.Enumeration;
import java.util.Hashtable;

import assemblyline.VehicleCollection;

import org.json.JSONObject;

public class ExistsKeyCommand extends Command {

    {
        hidden = true;
    }

    public JSONObject request(String[] args) {
        return null;
    }

    public JSONObject respond(JSONObject args) {
        Boolean exists = VehicleCollection.vehicleCollection.containsKey(args.getInt("key"));
        return new JSONObject().put("data", exists).put("command", "exists_key");
    }

    public void react(JSONObject args) {
    }

    public String getHelp() {
        return String.format("");
    }
}