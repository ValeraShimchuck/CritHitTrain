package ua.valeriihymchuk.crithittrain.common;

import ua.valeriihymchuk.crithittrain.Plugin;

public abstract class Manager {

    protected final Plugin plugin;


    public Manager(Plugin plugin) {
        this.plugin = plugin;
    }


    public void onEnable() {

    }

    public void onDisable() {

    }

}
