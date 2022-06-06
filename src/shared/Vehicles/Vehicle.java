package assemblyline.vehicles;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.time.LocalDate;

import assemblyline.VehicleCollection;
import assemblyline.utils.ValueOutOfRangeException;
import assemblyline.utils.NotEmptyException;
import assemblyline.utils.NotNullException;
import assemblyline.utils.IO;

import org.json.JSONObject;

public class Vehicle implements Comparable<Vehicle> {
    /**
     * Counts how many cars have already been created
     */
    private static int counter = 0;

    /**
     * <p> Unique vehicle id </p>
     * <p> * Generated automatically </p>
     * <p> * Cannot be null </p>
     * <p> * Must be more than 0 </p>
     */
    private Integer id;
    /**
     * <p> Vehicle's name </p>
     * <p> * Cannot be null </p>
     * <p> * Cannot be an empty string </p>
     */
    private String name;
    /**
     * <p> Vehicle coordinates </p>
     * <p> * Cannot be null </p>
     */
    private Coordinates coordinates;
    /**
     * <p> Vehicle creation date </p>
     * <p> * Generated automatically </p>
     * <p> * Cannot be null </p>
     */
    private java.time.LocalDate creationDate;
    /**
     * <p> Vehicle engine power </p>
     * <p> * Must be more than 0 </p>
     */
    private int enginePower;
    /**
     * <p> Amount of wheels a vehicle has </p>
     * <p> * Must be more than 0 </p>
     */
    private int numberOfWheels;
    /**
     * <p> Vehicle type </p>
     * <p> * Cannot be null </p>
     */
    private VehicleType type;
    /**
     * <p> Vehicle fuel type </p>
     * <p> * Cannot be null </p>
     */
    private FuelType fuelType;

    // =============== Access to variables methods ===============
    public Integer getId() {
        return this.id;
    }
    public Coordinates getCoordinates() {
        return this.coordinates;
    }
    public int getEnginePower() {
        return this.enginePower;
    }
    public int getNumberOfWheels() {
        return this.numberOfWheels;
    }
    public VehicleType getVehicleType() {
        return this.type;
    }
    public FuelType getFuelType() {
        return this.fuelType;
    }
    public String getName() {
        return this.name;
    }
    public java.time.LocalDate getCreationDate() {
        return this.creationDate;
    }
    // =============== Access to variables methods END ===============

    // =============== Check variable correctness ===============
    public static boolean isNameCorrect(String name) {
        if (name == null) return false;
        return !name.trim().equals("");
    }
    public static boolean isCoordinatesCorrect(Coordinates coordinates) {
        return coordinates != null;
    }
    public static boolean isEnginePowerCorrect(int enginePower) {
        return enginePower > 0;
    }
    public static boolean isNumberOfWheelsCorrect(int numberOfWheels) {
        return numberOfWheels > 0;
    }
    public static boolean isVehicleTypeCorrect(VehicleType vehicleType) {
        return vehicleType != null;
    }
    public static boolean isFuelTypeCorrect(FuelType fuelType) {
        return fuelType != null;
    }
    // =============== Check variable correctness END ===============

    // =============== Set variables ===============
    public void setName(String name) {
        if (!isNameCorrect(name)) {
            throw new NotEmptyException("Name");
        }
        this.name = name;
    }
    public void setCoordinates(Coordinates coordinates) {
        if (!isCoordinatesCorrect(coordinates)) {
            throw new NotNullException("Coordinates");
        }
        this.coordinates = coordinates;
    }
    public void setEnginePower(int enginePower) {
        if (!isEnginePowerCorrect(enginePower)) {
            throw new ValueOutOfRangeException(0, false, "Engine Power");
        }
        this.enginePower = enginePower;
    }
    public void setNumberOfWheels(int numberOfWheels) {
        if (!isNumberOfWheelsCorrect(numberOfWheels)) {
            throw new ValueOutOfRangeException(0, false, "Number of wheels");
        }
        this.numberOfWheels = numberOfWheels;
    }
    public void setVehicleType(VehicleType vehicleType) {
        if (!isVehicleTypeCorrect(vehicleType)) {
            throw new NotNullException("Vehicle type");
        }
        this.type = vehicleType;
    }
    public void setFuelType(FuelType fuelType) {
        if (!isFuelTypeCorrect(fuelType)) {
            throw new NotNullException("Fuel type");
        }
        this.fuelType = fuelType;
    }
    /**
     * Sets ID
     * This is only used for loading vehicles from JSON
     */
    public void setId(Integer id) {
        if (id <= 0) {
            throw new ValueOutOfRangeException();
        }
        this.id = id;
        if (counter < id) {
            counter = id;
        }
    }
    /**
     * Sets creation date
     * This is only used for loading vehicles from JSON
     */
    public void setCreationDate(java.time.LocalDate date) {
        this.creationDate = date;
    }
    // =============== Set variables END ===============

    /**
     * Asks for user input to update given vehicle's fields.
     * @param ifLower only update fields if given value is lower
     */
    public void updateData(JSONObject listOfParams, boolean ifLower) {
        Iterator<String> keys = listOfParams.keys();

        //I really should consider finding a better way to do this...
        while (keys.hasNext()) {
            String k = (String)keys.next();
            Object v = listOfParams.get(k);
            switch(k) {
                case "name":
                    this.setName((String)v);
                    IO.print("Name updated%n");
                    break;
                case "enginePower":
                    int enginePower = (int)v;
                    if (ifLower) {
                        if (this.getEnginePower() <= enginePower) {
                            break;
                        }
                    }
                    this.setEnginePower(enginePower);
                    IO.print("Engine power updated%n");
                    break;
                case "numberOfWheels":
                    int numberOfWheels = (int)v;
                    if (ifLower) {
                        if (this.getNumberOfWheels() <= numberOfWheels) {
                            break;
                        }
                    }
                    this.setNumberOfWheels(numberOfWheels);
                    IO.print("Number of wheels updated%n");
                    break;
                case "vehicleType":
                    this.setVehicleType((VehicleType)v);
                    IO.print("Vehicle type updated%n");
                    break;
                case "fuelType":
                    this.setFuelType((FuelType)v);
                    IO.print("Fuel type updated%n");
                    break;
                case "x":
                    double x = (double)v;
                    if (ifLower) {
                        if (coordinates.getX() <= x) {
                            break;
                        }
                    }
                    coordinates.setX(x);
                    IO.print("X updated%n");
                    break;
                case "y":
                    long y = (long)v;
                    if (ifLower) {
                        if (coordinates.getY() <= y) {
                            break;
                        }
                    }
                    coordinates.setY(y);
                    IO.print("Y updated%n");
                    break;
            }
        }
    }

    /**
     * Constructor.
     * @param name <p>* Cannot be null</p><p>* Cannot be an empty string</p>
     * @param coordinates <p>* Cannot be null</p>
     * @param enginePower <p>* Must be more than 0</p>
     * @param numberOfWheels <p>* Must be more than 0</p>
     * @param vehicleType <p>* Cannot be null</p>
     * @param fuelType <p>* Cannot be null</p>
     */
    public Vehicle(String name, Coordinates coordinates, int enginePower,
            int numberOfWheels, VehicleType vehicleType, FuelType fuelType) {
        if (!isNameCorrect(name)) throw new NotEmptyException("Name");
        if (!isCoordinatesCorrect(coordinates)) throw new NotNullException("Coordinates");
        if (!isEnginePowerCorrect(enginePower)) throw new ValueOutOfRangeException(1, false, "Engine Power");
        if (!isNumberOfWheelsCorrect(numberOfWheels)) throw new ValueOutOfRangeException(1, false, "Number of wheels");
        if (!isVehicleTypeCorrect(vehicleType)) throw new NotNullException("Vehicle type");
        if (!isFuelTypeCorrect(fuelType)) throw new NotNullException("Fuel type");

        counter += 1;

        this.id = counter;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = java.time.LocalDate.now();
        this.enginePower = enginePower;
        this.numberOfWheels = numberOfWheels;
        this.type = vehicleType;
        this.fuelType = fuelType;
    }

    /**
     * Converts vehicle to JSON object
     * @return JSON object
     */
    public JSONObject toJSON(){
        JSONObject vehicleJSON = new JSONObject();
        vehicleJSON.put("name", this.name);
        vehicleJSON.put("id", this.id);
        vehicleJSON.put("coordinates", this.coordinates.toJSON());
        vehicleJSON.put("enginePower", this.enginePower);
        vehicleJSON.put("numberOfWheels", this.numberOfWheels);
        vehicleJSON.put("vehicleType", this.type.toString());
        vehicleJSON.put("fuelType", this.fuelType.toString());
        vehicleJSON.put("creationDate", this.creationDate.toString());
        return vehicleJSON;
    }

    /**
     * Vehicle factory
     * @param json json object you want to use to create a vehicle object
     * @param trusted if set to false, id and and creation date fields will be ignored
     * @return new vehicle object
     */
    public static Vehicle fromJSON(JSONObject json, boolean trusted) {
        Vehicle vehicle = new Vehicle(
            json.getString("name"),
            Coordinates.fromJSON(json.getJSONObject("coordinates")),
            json.getInt("enginePower"),
            json.getInt("numberOfWheels"),
            VehicleType.valueOf(json.getString("vehicleType")),
            FuelType.valueOf(json.getString("fuelType"))
        );
        if (trusted) {
            //Creating new vehicle always increases counter, but because we have an id already
            //we actually shouldn't increases, but i suck at coding so this will do
            counter--;
            Integer id = json.getInt("id");
            //If given ID already exists, we throw an error
            if (VehicleCollection.getById(id) != null) {
                throw new ValueOutOfRangeException();
            }
            vehicle.setId(id);
            vehicle.setCreationDate(LocalDate.parse(json.getString("creationDate")));
        }
        return vehicle;
    }

    @Override
	public int compareTo(Vehicle e) {
		return this.getId() - e.getId();
	}

    @Override
    public String toString() {
        return String.format("%s - %s #%d%nFuel type: %s%nLocation: %s%nEngine Power: %d%nNumber of wheels: %d%nCreation date: %s",
            this.name, this.type, this.id, this.fuelType, this.coordinates.toString(), this.enginePower, this.numberOfWheels, this.creationDate.toString());
    }
}