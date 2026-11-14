package me.dimalyckovskij.regionprotect;

import org.bukkit.plugin.java.JavaPlugin;

public class RegionProtect extends JavaPlugin {

    private static RegionProtect instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new BlockListener(this), this);

        getLogger().info("RegionProtect enabled!");
    }

    public static RegionProtect getInstance() {
        return instance;
    }
}
