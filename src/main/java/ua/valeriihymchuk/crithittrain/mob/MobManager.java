package ua.valeriihymchuk.crithittrain.mob;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import ua.valeriihymchuk.crithittrain.Plugin;
import ua.valeriihymchuk.crithittrain.common.Manager;
import ua.valeriihymchuk.crithittrain.mob.handler.DamageHandler;

public class MobManager extends Manager {

    public MobManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        removeAllEntities();
        spawnAllEntities();
        Bukkit.getPluginManager().registerEvents(new DamageHandler(this), plugin);
    }


    @Override
    public void onDisable() {
        removeAllEntities();
    }

    private void spawnAllEntities() {
        plugin.config().entity_locations.forEach(this::addEntity);
    }

    private void removeAllEntities() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(this::removeEntity));
    }

    public void addEntity(Location location) {
        Entity entity = location.getWorld().spawnEntity(location, EntityType.valueOf(plugin.config().entity_type.toUpperCase()));
        entity.getPersistentDataContainer().set(new NamespacedKey(plugin, "custommob"), PersistentDataType.BYTE,  (byte)0);
        entity.setCustomName(plugin.config().entity_name);
        entity.setGravity(false);

        if(entity instanceof LivingEntity livingEntity) {
            livingEntity.setAI(false);
            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setCollidable(false);
        }

        if(!plugin.config().entity_locations.contains(location)) {
            plugin.config().entity_locations.add(location);
            plugin.saveConfiguration();
        }
    }
    public void removeEntity(Entity entity) {
        removeEntity(entity, true);
    }

    public void removeEntity(Entity entity, boolean save) {
        if(isPluginMob(entity)) {
            entity.remove();
            if(!save) {
                plugin.config().entity_locations.remove(entity.getLocation());
                plugin.saveConfiguration();
            }
        }
    }



    public boolean isPluginMob(Entity entity) {
        return entity.getPersistentDataContainer().has(new NamespacedKey(plugin, "custommob"), PersistentDataType.BYTE);
    }


}
