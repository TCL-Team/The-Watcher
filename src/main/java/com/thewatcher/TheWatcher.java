package com.thewatcher;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheWatcher implements ModInitializer {
    public static final String MOD_ID = "the-watcher";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("The Watcher is watching...");
    }
}
