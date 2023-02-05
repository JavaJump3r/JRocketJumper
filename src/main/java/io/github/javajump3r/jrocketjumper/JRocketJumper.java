package io.github.javajump3r.jrocketjumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.util.logging.Logger;
public final class JRocketJumper extends JavaPlugin{
    static public Config config = new Config();
    private static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    static Logger logger;
    @Override
    public void onEnable() {
        restoreConfig();
        logger = getLogger();
        getServer().getPluginManager().registerEvents(new EventHandlers(),this);
        getLogger().info("JRocketJumper initialized");
    }
    public void restoreConfig(){
        try {
            File dataFolder = getDataFolder();
            File configFile = dataFolder.toPath().resolve("jrjconfig.json").toFile();
            if(!configFile.exists())
                FileReadWrite.write(configFile,"~");
            try {
                config = prettyGson.fromJson(FileReadWrite.read(configFile), Config.class);
            } catch (JsonSyntaxException exception) {
                FileReadWrite.write(configFile, prettyGson.toJson(new Config()));
                restoreConfig();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
