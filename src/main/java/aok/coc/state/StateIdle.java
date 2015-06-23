package aok.coc.state;

import aok.coc.util.RobotUtils;
import aok.coc.util.coords.Clickable;

public class StateIdle extends State {
	private static final StateIdle instance = new StateIdle();

	public static StateIdle instance() {
		return instance;
	}

	boolean reloading = false;

	private StateIdle() {
	}

	@Override
	public void handle(Context context) throws InterruptedException {
		State nextState = null;
		while (true) {
			logger.info("StateIdle");
			if (Thread.interrupted()) {
				throw new InterruptedException("StateIdle is interrupted.");
			}

			if (reloading) {
				logger.info("reloading...");
				Thread.sleep(2000);
				continue;
			}

			if (RobotUtils
					.isClickableActive(Clickable.BUTTON_WAS_ATTACKED_HEADLINE)
					|| RobotUtils
							.isClickableActive(Clickable.BUTTON_WAS_ATTACKED_OKAY)) {
				logger.info("Was attacked.");
				RobotUtils.leftClick(Clickable.BUTTON_WAS_ATTACKED_OKAY, 250);
			} else if (RobotUtils.isClickableActive(Clickable.BUTTON_ATTACK)) {
				nextState = StateMainMenu.instance();
				break;
			} else if (RobotUtils.isClickableActive(Clickable.BUTTON_NEXT)) {
				nextState = StateAttack.instance();
				break;
			} else if (RobotUtils
					.isClickableActive(Clickable.BUTTON_FIND_A_MATCH)) {
				nextState = StateFindAMatch.instance();
				break;
			}

			Thread.sleep(1000);
		}

		context.setState(nextState);
	}

	public boolean isReloading() {
		return reloading;
	}

	public void setReloading(boolean reloading) {
		this.reloading = reloading;
	}

}
