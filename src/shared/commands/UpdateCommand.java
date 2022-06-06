package assemblyline.commands;

import assemblyline.vehicles.Vehicle;
import assemblyline.VehicleCollection;

import assemblyline.client.utils.Comms;

import assemblyline.utils.InputArguments;
import assemblyline.utils.IO;

import org.json.JSONObject;

/**
 * Updates information about vehicle using inputArguments
 */
public class UpdateCommand extends Command {
    public JSONObject request(String[] args) {
        isArgumentGiven(args);
        int id = Integer.parseInt(args[0]);
        Boolean exists = Comms.sendAndReceive(new JSONObject().put("command", "exists").put("id", id).toString()).getBoolean("data");
        
        if (!exists) {
            throw new NullPointerException(String.format("Vehicle with ID %s does not exist.", args[0]));
        }
        JSONObject jsonRequest = InputArguments.inputArguments(false, false)
        .put("command", "update").put("id", id);
        return jsonRequest;
    }

    public JSONObject respond(JSONObject args) {
        Vehicle vehicle = VehicleCollection.getById(args.getInt("id"));
        if (vehicle == null) {
            throw new NullPointerException(String.format("Vehicle with ID %s does not exist.", args.getInt("id")));
        }

        vehicle.updateData(args, false);

        return new JSONObject().put("command", "update");
    }

    public void react(JSONObject args) {
        IO.print("Vehicle updated.%n");
    }

    public String getHelp() {
        return String.format("Update vehicle's parameters with new data.%nYou can skip parameters you don't want to update by inputting an empty string.%n%nUsage: update [id]%n");
    }
}