package assemblyline.commands;

import assemblyline.vehicles.Vehicle;
import assemblyline.VehicleCollection;

import org.json.JSONObject;

public class ExistsCommand extends Command {

    {
        hidden = true;
    }

    public JSONObject request(String[] args) {
        return null;
    }

    public JSONObject respond(JSONObject args, boolean trusted) {
        Vehicle vehicle = VehicleCollection.getById(args.getInt("id"));
        Boolean exists = (vehicle != null);
        return new JSONObject().put("data", exists).put("command", "exists");
    }

    public void react(JSONObject args) {
    }

    public String getHelp() {
        return String.format("");
    }
}