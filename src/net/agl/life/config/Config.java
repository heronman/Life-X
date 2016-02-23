package net.agl.life.config;

import java.awt.Color;
import java.awt.Cursor;

import net.agl.life.model.Life;
import net.agl.life.tools.Tools;

public class Config {
	private Config() {
	}

	public static final String appName = "Game of Life";
	public static final String appIcon = "icons/app-icon.png";

	public static final Cursor curSelect = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR),
			curMoveReady = Tools.makeCursor("handopen", "cursor/openhand.gif", 6, 3),
			curMove = Tools.makeCursor("handgrab", "cursor/closedhand.gif", 8, 2),
			curCopyReady = Tools.makeCursor("handopen", "cursor/openhand-plus.gif", 6, 3),
			curCopy = Tools.makeCursor("handgrab", "cursor/closedhand-plus.gif", 8, 2), curBurn = null, // mkCursor("handgrab",
																										// "cursor/burn.gif",
																										// 8,
																										// 8),
			curKill = Tools.makeCursor("handgrab", "cursor/kill.gif", 8, 8);

	public static final int cellsize = 10;
	public static final int minCellsize = 1;
	public static final int maxCellsize = 30;
	public static final int gridMinSize = 3;
//	public static final int cols = 1000;
//	public static final int rows = 1000;
	public static final Life.Formula formula = Life.DEFAULT_FORMULA;
	public static final int timegap = 100;

	// public static final Color colorRulerBackground = Color.lightGray;
	public static final Color colorBackground = Color.black;
	public static final Color colorGrid = new Color(0, 30, 60);
	public static final Color colorLive = new Color(0, 100, 180);
	public static final Color colorDead = Color.black;
	// colors infused
	public static final Color colorTrace = new Color(255, 255, 0, 128);
	public static final Color colorTraceKill = new Color(255, 0, 0, 128);
	public static final Color colorSelected = new Color(0.0f, 0.7f, 0.7f, 0.5f);
	public static final Color colorSelection = new Color(0.0f, 0.5f, 0.5f, 0.3f);
	public static final Color colorSelectionFrame = new Color(0.0f, 0.7f, 0.7f, 0.5f);

	public static final int minCols = 50, minRows = 50;
}
