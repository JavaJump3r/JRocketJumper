package io.github.javajump3r.jrocketjumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.util.logging.Logger;
public final class JRocketJumper extends JavaPlugin{
    static private JRocketJumper instance;
    static JRocketJumper getInstance(){
        return instance;
    }
    static public Config config = new Config();
    private static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    static Logger logger;
    @Override
    public void onEnable() {
        instance=this;
        // Plugin startup logic
        //restoreConfig();
        getLogger().info("JRocketJumper init");
        logger = getLogger();
        getServer().getPluginManager().registerEvents(new EventHandlers(),this);
    }
    public void restoreConfig(){
        File dataFolder = getDataFolder();
        File configFile = dataFolder.toPath().resolve("jrjconfig.json").toFile();
        try{
            //config = prettyGson.fromJson(FileReadWrite.readJson(configFile), Config.class);
        }
        catch (JsonSyntaxException exception){
            //FileReadWrite.write(configFile,prettyGson.toJson(new Config()));
            restoreConfig();
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
