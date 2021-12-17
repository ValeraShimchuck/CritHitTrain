package ua.valeriihymchuk.crithittrain.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ua.valeriihymchuk.crithittrain.Plugin;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerCommands implements CommandExecutor {


    private final HashMap<Player, Integer> pushLevel = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return true;
        if(args.length < 1) return true;
        int n = Integer.parseInt(args[0]);
        if(n <= 0) pushLevel.remove(player);
        else {
            if(pushLevel.containsKey(player)) pushLevel.replace(player, n);
            else pushLevel.put(player, n);
        }
        return true;
    }


    public PlayerCommands() {
        runPushTask();
    }

    private Vector get2DPushVectorByDegrees(int degrees, double distance) {
        double radians = Math.toRadians(degrees);
        double x = Math.sin(radians) * distance * -1;
        double z = (1D / Math.tan(radians)) * x * -1;
        return new Vector(x, 0, z);
    }

    private void runPushTask() {
        Bukkit.getScheduler().runTaskTimer(Plugin.getInstance(), () -> pushLevel.forEach((p, level) -> {
            p.damage(0D);
            Vector pushVector = get2DPushVectorByDegrees(ThreadLocalRandom.current().nextInt(-180, 180), ((double) level) / 10);
            pushVector.add(new Vector(0, 0.4, 0));
            p.setVelocity(pushVector);
        }), 0, 40L);
    }

}
