package ua.valeriihymchuk.crithittrain.config.serializers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import ua.valeriihymchuk.crithittrain.Plugin;

import java.lang.reflect.Type;
import java.util.Arrays;

public class LocationSerializer implements TypeSerializer<Location> {
    @Override
    public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if(node.isNull()) return null;
        else return convert(node.getString());
    }

    @Override
    public void serialize(Type type, @Nullable Location obj, ConfigurationNode node) throws SerializationException {
        if(obj != null) node.set(convert(obj));
        else node.raw(null);
    }

    private String convert(Location location) {
        return location.getX()
                + " " + location.getY()
                + " " + location.getZ()
                + " " + location.getYaw()
                + " " + location.getPitch()
                + " " + location.getWorld().getName();
    }

    private Location convert(String serialized) {
        String[] stripped = serialized.split(" ");
        return switch (stripped.length) {
            case 4 -> new Location(Bukkit.getWorld(stripped[3]),
                    Double.parseDouble(stripped[0]),
                    Double.parseDouble(stripped[1]),
                    Double.parseDouble(stripped[2]));
            case 6 -> new Location(Bukkit.getWorld(stripped[5]),
                    Double.parseDouble(stripped[0]),
                    Double.parseDouble(stripped[1]),
                    Double.parseDouble(stripped[2]),
                    Float.parseFloat(stripped[3]),
                    Float.parseFloat(stripped[4]));
            default -> null;
        };
    }

}
