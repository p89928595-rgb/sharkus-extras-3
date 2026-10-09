package sharkus.extras;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("sharkus-extras.json");
    public static Config I = new Config();

    public boolean armorDisplay, healthIndicator, hitColor, nametags, blockOutlines, teamTracker,
            itemPhysics, cps, fps = true, ping, crosshairIndicator, shieldStatus, motionBlur;

    // Nametags: eigener Name + Ersetzungen fremder Namen ("Original=Neu")
    public String ownNameOverride = "";
    public java.util.Map<String, String> nameReplacements = new java.util.HashMap<>();

    public static void load() {
        try {
            if (Files.exists(FILE)) I = GSON.fromJson(Files.readString(FILE), Config.class);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void save() {
        try { Files.writeString(FILE, GSON.toJson(I)); } catch (Exception e) { e.printStackTrace(); }
    }
}
