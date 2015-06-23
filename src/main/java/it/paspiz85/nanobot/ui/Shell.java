package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.util.Logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import aok.coc.launcher.BotLauncher;

public class Shell {

	private static final Logger logger = Logger
			.getLogger(Shell.class.getName());

	public static void main(String[] args) {
		Logging.initialize();
		try {
			// run the bot
			BotLauncher launcher = new BotLauncher();
			launcher.initialize();
			launcher.setup();
			launcher.start();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		} catch (BotConfigurationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			System.exit(2);
		} catch (BotException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			System.exit(3);
		} finally {
			Logging.close();
		}
	}

}
