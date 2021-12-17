package ua.valeriihymchuk.crithittrain.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ua.valeriihymchuk.crithittrain.Plugin;
import ua.valeriihymchuk.crithittrain.mob.MobManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCommands implements CommandExecutor {

    private final Plugin plugin;

    public AdminCommands(Plugin plugin) {
        this.plugin = plugin;
    }


    private Entity getLookingAt(Player player) {
        return player.getNearbyEntities(10, 10, 10).stream()
                .filter(player::hasLineOfSight)
                .min(new DistanceComparator(player))
                .orElse(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return true;
        if(!player.hasPermission(plugin.config().entity_admin_perm)) return true;
        if(args.length < 1) return true;
        switch (args[0]) {
            case "addentity" -> plugin.getManager(MobManager.class).addEntity(player.getLocation());
            case "removeentity" -> {
                Entity lookingAt = getLookingAt(player);
                if(lookingAt != null) plugin.getManager(MobManager.class).removeEntity(lookingAt, false);
            }
        }
        return true;
    }

    private static class DistanceComparator implements Comparator<Entity> {

        private Player player;

        public DistanceComparator(Player player) {
            this.player = player;
        }

        @Override
        public int compare(Entity o1, Entity o2) {
            return Double.compare(player.getLocation().distance(o1.getLocation()), player.getLocation().distance(o2.getLocation()));
        }
    }

}
