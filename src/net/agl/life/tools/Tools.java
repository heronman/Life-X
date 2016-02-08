package net.agl.life.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

public class Tools {
	public static BufferedImage loadImage(String path) {
		ClassLoader cl = Tools.class.getClassLoader();
		try {
			URL iconurl = cl.getResource(path);
			if (iconurl != null)
				return ImageIO.read(iconurl);
			System.err.println("Can't find image: " + path);
		} catch (IOException ex) {
			System.err.println("Error loading image " + path + ": "
					+ ex.toString());
		}
		return null;
	}

	public static Cursor makeCursor(String name, String image, int x, int y) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = loadImage(image);
		int ht = img.getHeight(null);
		int wd = img.getWidth(null);
		Dimension dim = kit.getBestCursorSize(wd, ht);
		x = Math.round(x * (dim.width / (float) wd));
		y = Math.round(y * (dim.height / (float) ht));

		BufferedImage buffered = new BufferedImage(dim.width, dim.height,
				Transparency.TRANSLUCENT);
		buffered.getGraphics().drawImage(img, (dim.width - wd) / 2,
				(dim.height - ht) / 2, null);

		Cursor cur = kit.createCustomCursor(buffered, new Point(x, y), name);

		return cur;
	}

	public static Map<String, String> loadStrings(Locale locale) throws IOException {
		File f = new File("messages-" + locale.getCountry() + ".properties");
		if(!f.exists()) {
			throw new FileNotFoundException();
		}
		Map<String, String> strings = new HashMap<String, String> ();

		ClassLoader cl = Tools.class.getClassLoader();
		BufferedReader reader = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("messages-" + locale.getLanguage() + ".properties")));
		while(reader.ready()) {
			String s = reader.readLine();
			if(s == null) break;
			int p = s.indexOf("=");
			if(p < 1) {
				continue;
			}
			strings.put(s.substring(0, p).trim(), s.substring(p+1).trim());
		}
		reader.close();
	
		return strings;
	}

	public static Color colorFusion(Color bg, Color overlay) {
		float[] rgb1 = bg.getComponents(null);
		float[] rgb2 = overlay.getComponents(null);
		return new Color(rgb2[3] * rgb2[0] + (1.0f - rgb2[3]) * rgb1[0],
				rgb2[3] * rgb2[1] + (1.0f - rgb2[3]) * rgb1[1], rgb2[3]
						* rgb2[2] + (1.0f - rgb2[3]) * rgb1[2], 1.0f);
	}
}
