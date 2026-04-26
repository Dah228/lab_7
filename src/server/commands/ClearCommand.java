package server.commands;

import common.CommandType;
import common.ReturnCode;
import server.collection.VehicleManager;

public class ClearCommand implements Command {
    private final VehicleManager manager;
    private final CommandType type = CommandType.NOARGS;

    public ClearCommand(VehicleManager manager) {
        this.manager = manager;
    }

    @Override
    public ReturnCode execute(CommandParams params) {
        if (params.args().size() != 1) return ReturnCode.FAILED;
        // Передаём логин: очистка только своих объектов
        manager.clearCollection(params.login());
        if (params.isLaud()) params.responseSender().send("Коллекция очищена (ваши объекты)");
        return ReturnCode.OK;
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}