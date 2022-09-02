package assemblyline.commands;

import java.util.Arrays;
import java.util.Comparator;

import assemblyline.vehicles.*;
import assemblyline.VehicleCollection;

import assemblyline.utils.IO;
import assemblyline.utils.ErrorMessages;

import org.json.JSONObject;

/**
 * Prints specified field in specified order
 */
public class PrintFieldSortedCommand extends Command {
    /**
     * Fieldname by which vehicles should be sorted
     */
    private final String fieldName;
    /**
     * Whether we should sort in ascending or descending order
     */
    private final boolean ascending;

    /**
     * Constructor
     * @param fieldName Fieldname by which vehicles should be sorted
     * @param ascending Whether we should sort in ascending or descending order
     */
    public PrintFieldSortedCommand(String fieldName, boolean ascending) {
        this.fieldName = fieldName;
        this.ascending = ascending;
    }

    private String getCommandName() {
        String template = "print_field_%s_%s";
        String ascdes;
        if (ascending) {
            ascdes = "ascending";
        } else {
            ascdes = "descending";
        }
        String fName = fieldName.toLowerCase().replace(" ", "_");
        return String.format(template, ascdes, fName);
    }

    public JSONObject request(String[] args) {
        return new JSONObject().put("command", getCommandName());
    }

    public JSONObject respond(JSONObject args, boolean trusted) {
        return new JSONObject().put("command", getCommandName()).put("data", VehicleCollection.toJSON());
    }

    public void react(JSONObject args) {
        VehicleCollection.fromJSON(args.getJSONObject("data"));
        if (VehicleCollection.vehicleCollection.isEmpty()) {
            IO.print("%s%n", ErrorMessages.COLLECTION_IS_EMPTY);
            return;
        }
        Vehicle[] vehicles = VehicleCollection.toArray();
        //Sort the array
        Arrays.sort(vehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle v1, Vehicle v2) {
                int mod = 1;
                if (ascending) mod = -1;
                switch(fieldName) {
                    case "Fuel type":
                        return v1.getFuelType().compareTo(v2.getFuelType()) * mod;
                    case "Engine power":
                        return (v1.getEnginePower() - v2.getEnginePower()) * mod;
                    case "Number of wheels":
                        return (v1.getNumberOfWheels() - v2.getNumberOfWheels()) * mod;
                }
                return 0;
            }
        });
        //Print it!
        for (int i = 0; i < vehicles.length; i++) {
            IO.print("[%s #%d] %s:", vehicles[i].getVehicleType(), vehicles[i].getId(), fieldName);
            switch(fieldName) {
                case "Fuel type":
                    IO.print("%s%n", vehicles[i].getFuelType());
                    break;
                case "Engine power":
                    IO.print("%d%n", vehicles[i].getEnginePower());
                    break;
                case "Number of wheels":
                    IO.print("%d%n", vehicles[i].getNumberOfWheels());
                    break;
            }
        }
    }

    public String getHelp() {
        return String.format("Prints fuel types of every vehicle in ascending order.%n%nUsage: print_field_ascending_fuel_type");
    }
}