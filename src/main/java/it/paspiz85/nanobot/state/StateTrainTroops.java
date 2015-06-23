package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.util.Config;
import aok.coc.util.RobotUtils;
import aok.coc.util.coords.Clickable;

public class StateTrainTroops extends State {
	private static StateTrainTroops instance = new StateTrainTroops();

	public static StateTrainTroops instance() {
		return instance;
	}

	private StateTrainTroops() {
	}

	@Override
	public void handle(Context context) throws InterruptedException {
		logger.info("StateTrainTroops");
		// first barracks must be opened at this point

		Clickable[] raxInfo = Config.instance().getRaxInfo();
		for (int currRax = 0; currRax < raxInfo.length; currRax++) {
			Clickable troop = raxInfo[currRax];

			if (troop != Clickable.BUTTON_RAX_NO_UNIT) {
				for (int i = 0; i < RobotUtils.random.nextInt(5) + 15; i++) {
					RobotUtils.leftClick(troop, 75);
				}
			}

			if (currRax < raxInfo.length - 1) {
				// select next rax
				RobotUtils.leftClick(Clickable.BUTTON_RAX_NEXT, 350);
			}
		}
		RobotUtils.leftClick(Clickable.BUTTON_RAX_CLOSE, 250);

		context.setState(StateMainMenu.instance());
		RobotUtils.sleepRandom(5000);
	}

}
