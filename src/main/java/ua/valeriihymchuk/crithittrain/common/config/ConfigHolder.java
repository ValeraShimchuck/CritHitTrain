package ua.valeriihymchuk.crithittrain.common.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
public abstract class ConfigHolder {

    protected transient CommentedConfigurationNode rootNode;

    public void setRootNode(CommentedConfigurationNode rootNode){
        this.rootNode = rootNode;
    }

    public void onPostLoad() {}

}
