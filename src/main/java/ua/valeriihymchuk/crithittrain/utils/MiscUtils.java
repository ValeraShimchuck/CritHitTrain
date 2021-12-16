package ua.valeriihymchuk.crithittrain.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import ua.valeriihymchuk.crithittrain.Plugin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;


public class MiscUtils {

    private static final Logger LOGGER;

    static {
        LOGGER = Plugin.getInstance().getLogger();
    }

    // logging

    public static void info(String message, Object... args) {
        LOGGER.info(String.format(message, args));
    }

    public static void warning(String message, Object... args) {
        LOGGER.warning(String.format(message, args));
    }

    public static <T> T printException(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        Bukkit.getLogger().warning(sw.toString());
        return null;
    }


    /**
     * Sneaky throw any type of Throwable.
     */
    public static void sneakyThrow(Throwable throwable) {
        MiscUtils.sneakyThrow0(throwable);
    }

    /**
     * Sneaky throw any type of Throwable.
     */
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void sneakyThrow0(Throwable throwable) throws E {
        throw (E) throwable;
    }



}

