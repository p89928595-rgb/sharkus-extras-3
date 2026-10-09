package sharkus.extras;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Hud {
    private static final ArrayDeque<Long> clicks = new ArrayDeque<>();
    private static boolean wasDown;

    public static void tick(Minecraft mc) {
        if (mc.getWindow() == null) return;
        boolean down = GLFW.glfwGetMouseButton(mc.getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        if (down && !wasDown) clicks.add(System.currentTimeMillis());
        wasDown = down;
        long now = System.currentTimeMillis();
        while (!clicks.isEmpty() && now - clicks.peek() > 1000) clicks.poll();
    }

    public static void register() {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("sharkus-extras", "hud"), Hud::render);
    }

    private static void render(GuiGraphicsExtractor g, net.minecraft.client.DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        Config c = Config.I;
        List<String> lines = new ArrayList<>();
        if (c.fps) lines.add("FPS: " + mc.getFps());
        if (c.cps) lines.add("CPS: " + clicks.size());
        if (c.ping && mc.getConnection() != null) {
            PlayerInfo info = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            lines.add("Ping: " + (info == null ? "?" : info.getLatency()) + " ms");
        }
        int y = 4;
        for (String s : lines) {
            g.text(mc.font, s, 4, y, 0xFFFFFFFF);
            y += 10;
        }
    }
}
