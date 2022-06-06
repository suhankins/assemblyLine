package assemblyline.commands;

import assemblyline.VehicleCollection;

import assemblyline.utils.IO;

import org.json.JSONObject;

public class RemoveKeyCommand extends Command {
    public JSONObject request(String[] args) {
        isArgumentGiven(args);
        IO.print("Deleting %s key %n", args[0]);
        return new JSONObject().put("command", "remove_key").put("data", args[0]);
    }

    public JSONObject respond(JSONObject args) {
        int key = args.getInt("data");

        if (!VehicleCollection.vehicleCollection.containsKey(key)) {
            throw new NullPointerException(String.format("%d key doesn't exist", key));
        }

        VehicleCollection.vehicleCollection.remove(key);

        IO.print("%d key was removed.%n", key);

        return new JSONObject().put("command", "remove_key");
    }

    public void react(JSONObject args) {
        IO.print("Key was successfully removed%n");
    }

    public String getHelp() {
        return String.format("Removes a vehicle with the specified key.%n%nUsage: remove_key [key]");
    }
}