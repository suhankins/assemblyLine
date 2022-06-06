package assemblyline.commands;

import assemblyline.vehicles.Vehicle;
import assemblyline.VehicleCollection;

import assemblyline.client.utils.Comms;

import assemblyline.utils.InputArguments;
import assemblyline.utils.IO;

import org.json.JSONObject;

public class ReplaceIfLowerCommand extends Command {
    public JSONObject request(String[] args) {
        isArgumentGiven(args);

        int key = Integer.parseInt(args[0]);
        Boolean exists = Comms.sendAndReceive(new JSONObject().put("command", "exists_key").put("key", key).toString()).getBoolean("data");
        
        if (!exists) {
            throw new NullPointerException(String.format("Vehicle with key %s does not exist.", args[0]));
        }

        JSONObject jsonRequest = InputArguments.inputArguments(false, true)
        .put("command", "replace_if_lower").put("key", key);
        return jsonRequest;
    }

    public JSONObject respond(JSONObject args) {
        Vehicle vehicle = VehicleCollection.vehicleCollection.get(args.getInt("key"));
        if (vehicle == null) {
            throw new NullPointerException(String.format("Vehicle with key %d does not exist.", args.getInt("key")));
        }

        vehicle.updateData(args, true);

        return new JSONObject().put("command", "replace_if_lower");
    }

    public void react(JSONObject args) {
        IO.print("Fields have been seccessfully replaced%n");
    }

    public String getHelp() {
        return String.format("Replace fields of a car stored at a given key if new values are lower.%n%nUsage: replace_if_lower [key]");
    }
}