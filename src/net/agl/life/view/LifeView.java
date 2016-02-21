package net.agl.life.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import net.agl.life.model.Life;

public interface LifeView {
	int getCellSize();

	int getLeft();

	int getTop();

	Point getOffset();

	int getFieldWidth();

	int getFieldHeight();

	Dimension getFieldSize();

	int getCol(int x);

	int getRow(int y);

	Rectangle getCellsRange(Rectangle frame);

	int getCellX(int col);

	int getCellY(int row);

	void setCellSize(int cellSize);

	void setOffset(Point p);

	void setOffset(int x, int y);

	void shift(int dx, int dy);

	void setLife(Life life);

	void addMouseListener(MouseListener listener);

	void addMouseMotionListener(MouseMotionListener listener);

	void addMouseWheelListener(MouseWheelListener listener);

	void repaint();

	void setCursor(Cursor cursor);

	void grabFocus();

	void addKeyListener(KeyListener listener);

	void updateSelection(final Rectangle selection);

	void updateSelectionFrame(final Rectangle frame);

	void updateCursorCell(int col, int row);

	void updateCell(int col, int row);
}
