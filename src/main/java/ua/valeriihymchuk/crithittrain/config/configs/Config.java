package ua.valeriihymchuk.crithittrain.config.configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import ua.valeriihymchuk.crithittrain.common.config.ConfigHolder;
import ua.valeriihymchuk.crithittrain.common.config.ConfigLoader;
import ua.valeriihymchuk.crithittrain.config.serializers.LocationSerializer;

import java.util.Collections;
import java.util.List;

@ConfigSerializable
public class Config extends ConfigHolder {

    public List<Location> entity_locations = Collections.singletonList(new Location(Bukkit.getWorld("world"), 0, 0,0));
    public String entity_type = EntityType.ZOMBIE.toString().toLowerCase();
    public String entity_name = "CRIT ME!";
    public String entity_admin_perm = "crithittrain.admin";

    public static Config load() {
        return ConfigLoader.load("config.yml", Config.class, TypeSerializerCollection.builder()
                .register(Location.class, new LocationSerializer())
                .build());
    }

}
