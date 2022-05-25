package assemblyline.commands;

import assemblyline.utils.FileManager;

/**
 * Executes script from a specified file
 */
public class ExecuteScriptCommand extends Command {
    public JSONObject execute(String[] args) {
        isArgumentGiven(args);
        
        FileManager.loadScript(args[0]);

        IO.print("Executing the script...%n");
        return null;
    }

    public JSONObject execute(JSONObject args) {
        execute(args.getString("data"));
    }

    public String getHelp() {
        return String.format("Executes script from specified files.%n%nUsage: execute_script [file path]");
    }
}