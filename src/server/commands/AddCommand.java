package server.commands;

import common.CommandType;
import common.ReturnCode;
import server.collection.VehicleManager;

public class AddCommand implements Command {
    private final CommandType type = CommandType.WITHMODEL;
    private final VehicleManager vehicleAdder;

    public AddCommand(VehicleManager vehicleAdder) {
        this.vehicleAdder = vehicleAdder;
    }

    @Override
    public ReturnCode execute(CommandParams params) {
        // Устанавливаем владельца из авторизованного пользователя
        params.vehicle().setOwnerLogin(params.login());

        // addElement теперь возвращает boolean: true = успех БД + памяти
        if (vehicleAdder.addElement(params.vehicle())) {
            if (params.isLaud()) {
                params.responseSender().send("Транспортное средство успешно добавлено");
                params.responseSender().send("ID: " + params.vehicle().getId());
            }
            return ReturnCode.OK;
        }
        if (params.isLaud()) {
            params.responseSender().sendError("Не удалось добавить объект в базу данных");
        }
        return ReturnCode.FAILED;
    }

    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию";
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}