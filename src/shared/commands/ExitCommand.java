package assemblyline.commands;

import org.json.JSONObject;

public class ExitCommand extends Command {
    public JSONObject request(String[] args) {
        System.exit(0);
        return null;
    }

    public JSONObject respond(JSONObject args, boolean trusted) {
        //System.exit(0);
        return null;
    }

    public void react(JSONObject args) {
    }

    public String getHelp() {
        return String.format("Closes the program.%n%nUsage: exit");
    }
}