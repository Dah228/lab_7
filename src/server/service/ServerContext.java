package server.service;

import server.collection.VehicleCollection;
import server.collection.VehicleManager;
import server.commands.CommandsList;
import server.commands.Invoker;
import server.database.AuthService;
import server.database.VehicleDao;

public class ServerContext {
    private final CommandsList commandsList;
    private final Invoker invoker;
    private final ServerNetworkService networkService;
    private final VehicleManager vehicleManager;  // ← вынесено для удобства
    private final String xmlFilePath; // ← оставляем, но не используем для загрузки
    private final int port;

    // server/service/ServerContext.java
    public ServerContext(int port, String xmlFilePath) {
        this.port = port;
        this.xmlFilePath = xmlFilePath;

        AuthService authService = new AuthService();
        VehicleDao vehicleDao = new VehicleDao();
        VehicleCollection collection = new VehicleCollection();

        // 1. Создаём Invoker с AuthService
        this.invoker = new Invoker(authService);

        // 2. VehicleManager требует VehicleDao
        this.vehicleManager = new VehicleManager(collection, vehicleDao);

        // 3. CommandsList получает manager И invoker (один и тот же!)
        this.commandsList = new CommandsList(vehicleManager, invoker);  // ← ПЕРЕДАЁМ invoker

        this.networkService = new ServerNetworkService(port, commandsList);
    }

    public boolean startNetwork() {
        return networkService.start();
    }

    // Геттеры
    public CommandsList getCommandsList() { return commandsList; }
    public Invoker getInvoker() { return invoker; }
    public ServerNetworkService getNetworkService() { return networkService; }
    public VehicleManager getVehicleManager() { return vehicleManager; } // ← НОВЫЙ
    public String getXmlFilePath() { return xmlFilePath; }
    public int getPort() { return port; }

    public void stop() {
        if (networkService != null) networkService.stop();
    }
}