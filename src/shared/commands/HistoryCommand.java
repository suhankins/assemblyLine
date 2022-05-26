package assemblyline.commands;

import assemblyline.utils.IO;

import org.json.JSONObject;

/**
 * Prints last 12 executed commands
 */
public class HistoryCommand extends Command {
    public JSONObject request(String[] args) {
        for (int i = 11; i >= 0; i--) {
            if (history[i] != null) {
                IO.print("%s%n", history[i]);
            }
        }
        return null;
    }

    public JSONObject respond(JSONObject args) {
        for (int i = 11; i >= 0; i--) {
            if (history[i] != null) {
                IO.print("%s%n", history[i]);
            }
        }
        return null;
    }

    public void react(JSONObject args) {
        return;
    }

    public String getHelp() {
        return String.format("Prints 12 last executed commands.%n%nUsage: history");
    }
}