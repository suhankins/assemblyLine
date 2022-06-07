package assemblyline.commands;

import assemblyline.utils.FileManager;

import org.json.JSONObject;

public class SaveCommand extends Command {
    {
        allowedOnClient = false;
    }
    public JSONObject request(String[] args) {
        isArgumentGiven(args);
        return new JSONObject().put("command", "save").put("data", args[0]);
    }
    public JSONObject respond(JSONObject args) {
        FileManager.saveCollection(args.getString("data"));
        return null;
    }
    public void react(JSONObject args) {
    }

    public String getHelp() {
        return String.format("Saves vehicle collection to a specified JSON file.%n%nUsage: save [file path]");
    }
}