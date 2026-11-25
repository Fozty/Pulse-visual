package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ClientModInitializer {
    private static boolean menuOpen = false;
    private static KeyBinding menuKey;
    private float pulseTime = 0f;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.pulse.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.pulse"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                menuOpen = !menuOpen;
            }
        });

        HudRenderCallback.EVENT.register((DrawContext context, float tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return;

            pulseTime += 0.05f;
            float phase = (float) ((Math.sin(pulseTime) + 1) / 2);
            int width = mc.getWindow().getScaledWidth();
            int height = mc.getWindow().getScaledHeight();
            
            int radius = (int) (40 + 20 * phase);
            int alpha = (int) ((0.4 + 0.6 * phase) * 255);
            int color = (alpha << 24) | 0x00B4FF; 
            context.fill(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius, color);

            if (menuOpen) {
                context.fill(0, 0, width, height, 0x99000000);
                context.drawText(mc.textRenderer, "Pulse Menu (Right Shift)", 20, 20, 0xFFFFFF, true);
            }
        });
    }
												  }
