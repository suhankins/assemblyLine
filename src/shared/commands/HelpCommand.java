package assemblyline.commands;

import java.util.Enumeration;
import java.util.Hashtable;

import assemblyline.utils.CommandDoesNotExistException;
import assemblyline.utils.IO;

import org.json.JSONArray;
import org.json.JSONObject;

public class HelpCommand extends Command {
    public JSONObject request(String[] args) {
        JSONObject output = new JSONObject().put("command", "help");
        if (args.length == 0) {
            IO.print("Asking server for the list...%n");
        } else {
            IO.print("Asking server for the description of %s command...%n", args[0]);
            output.put("data", args[0]);
        }
        return output;
    }

    public JSONObject respond(JSONObject args) {
        Hashtable<String, Command> commandList = Command.getCommandList();
        JSONArray list = new JSONArray();
        //if we were asked about a specific command, reply with its description
        if (args.has("data")) {
            String data = args.getString("data").toLowerCase();
            if (!doesCommandExist(data)) {
                throw new CommandDoesNotExistException(data);
            }

            list.put(commandList.get(data).getHelp());
        } else {
        //otherwise print the list of commands
            Enumeration keys = commandList.keys();
            while (keys.hasMoreElements()) {
                list.put(keys.nextElement());
            }
        }

        return new JSONObject().put("data", list).put("command", "help");
    }

    public void react(JSONObject args) {
        JSONArray array = args.getJSONArray("data");
        if (array.length() > 1) {
            IO.print("List of commands:%n");
        }
        for (int i = 0; i < array.length(); i++) {
            IO.print("%s%n", array.getString(i));
        }
    }

    public String getHelp() {
        return String.format("Prints description of a command given in an argument.%n%nUsage: help [command]%n%nIf no arguments are given, prints list of existing commands.%n%nUsage: help");
    }
}