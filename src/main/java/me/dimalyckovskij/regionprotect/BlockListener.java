package me.dimalyckovskij.regionprotect;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.Set;

public class BlockListener implements Listener {

    private final RegionProtect plugin;

    public BlockListener(RegionProtect plugin) {
        this.plugin = plugin;
    }

    private final Set<Material> blocked = Set.of(
            Material.REDSTONE,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.OBSERVER,
            Material.PISTON,
            Material.STICKY_PISTON,
            Material.DISPENSER,
            Material.DROPPER,
            Material.HOPPER,
            Material.REDSTONE_TORCH,
            Material.REDSTONE_WALL_TORCH,
            Material.REDSTONE_LAMP,
            Material.LEVER
    );

    private boolean isOutsideRegions(Player player) {

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        ApplicableRegionSet regions =
                query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        return regions.size() == 0;
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent e) {

        Player player = e.getPlayer();

        if (player.hasPermission("regionprotect.bypass"))
            return;

        if (!isOutsideRegions(player))
            return;

        e.setCancelled(true);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("message")));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        Player player = e.getPlayer();

        if (player.hasPermission("regionprotect.bypass"))
            return;

        if (!isOutsideRegions(player))
            return;

        if (!blocked.contains(e.getBlock().getType()))
            return;

        e.setCancelled(true);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("message")));
    }

}
