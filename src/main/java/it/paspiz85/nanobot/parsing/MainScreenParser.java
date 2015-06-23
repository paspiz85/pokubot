package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Robot;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public final class MainScreenParser extends AbstractParser {

	MainScreenParser() {
	}

	public Point findTrainButton() {
		BufferedImage image = Robot.instance()
				.screenShot(Area.BARRACKS_BUTTONS);
		Rectangle rectangle = findArea(image,
				getClass().getResource("train.png"));
		if (rectangle == null) {
			return null;
		}

		Point ret = rectangle.getLocation();
		ret.x += Area.BARRACKS_BUTTONS.getX1();
		ret.y += Area.BARRACKS_BUTTONS.getY1();
		return ret;
	}

}
