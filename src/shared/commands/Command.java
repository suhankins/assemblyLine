package assemblyline.commands;

import java.util.Hashtable;

import assemblyline.utils.CommandDoesNotExistException;
import assemblyline.utils.NoArgumentGivenException;

import org.json.JSONObject;

/**
 * Class for commands
 */
public abstract class Command {
    /**
     * Is user client or server?
     * Should be set in main on startup
     */
    public static Boolean client;
    /**
     * List of commands
     */
    private static Hashtable<String, Command> commandList = new Hashtable<String, Command>();

    /**
     * Used for history command. Stores last 12 executed commands
     */
    public static String[] history = new String[12];

     /**
     * Some commands should only be accessible to server.
     * By default, we assume that all commands can be used by client too.
     */
    public Boolean allowedOnClient = true;
    /**
     * Some commands are not meant to be used by people.
     * By default, all commands are assumed to be visible.
     */
    public Boolean hidden = false;

    //Static initialization block
    static {
        commandList.put("exists", new ExistsCommand());
        commandList.put("exists_key", new ExistsKeyCommand());

        commandList.put("help", new HelpCommand());
        commandList.put("info", new InfoCommand());
        commandList.put("show", new ShowCommand());
        commandList.put("insert", new InsertCommand());
        commandList.put("update", new UpdateCommand());
        commandList.put("remove_key", new RemoveKeyCommand());
        commandList.put("clear", new ClearCommand());

        commandList.put("save", new SaveCommand());
        commandList.put("execute_script", new ExecuteScriptCommand());
        commandList.put("exit", new ExitCommand());
        commandList.put("history", new HistoryCommand());
        commandList.put("replace_if_lower", new ReplaceIfLowerCommand());
        commandList.put("remove_lower_key", new RemoveLowerKeyCommand());

        commandList.put("print_field_ascending_fuel_type", new PrintFieldSortedCommand("Fuel type", true));
        commandList.put("print_field_descending_engine_power", new PrintFieldSortedCommand("Engine power", false));
        commandList.put("print_field_descending_number_of_wheels", new PrintFieldSortedCommand("Number of wheels", false));
    }

    /**
     * Gets the whole command list
     * @return commandList
     */
    public static Hashtable<String, Command> getCommandList() {
        return commandList;
    }

    /**
     * Execute a command with arguments
     * @param name command name
     * @param args list of arguments
     */
    public static JSONObject requestCommand(String name, String[] args){
        if (!doesCommandExist(name)) {
            throw new CommandDoesNotExistException(name);
        }

        Command command = commandList.get(name);

        if (command.hidden) {
            throw new CommandDoesNotExistException(name);
        }

        appendHistory(name);

        return command.request(args);
    }

    /**
     * Execute a command without arguments
     * @param name command name
     */
    public static JSONObject requestCommand(String name){
        return requestCommand(name, new String[0]);
    }
    
    /**
     * Respond to user's command
     * @param args user's request
     * @param trusted is this a command sent from console or from client?
     * @return result of commands execution
     */
    public static JSONObject respondCommand(JSONObject args, Boolean trusted) {
        Command command = commandList.get(args.getString("command"));
        //If client uses command it's not supposed to use, we just say it doesn't exist
        if (!trusted & !command.allowedOnClient) {
            throw new CommandDoesNotExistException(args.getString("command"));
        }
        appendHistory(args.getString("command"));
        return command.respond(args, trusted);
    }
    
    /**
     * React to data replied by server
     * @param args Server's answer
     */
    public static void reactCommand(JSONObject args) {
        commandList.get(args.getString("command")).react(args);
    }

    /**
     * Append a new command to history list
     * @param name
     */
    public static void appendHistory(String name) {
        for (int i = history.length - 1; i > 0; i--) {
            history[i] = history[i - 1];
        }
        history[0] = name;
    }

    /**
     * Checks if command exists
     * @param name command name
     * @return whether command exists or not
     */
    public static boolean doesCommandExist(String name) {
        return commandList.containsKey(name);
    }

    /**
     * Checks if argument was given. Throws NoArgumentGivenException if not.
     * @param args arguments
     */
    public static void isArgumentGiven(String[] args) {
        if (args.length == 0) {
            throw new NoArgumentGivenException();
        }
    }

    //=============== Instance methods ===============

    /**
     * Execute the command on client side
     * @param args arguments
     */
    public abstract JSONObject request(String[] args);

    /**
     * Execute the command on server side
     * @param args JSONObject with arguments
     * @param trusted was command sent from console or server?
     */
    public abstract JSONObject respond(JSONObject args, boolean trusted);

    /**
     * Handle server's response on client side
     * @param args JSONObject with arguments
     */
    public abstract void react(JSONObject args);

    /**
     * Get command's description
     * @return command description
     */
    public abstract String getHelp();
}