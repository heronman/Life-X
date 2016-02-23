package net.agl.life.view;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import net.agl.life.model.Life;

public interface LifeView {
	void setLife(Life life);

	int getCellSize();
	void setCellSize(int cellSize);
	int getLeft();
	int getTop();
	void setOffset(int x, int y);
	void shift(int dx, int dy);

	// graphics / grid coords conversion
	int getCol(int x);
	int getRow(int y);
	int getCellX(int col);
	int getCellY(int row);
	Rectangle getCellsRange(Rectangle frame);

	void addMouseListener(MouseListener listener);
	void addMouseMotionListener(MouseMotionListener listener);
	void addMouseWheelListener(MouseWheelListener listener);
	void addKeyListener(KeyListener listener);

	void repaint();
	void setCursor(Cursor cursor);
	void grabFocus();

	void updateSelection(final Rectangle selection);
	void updateSelectionFrame(final Rectangle frame);
	void resetCursorCell();
	void updateCursorCell(int col, int row);
	void updateCell(int col, int row);
}
