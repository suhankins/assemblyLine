package assemblyline.commands;

import java.util.Hashtable;

import assemblyline.vehicles.*;
import assemblyline.VehicleCollection;

import assemblyline.utils.IO;
import assemblyline.utils.InputArguments;

import org.json.JSONObject;

/**
 * Inserts a new vehicle into vehicleCollection
 */
public class InsertCommand extends Command {
    public JSONObject request(String[] args) {
        isArgumentGiven(args);

        int key = Integer.parseInt(args[0]);

        IO.print("Creating a new vehicle with the '%d' key:%n", key);
        
        JSONObject listOfParams = InputArguments.inputArguments(true);

        JSONObject data = new JSONObject().put("key", key);

        Vehicle vehicle = new Vehicle(listOfParams.getString("name"),
            new Coordinates(listOfParams.getDouble("x"), listOfParams.getLong("y")),
            listOfParams.getInt("enginePower"),
            listOfParams.getInt("numberOfWheels"),
            VehicleType.valueOf(listOfParams.getString("vehicleType")),
            FuelType.valueOf(listOfParams.getString("fuelType")));
        
        data = data.put("vehicle", vehicle.toJSON());

        JSONObject toSend = new JSONObject().put("data", data).put("command", "insert");

        IO.print("Sending new vehicle to server...%n");
        return toSend;
    }

    public JSONObject respond(JSONObject args) {
        JSONObject data = args.getJSONObject("data");
        VehicleCollection.vehicleCollection.put(data.getInt("key"),
        Vehicle.fromJSON(data.getJSONObject("vehicle"), false));
        //Setting initialization data since this is probably the first car in collection
        if (VehicleCollection.initializationDate == null)
            VehicleCollection.initializationDate = java.time.LocalDate.now();
        IO.print("Added new vehicle with key %d%n", data.getInt("key"));
        return new JSONObject().put("command", "insert");
    }

    public void react(JSONObject args) {
        IO.print("New vehicle successfully added.%n");
    }

    @Override
    public String getHelp() {
        return String.format("Insert a new vehicle with a specified key.%n%nUsage: insert [key]");
    }
}