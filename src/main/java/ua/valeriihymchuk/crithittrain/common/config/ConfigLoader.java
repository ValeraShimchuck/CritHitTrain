package ua.valeriihymchuk.crithittrain.common.config;


import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import ua.valeriihymchuk.crithittrain.Plugin;

import java.io.File;

import static ua.valeriihymchuk.crithittrain.utils.MiscUtils.sneakyThrow;


public class ConfigLoader {
    
    public static <T extends ConfigHolder> T load(String path, Class<T> type, TypeSerializerCollection... serializers) {
        File file = new File(Plugin.getInstance().getDataFolder(), path);
        return load(file, type, true, serializers);
    }
    
    public static <T extends ConfigHolder> T load(File file, Class<T> type, boolean save, TypeSerializerCollection... serializers) {
        try {
            YamlConfigurationLoader loader = getLoader(file, serializers);
            
            // load
            CommentedConfigurationNode node = loader.load();
            T config = node.get(type);
            
            config.setRootNode(node);
            config.onPostLoad();
            
            // save
            if (save) {
                node.set(type, config);
                
                try {
                    loader.save(node);
                } catch (ConfigurateException ex) {
                    // suppress
                }
            }
            
            return config;
        } catch (Exception ex) {
            sneakyThrow(ex);
            throw new RuntimeException();
        }
    }

    private static YamlConfigurationLoader getLoader(File file, TypeSerializerCollection... serializers) {
        YamlConfigurationLoader.Builder builder = YamlConfigurationLoader.builder()
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .file(file);

        // register serializers
        if (serializers.length > 0) {
            builder.defaultOptions(opts -> opts
                    .serializers(b -> {
                        for (TypeSerializerCollection collection : serializers)
                            b.registerAll(collection);
                    }));
        }

        return builder.build();
    }
    public static <T extends ConfigHolder> void save(String path, T obj, TypeSerializerCollection... serializers) {
        File file = new File(Plugin.getInstance().getDataFolder(), path);
        save(file, obj, serializers);
    }

    public static  <T extends ConfigHolder> void save(File file, T obj, TypeSerializerCollection... serializers) {
        try {

            YamlConfigurationLoader loader = getLoader(file, serializers);

            // load
            CommentedConfigurationNode node = loader.load();

            obj.setRootNode(node);

            // save
            node.set(obj.getClass(), obj);

            try {
                loader.save(node);
            } catch (ConfigurateException ex) {
                // suppress
            }

        } catch (Exception ex) {
            sneakyThrow(ex);
            throw new RuntimeException();
        }
    }

}

