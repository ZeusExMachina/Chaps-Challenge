package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: Like a free tile, but when Chap steps on this field, a help text will be displayed.
 */
public class HelpTile implements Tile {
	/**
	 * Help text to display
	 */
	private final String help;

	/**
	 * Construct a HelpTile with text
	 * @param h text to display
	 */
	public HelpTile(String h) {
		help = h;
	}

	/**
	 * Retrieve help text to display
	 * @return displayed help text
	 */
	public String getHelp() {
		return help;
	}

	@Override
	public boolean canMoveTo() {
		return true;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("help.png"));
	}
}
