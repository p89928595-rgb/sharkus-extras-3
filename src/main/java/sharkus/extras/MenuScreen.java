package sharkus.extras;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuScreen extends Screen {
    private final Screen parent;

    public MenuScreen(Screen parent) {
        super(Component.literal("Sharkus Extras"));
        this.parent = parent;
    }

    private void toggle(String name, int col, int row, Supplier<Boolean> get, Consumer<Boolean> set) {
        int w = 150, x = width / 2 - w - 2 + col * (w + 4), y = 40 + row * 24;
        addRenderableWidget(Button.builder(label(name, get.get()), b -> {
            set.accept(!get.get());
            b.setMessage(label(name, get.get()));
            Config.save();
        }).bounds(x, y, w, 20).build());
    }

    private static Component label(String n, boolean on) {
        return Component.literal(n + ": " + (on ? "AN" : "AUS"));
    }

    @Override
    protected void init() {
        Config c = Config.I;
        toggle("Armor Display", 0, 0, () -> c.armorDisplay, v -> c.armorDisplay = v);
        toggle("Health Indicator", 1, 0, () -> c.healthIndicator, v -> c.healthIndicator = v);
        toggle("Hit Color", 0, 1, () -> c.hitColor, v -> c.hitColor = v);
        toggle("Nametags", 1, 1, () -> c.nametags, v -> c.nametags = v);
        toggle("Block Outlines", 0, 2, () -> c.blockOutlines, v -> c.blockOutlines = v);
        toggle("Team Tracker", 1, 2, () -> c.teamTracker, v -> c.teamTracker = v);
        toggle("Item Physics", 0, 3, () -> c.itemPhysics, v -> c.itemPhysics = v);
        toggle("CPS", 1, 3, () -> c.cps, v -> c.cps = v);
        toggle("FPS", 0, 4, () -> c.fps, v -> c.fps = v);
        toggle("Ping", 1, 4, () -> c.ping, v -> c.ping = v);
        toggle("Crosshair Indicator", 0, 5, () -> c.crosshairIndicator, v -> c.crosshairIndicator = v);
        toggle("Shield Status", 1, 5, () -> c.shieldStatus, v -> c.shieldStatus = v);
        toggle("Motion Blur", 0, 6, () -> c.motionBlur, v -> c.motionBlur = v);

        addRenderableWidget(Button.builder(Component.literal("Fertig"), b -> onClose())
                .bounds(width / 2 - 50, 40 + 7 * 24 + 6, 100, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        g.centeredText(font, title, width / 2, 18, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        Config.save();
        minecraft.gui.setScreen(parent);
    }
}
