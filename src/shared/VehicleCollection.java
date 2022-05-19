package assemblyline;

import java.util.Hashtable;
import java.util.Enumeration;
import java.time.LocalDate;

import assemblyline.vehicles.Vehicle;

import org.json.JSONObject;

/**
 * Class for storing all created vehicles
 */
public class VehicleCollection {
    /**
     * Hashtable where vehicles are stored.
     */
    public static Hashtable<Integer, Vehicle> vehicleCollection = new Hashtable<Integer, Vehicle>();
    /**
     * Vehicle collection initialization date
     * If null - vehicle collection was never initialized.
     */
    public static LocalDate initializationDate = null;

    /**
     * Get vehicle from vehicle collection by its id
     * @param id
     * @return vehicle with corresponding id
     */
    public static Vehicle getById(int id) {
        Enumeration keys = vehicleCollection.keys();

        while (keys.hasMoreElements()) {
            int k = (int)keys.nextElement();
            if (vehicleCollection.get(k).getId() == id) {
                return vehicleCollection.get(k);
            }
        }
        
        return null;
    }

    /**
     * Checks whether collection is empty or not
     * @return true if collection is empty, false if it's not
     */
    public static boolean isEmpty() {
        return vehicleCollection.size() == 0;
    }

    /**
     * Return an array with every vehicle. Keys get lost, sadly.
     * @return Array of vehicles in the collection
     */
    public static Vehicle[] toArray() {
        //This is ridiculous
        Vehicle[] vehicles = new Vehicle[VehicleCollection.vehicleCollection.size()];
        Enumeration keys = VehicleCollection.vehicleCollection.keys();
        //Here we put stuff from hashtable into an array
        for (int i = 0; i < vehicles.length; i++) {
            int k = (int)keys.nextElement();
            vehicles[i] = VehicleCollection.vehicleCollection.get(k);
        }

        return vehicles;
    }

    /**
     * Converts the whole collection to JSON
     * All vehicles are stored in vehicles
     * @return collection converted to JSON
     */
    public static JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("initializationDate", initializationDate.toString());
        JSONObject vehicles = new JSONObject();
        Enumeration keys = vehicleCollection.keys();
        while (keys.hasMoreElements()) {
            int k = (int)keys.nextElement();
            vehicles.put(Integer.toString(k), vehicleCollection.get(k).toJSON());
        }
        json.put("vehicles", vehicles);
        return json;
    }

    public static void fromJSON(JSONObject json) {
        initializationDate = LocalDate.parse(json.getString("initializationDate"));
        JSONObject vehicles = json.getJSONObject("vehicles");
        String[] names = JSONObject.getNames(vehicles);
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            vehicleCollection.put(
                Integer.parseInt(name), 
                Vehicle.fromJSON(vehicles.getJSONObject(name))
            );
        }
    }
}