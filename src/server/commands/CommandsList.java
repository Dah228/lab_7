package server.commands;

import server.collection.VehicleManager;

import java.util.HashMap;
import java.util.Map;

public class CommandsList {
    private final Map<String, Command> commandList = new HashMap<>();
    private final Invoker invoker;

    // Конструктор теперь принимает готовый VehicleManager (с DAO)
    public CommandsList(VehicleManager manager, Invoker invoker) {
        this.invoker = invoker;  // ← используем переданный
        registerCommands(manager);
    }


    private void registerCommands(VehicleManager manager) {
        // Команды без аргументов
        register("clear", new ClearCommand(manager));
        register("info", new InfoCommand(manager));
        register("show", new ShowCommand(manager));
        register("print_descending", new PrintDescendingCommand(manager));
        register("shuffle", new ShuffleCommand(manager));
        register("sort", new SortCommand(manager));
        register("help", new HelpCommand(invoker.getCommands()));
        register("group_by", new GroupByCommand(manager));

        // Команды с аргументами
        register("filter_greater_than_engine_power", new CompareByEnginePowerCommand(manager));
        register("remove_by_id", new RemoveByID(manager));
        register("filter_less_than_type", new FilterLessThatType(manager));

        // Команды с моделью
        register("add", new AddCommand(manager));
        register("add_if_max", new AddIfMax(manager));
        register("update", new UpdateElementID(manager));

        // register — спец-команда, обрабатывается в Invoker
    }

    private void register(String name, Command command) {
        commandList.put(name, command);
        invoker.registerCommand(name, command);
    }

    public Map<String, Command> getCommandList() {
        return commandList;
    }

    public Invoker getInvoker() {
        return invoker;
    }
}