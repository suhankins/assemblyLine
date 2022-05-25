package assemblyline.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import assemblyline.VehicleCollection;
import assemblyline.vehicles.Coordinates;
import assemblyline.vehicles.FuelType;
import assemblyline.vehicles.VehicleType;
import assemblyline.vehicles.Vehicle;

/**
 * Class used for all things file management
 */
public class FileManager {
    /**
     * Loads and executes commands from a specified file
     * @param filename name of the file from which command list should be loaded
     */
    public static void loadScript(String filename) {
        File file = new File(filename);
        Scanner fileReader;
        try {
            fileReader = new Scanner(file);
        } catch(FileNotFoundException exception) {
            IO.print(ErrorMessages.FILE_DOES_NOT_EXIST);
            return;
        } catch(Exception e) {
            IO.print(ErrorMessages.TEMPLATE, e.getMessage());
            return;
        }
        ArrayList<String> lines = new ArrayList<String>();
        while (fileReader.hasNextLine()) {
            lines.add(fileReader.nextLine());
        }
        IO.addScript(lines, file);
        fileReader.close();
    }
    /**
     * Saves list of created vehicles to requested file
     * @param filename name of the file to which vehicle list should be written
     */
    public static void saveCollection(String filename) {
        if (VehicleCollection.isEmpty()) {
            IO.print("%s%n", ErrorMessages.COLLECTION_IS_EMPTY);
            return;
        }

        try {
            FileWriter file = new FileWriter(filename);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write(VehicleCollection.toJSON().toString());
            writer.close();
        } catch(Exception e) {
            IO.print(ErrorMessages.TEMPLATE, e.getMessage());
            return;
        }

        IO.print("File successfully saved%n");
    }
    /**
     * Loads vehicle collection from a specified file
     * @param filename name of the file
     */
    public static void loadSave(String filename) {
        // Since this is probably the last thing I'm going to add to this project
        // before next lab, here's the song I listened to while writing this code
        // https://www.youtube.com/watch?v=oqLOBhaizy8
        // 19.05.2022 Doing my next lab, here's another song I'm listening to.
        // https://www.youtube.com/watch?v=ucbx9we6EHk
        File file = new File(filename);
        Scanner fileReader;
        try {
            fileReader = new Scanner(file);
        } catch(FileNotFoundException exception) {
            IO.print(ErrorMessages.FILE_DOES_NOT_EXIST);
            return;
        } catch(Exception e) {
            IO.print(ErrorMessages.TEMPLATE, e.getMessage());
            return;
        }
        String rawJSON = "";
        while (fileReader.hasNextLine()) {
            rawJSON = String.format("%s%n%s", rawJSON, fileReader.nextLine());
        }
        fileReader.close();
        JSONObject vehiclesJSON = new JSONObject(rawJSON);
        VehicleCollection.fromJSON(vehiclesJSON);

        IO.print("Collection was successfully loaded!%n");
    }
}