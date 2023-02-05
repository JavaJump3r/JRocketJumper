package io.github.javajump3r.jrocketjumper;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Collection;

public class EventHandlers implements Listener {
    @EventHandler
    void onFireworkExplode(FireworkExplodeEvent event) {
        Config config = JRocketJumper.config;

        Firework firework = event.getEntity();

        Vector fireworkPos = firework.getLocation().toVector();
        BoundingBox box = new BoundingBox(fireworkPos.getX(), fireworkPos.getY(), fireworkPos.getZ(), fireworkPos.getX(), fireworkPos.getY(), fireworkPos.getZ());
        box.expand(16);
        fireworkPos = fireworkPos.add(new Vector(0,config.boostYOffset,0));

        Collection<Entity> affectedEntities = firework.getNearbyEntities(32, 32, 32);
        for (Entity entity : affectedEntities) {
            if (entity instanceof Firework)
                return;
            if (entity instanceof Player) {
                if (((Player) entity).getGameMode() == GameMode.SPECTATOR)
                    return;
            }
            Vector entityVelocity = entity.getVelocity();
            entityVelocity = new Vector(
                    entityVelocity.getX(),
                    Math.min(
                            1.0,
                            Math.abs(entityVelocity.getY())
                    ),
                    entityVelocity.getZ());
            Vector direction = entity.getLocation().toVector().subtract(fireworkPos).normalize();
            Vector addVelocity = direction;

            double entityTypeMultiplier;
            if (entity instanceof Player) {
                entityTypeMultiplier = config.playerMultiplier;
            } else if (entity instanceof LivingEntity) {
                entityTypeMultiplier = config.livingEntityMultiplier;
            } else {
                entityTypeMultiplier = config.otherEntityMultiplier;
            }

            double distance = entity.getLocation().toVector().distance(fireworkPos);
            double distanceMultiplier = Util.calculatePower(config, distance);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            double starCountMultiplier = fireworkMeta.getEffectsSize() * config.perStarMultiplier;

            addVelocity = addVelocity
                    .multiply(entityTypeMultiplier)
                    .multiply(starCountMultiplier)
                    .multiply(distanceMultiplier);

            entityVelocity = entityVelocity.add(addVelocity);
            if (addVelocity.length() > 0.01) {
                entity.setVelocity(entityVelocity);
            }
        }
    }
    @EventHandler
    void onPlayerElytraBoost(PlayerElytraBoostEvent event){
        Config config = JRocketJumper.config;
        if(config.disableVanillaBoost)
        {
            event.setCancelled(true);
            if(config.sendDisableVanillaBoostMessage){
                event.getPlayer().sendMessage(Component.text(config.disableVanillaBoostMessage));
            }
        }
    }
}
