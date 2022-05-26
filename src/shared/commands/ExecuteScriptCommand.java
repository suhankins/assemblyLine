package assemblyline.commands;

import assemblyline.utils.FileManager;

import org.json.JSONObject;

/**
 * Executes script from a specified file
 */
public class ExecuteScriptCommand extends Command {
    public JSONObject request(String[] args) {
        isArgumentGiven(args);
        
        FileManager.loadScript(args[0]);
        return null;
    }

    public JSONObject respond(JSONObject args) {
        return null;
    }

    public void react(JSONObject args) {
        return;
    }

    public String getHelp() {
        return String.format("Executes script from specified files.%n%nUsage: execute_script [file path]");
    }
}