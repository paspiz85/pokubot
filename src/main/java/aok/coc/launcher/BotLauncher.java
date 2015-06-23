package aok.coc.launcher;

import java.util.logging.Level;
import java.util.logging.Logger;

import aok.coc.exception.BotConfigurationException;
import aok.coc.exception.BotException;
import aok.coc.state.Context;
import aok.coc.state.StateIdle;

public class BotLauncher {

	protected final Logger logger = Logger.getLogger(getClass().getName());

	private boolean waitingForDcChecker = false;

	public void initialize() {
		Setup.initialize();
	}

	public boolean isWaitingForDcChecker() {
		return waitingForDcChecker;
	}

	private void loop(Context context) throws InterruptedException,
			BotException {
		Exception botException; // throw in case of timeout
		try {
			while (true) {
				if (Thread.interrupted()) {
					throw new InterruptedException(
							"BotLauncher is interrupted.");
				}
				context.handle();
			}
		} catch (InterruptedException e) {
			// either by dc checker
			if (context.isDisconnected()) {
				logger.info("Interrupted by dc checker.");
				context.setDisconnected(false);
				context.setWaitDone(false);
				return;
				// or by user
			} else {
				logger.info("Interrupted by user.");
				throw e;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			botException = e;
		}

		final long timeout = 10 * 60 * 1000;
		// wait for dc checker to wake me up
		synchronized (context) {
			while (!context.isWaitDone()) {
				long tBefore = System.currentTimeMillis();

				logger.info("Waiting for dc checker to wake me up...");
				this.waitingForDcChecker = true;

				// if user interrupts here while it is waiting, make sure
				// waitingForDcChecker is set to false
				context.wait(timeout);

				if (System.currentTimeMillis() - tBefore > timeout) {
					throw new BotException("Timed Out.", botException);
				}
			}
			context.setWaitDone(false);
		}
		this.waitingForDcChecker = false;
		logger.info("Woken up. Launching again...");
	}

	public void setup() throws BotConfigurationException, InterruptedException {
		// setup the bot
		Setup.setup();
	}

	public void start() throws InterruptedException, BotException {
		// state pattern
		Context context = new Context();

		// start daemon thread that checks if you are DC'ed etc
		logger.info("Starting disconnect detector...");
		Thread dcThread = new Thread(new DisconnectChecker(context,
				Thread.currentThread(), this), "DisconnectCheckerThread");
		dcThread.setDaemon(true);
		dcThread.start();

		try {
			while (true) {
				context.setState(StateIdle.instance());
				loop(context);
			}
		} finally {
			dcThread.interrupt();
			this.waitingForDcChecker = false;
			context.setWaitDone(false);
		}
	}

	public void tearDown() {
		// TODO Setup.tearDown();
	}
}
