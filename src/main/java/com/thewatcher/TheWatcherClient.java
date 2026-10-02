package com.thewatcher;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.Random;

public class TheWatcherClient implements ClientModInitializer {

    private static final Random RANDOM = new Random();

    private int ticksUntilEvent = 600 + RANDOM.nextInt(1200);
    private int cooldown = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);

        TheWatcher.LOGGER.info("The Watcher client initialized.");
    }

    private void tick(MinecraftClient client) {

        if (client.player == null || client.world == null) {
            return;
        }

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        if (ticksUntilEvent > 0) {
            ticksUntilEvent--;
            return;
        }

        ticksUntilEvent = 900 + RANDOM.nextInt(1800);

        if (isNight(client)) {
            triggerWatcher(client);
        }
    }

    private boolean isNight(MinecraftClient client) {
        long time = client.world.getTimeOfDay() % 24000;

        return time >= 13000 && time <= 23000;
    }

    private void triggerWatcher(MinecraftClient client) {

        cooldown = 100;

        var player = client.player;

        // Creepy sound
        player.playSound(
                SoundEvents.ENTITY_ENDERMAN_STARE,
                0.8F,
                0.65F
        );

        // Short darkness effect
        player.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.DARKNESS,
                        35,
                        0,
                        false,
                        false,
                        false
                )
        );

        // Horror message
        client.inGameHud.setTitle(
                Text.literal("I SEE YOU.")
        );

        client.inGameHud.setTitleTicks(
                5,
                35,
                10
        );

        // Smoke around the player
        for (int i = 0; i < 18; i++) {

            double x = player.getX() + (RANDOM.nextDouble() - 0.5) * 5.0;
            double y = player.getY() + RANDOM.nextDouble() * 2.0;
            double z = player.getZ() + (RANDOM.nextDouble() - 0.5) * 5.0;

            client.world.addParticle(
                    ParticleTypes.SMOKE,
                    x,
                    y,
                    z,
                    0.0,
                    0.02,
                    0.0
            );
        }

        TheWatcher.LOGGER.info("The Watcher has noticed the player.");
    }
}
