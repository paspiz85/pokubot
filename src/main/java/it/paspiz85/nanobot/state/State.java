package it.paspiz85.nanobot.state;

import java.util.logging.Logger;

import aok.coc.exception.BotException;

public abstract class State {

	protected final Logger logger = Logger.getLogger(getClass().getName());

	public abstract void handle(Context context) throws BotException,
	InterruptedException;

}
