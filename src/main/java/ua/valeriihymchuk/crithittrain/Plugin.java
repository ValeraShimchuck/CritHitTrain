package ua.valeriihymchuk.crithittrain;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import ua.valeriihymchuk.crithittrain.command.AdminCommands;
import ua.valeriihymchuk.crithittrain.common.Manager;
import ua.valeriihymchuk.crithittrain.common.config.ConfigLoader;
import ua.valeriihymchuk.crithittrain.config.configs.Config;
import ua.valeriihymchuk.crithittrain.config.serializers.LocationSerializer;
import ua.valeriihymchuk.crithittrain.mob.MobManager;

import java.util.HashMap;
import java.util.Map;

public final class Plugin extends JavaPlugin {

    private static Plugin instance;

    private final Map<Class<?>, Manager> managers = new HashMap<>();

    private Config config;

    @Override
    public void onEnable() {
        instance = this;
        config = Config.load();
        enableManager(MobManager.class, new MobManager(this));
        loadCommands();
    }

    @Override
    public void onDisable() {
        managers.values().forEach(Manager::onDisable);
    }


    private void loadCommands() {
        getCommand("hittrain").setExecutor(new AdminCommands(this));
    }


    public Config config() {
        return config;
    }

    public void saveConfiguration() {
        ConfigLoader.save("config.yml", config, TypeSerializerCollection.builder()
                .register(Location.class, new LocationSerializer())
                .build());
    }

    public <T extends Manager> T enableManager(T manager) {
        return enableManager(manager.getClass(), manager);
    }

    public <T extends Manager> T enableManager(Class<?> type, T manager) {
        managers.put(type, manager);
        manager.onEnable();
        return manager;
    }

    public <T> T getManager(Class<T> type) {
        return type.cast(managers.get(type));
    }

    public static Plugin getInstance() {
        return instance;
    }
}
