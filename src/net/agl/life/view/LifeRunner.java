package net.agl.life.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
//import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;

import net.agl.life.config.Config;
import net.agl.life.model.Life;
import net.agl.life.tools.Tools;

public class LifeRunner extends JComponent implements LifeView, FocusListener {
	private static final long serialVersionUID = 1L;

	/**
	 * cell width/height, in pixels
	 */
	private int cellSize = Config.cellsize;

	/**
	 * viewport left top corner offset, in pixels
	 */
	private Point offset = new Point(0, 0);

	/**
	 * current cell grid coord
	 */
	private Point current = null;

	/**
	 * selected area, grid coord
	 */
	private Rectangle selection;

	/**
	 * selected frame, pixels
	 */
	private Rectangle selectionFrame;

	private volatile Life life;

	@Override
	public int getCellSize() {
		return cellSize;
	}

	@Override
	public void setCellSize(int newSize) {
		Point mouse = getMousePosition();

		if(mouse != null) {
			offset.x = (int)Math.round(offset.x + ((mouse.x + offset.x) / (double)cellSize) * (newSize - cellSize));
			offset.y = (int)Math.round(offset.y + ((mouse.y + offset.y) / (double)cellSize) * (newSize - cellSize));
		}
		cellSize = newSize;

		repaint();
	}

	/**
	 * get viewport horizontal offset in pixels
	 */
	@Override
	public int getLeft() {
		return offset.x;
	}

	/**
	 * get viewport vertical offset in pixels
	 */
	@Override
	public int getTop() {
		return offset.y;
	}

	/**
	 * set viewport offset in pixels
	 * @param x - horizontal offset
	 * @param y - vertical offset
	 */
	@Override
	public void setOffset(int x, int y) {
		offset.setLocation(x, y);
		repaint();
	}

	/**
	 * shift viewport by dx/dy pixels relatively to current position
	 * @param dx - related horizontal offset
	 * @param dy - related vertical offset
	 */
	@Override
	public void shift(int dx, int dy) {
		offset.x += dx;
		offset.y += dy;
		repaint();
	}

	@Override
	public int getCol(int x) {
		x += offset.x;
		int add = (x < 0) ? 1 : 0;
		return (x + add) / cellSize - add;
	}

	@Override
	public int getRow(int y) {
		y += offset.y;
		int add = (y < 0) ? 1 : 0;
		return (y + add) / cellSize - add;
	}

	@Override
	public int getCellX(int col) {
		return (col * cellSize) - offset.x;
	}

	@Override
	public int getCellY(int row) {
		return (row * cellSize) - offset.y;
	}

	@Override
	public Rectangle getCellsRange(Rectangle frame) {
		return new Rectangle(getCol(frame.x), getRow(frame.y),
				frame.width / cellSize + (frame.width % cellSize > 0 ? 1 : 0),
				frame.height / cellSize + (frame.height % cellSize > 0 ? 1 : 0));
	}

	@Override
	public void setLife(Life life) {
		this.life = life;
		repaint();
	}

	@Override
	public void resetCursorCell() {
		if(current != null) {
			Point old = current;
			current = null;
			if(old != null)
				drawCell(old.x, old.y, getColorForCell(old.x, old.y), getGraphics());
		}
	}

	@Override
	public void updateCell(int col, int row) {
		drawCell(col, row, getColorForCell(col, row), getGraphics());
	}

	@Override
	public void updateSelection(final Rectangle selection) {
		if (selection == this.selection) // either rectangles are identical or
											// both are null
			return;
		Rectangle r;
		if (selection == null)
			r = this.selection;
		else if (this.selection == null)
			r = selection;
		else
			r = this.selection.union(selection);

		this.selection = new Rectangle(selection);

		repaintCells(r.x, r.y, r.width, r.height);
	}

	@Override
	public void updateSelectionFrame(final Rectangle frame) {
		if (frame == this.selectionFrame) // either rectangles are identical or
											// both are null
			return;
		Rectangle r;
		if (frame == null)
			r = this.selectionFrame;
		else if (this.selectionFrame == null)
			r = frame;
		else
			r = this.selectionFrame.union(frame);

		this.selectionFrame = new Rectangle(frame);

		Graphics g = getGraphics();
		g.setClip(r.x, r.y, Math.min(r.width, getWidth() - r.x), Math.min(r.height, getHeight() - r.y));
		paintComponent(g);
	}

	private void repaintCells(int cx, int cy, int width, int height) {
		int x = Math.max(getCellX(cx), 0), y = Math.max(getCellY(cy), 0);
		repaint(x, y, Math.min(width * cellSize, getWidth() - x), Math.min(height * cellSize, getHeight() - y));
	}

	public Rectangle getCells(Rectangle bounds) {
		return new Rectangle(bounds.x / cellSize + offset.x, bounds.y / cellSize + offset.y,
				(bounds.x + bounds.width - 1) / cellSize - bounds.x / cellSize + 1,
				(bounds.y + bounds.height - 1) / cellSize - bounds.y / cellSize + 1);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (life == null) {
			super.paintComponent(g);
			return;
		}

		Rectangle bounds = g.getClipBounds();

		// clean
		g.setColor(Config.colorDead);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		if (bounds != null && bounds.width > 0 && bounds.height > 0) {
			// draw grid
			g.setColor(Config.colorGrid);
			int bottom = bounds.y + bounds.height - 1;
			int right = bounds.x + bounds.width - 1;

			if (cellSize >= Config.gridMinSize) {
				for (int y = bounds.y / cellSize * cellSize - offset.y % cellSize; y <= bottom; y += cellSize)
					g.drawLine(bounds.x, y, right, y);
				for (int x = bounds.x / cellSize * cellSize - offset.x % cellSize; x <= right; x += cellSize)
					g.drawLine(x, bounds.y, x, bottom);
			}

			// draw cells
			int minX = Math.max(getCol(bounds.x), life.getLeft());
			int minY = Math.max(getRow(bounds.y), life.getTop());
			int maxX = Math.min(getCol(right), life.getRight());
			int maxY = Math.min(getRow(bottom), life.getBottom());

			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					Color color = getColorForCell(x, y);
					if (color != null && !color.equals(Config.colorDead))
						drawCell(x, y, color, g);
				}
			}
		}

		drawSelectionFrame(g);
	}

	private void drawSelectionFrame(Graphics g) {
		if (selectionFrame != null) {
			g.setColor(Config.colorSelectionFrame);
			g.drawRect(selectionFrame.x, selectionFrame.y, selectionFrame.width - 1, selectionFrame.height - 1);
			g.setColor(Config.colorSelection);
			g.fillRect(selectionFrame.x + 1, selectionFrame.y + 1, selectionFrame.width - 2, selectionFrame.height - 2);
		}
	}

	private void drawCell(int col, int row, Color color, Graphics g) {
		int x = getCellX(col);
		int y = getCellY(row);
		int cs = cellSize;
		if(cellSize >= Config.gridMinSize) {
			cs--;
			x++; y++;
		}

		if (g == null)
			g = getGraphics();
		if (!g.hitClip(x, y, cs, cs))
			return;

		g.setColor(color);
		g.fillRect(x, y, cs, cs);
		// Toolkit.getDefaultToolkit().sync();
	}

	private Color getColorForCell(int col, int row) {
		return getColorForCell(col, row, selectionFrame == null 
				&& current != null && current.x == col && current.y == row);
	}

	private Color getColorForCell(int col, int row, boolean cursor) {
		Color color = Config.colorDead;
		if (life.test(col, row))
			color = Config.colorLive;

		if (selection != null && selection.contains(col, row))
			color = Tools.colorFusion(color, Config.colorSelected);
		if (cursor) {
			// if(mmode == MM_INVERSE && life.test(col, row) || mmode ==
			// MM_KILL)
			// color = Utils.colorFusion(color, config.colorTraceKill);
			// else if(mmode == MM_NONE || mmode == MM_INVERSE || mmode ==
			// MM_BURN)
			color = Tools.colorFusion(color, Config.colorTrace);
		}

		return color;
	}

	/*
	 * private Rectangle getViewRect(Rectangle bounds) { Rectangle r =
	 * bounds.intersection(new Rectangle(-left, -top, getFieldWidth(),
	 * getFieldHeight())); return r; }
	 */

	@Override
	public void focusGained(FocusEvent arg0) {
		System.err.println("Focus gained");
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		System.err.println("Focus lost");
		grabFocus();
	}

/*
	@Override
	public Dimension getPreferredSize() {
		Dimension ps = super.getPreferredSize();

		int wd = life.getCols() * cellSize;
		int ht = life.getRows() * cellSize;

		ps.width = Math.max(ps.width, )
	}
*/

	@Override
	public void updateCursorCell(int col, int row) {
		if(current != null && current.x == col && current.y == row) {
			return;
		}

		if(current != null) {
			int oCol = current.x;
			int oRow = current.y;
			current.setLocation(col, row);
			drawCell(oCol, oRow, getColorForCell(oCol, oRow), getGraphics());
		} else {
			current = new Point(col, row);
		}

		drawCell(current.x, current.y, getColorForCell(current.x, current.y), getGraphics());
	}

	public boolean hasCursorCell() {
		return current != null;
	}

	public int getCursorCol() {
		if(current == null)
			return 0;
		return current.x;
	}

	public int getCursorRow() {
		if(current == null)
			return 0;
		return current.y;
	}

}
