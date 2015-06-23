package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.ui.Application;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Logging {

	public static void initialize() {
		try (InputStream inputStream = Application.class
				.getResourceAsStream("/logging.properties")) {
			LogManager.getLogManager().readConfiguration(inputStream);
		} catch (final IOException e) {
			Logger.getAnonymousLogger().severe(
					"Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}

	public static void close() {
		for (Handler h : Logger.getLogger("").getHandlers()) {
			h.close();
		}
	}

}
