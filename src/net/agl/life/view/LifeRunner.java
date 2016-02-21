package net.agl.life.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;

import net.agl.life.config.Config;
import net.agl.life.model.Life;
import net.agl.life.tools.Tools;

public class LifeRunner extends JPanel implements LifeView, FocusListener {
	private static final long serialVersionUID = 1L;

	private int cellSize = Config.cellsize;
	private int left = 0, top = 0;
	private int cellX = -1, cellY = -1;
	private Rectangle selection, selectionFrame;
	private volatile Life life;

	@Override
	public int getCellSize() {
		return cellSize;
	}

	@Override
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		repaint();
	}

	@Override
	public int getLeft() {
		return left;
	}

	@Override
	public int getTop() {
		return top;
	}

	@Override
	public Point getOffset() {
		return new Point(left, top);
	}

	@Override
	public void setOffset(Point p) {
		setOffset(p.x, p.y);
	}

	@Override
	public void setOffset(int x, int y) {
		left = x;
		top = y;
		repaint();
	}

	@Override
	public void shift(int dx, int dy) {
		left += dx;
		top += dy;
		repaint();
	}

	@Override
	public int getFieldWidth() {
		return getWidth();
		// return cellSize * life.getCols() + (cellSize < Config.gridMinSize ? 2
		// : 1);
	}

	@Override
	public int getFieldHeight() {
		return getHeight();
		// return cellSize * life.getRows() + (cellSize < Config.gridMinSize ? 2
		// : 1);
	}

	@Override
	public Dimension getFieldSize() {
		return new Dimension(getFieldWidth(), getFieldHeight());
	}

	@Override
	public int getCol(int x) {
		// return (x - 1 + left) / cellSize - (x <= 0 ? 1 : 0);
		int add = (x < 0) ? 1 : 0;
		return (x + add + left) / cellSize - add;
	}

	@Override
	public int getRow(int y) {
		// return (y - 1 + top) / cellSize - (y <= 0 ? 1 : 0);
		int add = (y < 0) ? 1 : 0;
		return (y + add + top) / cellSize - add;
	}

	@Override
	public Rectangle getCellsRange(Rectangle frame) {
		return new Rectangle(getCol(frame.x), getRow(frame.y),
				frame.width / cellSize + (frame.width % cellSize > 0 ? 1 : 0),
				frame.height / cellSize + (frame.height % cellSize > 0 ? 1 : 0));
	}

	@Override
	public int getCellX(int col) {
		// return (col + (col < 0 ? 1 : 0)) * cellSize + 1 - left;
		return (col * cellSize) - left;
	}

	@Override
	public int getCellY(int row) {
		// return (row + (row < 0 ? 1 : 0)) * cellSize + 1 - top;
		return (row * cellSize) - top;
	}

	@Override
	public void setLife(Life life) {
		if (life != null && this.life != null
				&& (life.getCols() != this.life.getCols() || life.getRows() != this.life.getRows())) {
			cellX = cellY = -1;
			selection = selectionFrame = null;
		}
		this.life = life;
		repaint();
	}

	@Override
	public void updateCursorCell(int col, int row) {
		if (col == cellX && row == cellY)
			return;
		int ox = cellX, oy = cellY;
		cellX = col;
		cellY = row;
		if (ox >= 0 && oy >= 0)
			this.drawCell(ox, oy, this.getColorForCell(ox, oy), getGraphics());
		if (cellX >= 0 && cellY >= 0)
			this.drawCell(cellX, cellY, this.getColorForCell(cellX, cellY), getGraphics());
	}

	@Override
	public void updateCell(int col, int row) {
		if (life.isValid(col, row)) {
			drawCell(col, row, this.getColorForCell(col, row), getGraphics());
		}
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

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300, 300);
	}
	/*
	 * @Override public void paintComponent(Graphics g) { if(life == null) {
	 * super.paintComponent(g); return; }
	 * 
	 * Rectangle bounds = g.getClipBounds();
	 * 
	 * // clean g.setColor(Config.colorDead); g.fillRect(bounds.x, bounds.y,
	 * bounds.width, bounds.height);
	 * 
	 * // clip bounds with visible part of the field bounds =
	 * getViewRect(bounds); if(bounds != null && bounds.width > 0 &&
	 * bounds.height > 0) { // draw grid g.setColor(Config.colorGrid); int
	 * bottom = bounds.y + bounds.height - 1; int right = bounds.x +
	 * bounds.width - 1;
	 * 
	 * if(cellSize < Config.gridMinSize) // while cellSize < gridMinSize, there
	 * is no grid, just a frame g.drawRect(-left, -top, getFieldWidth()-1,
	 * getFieldHeight()-1); else { for(int y = bounds.y / cellSize * cellSize -
	 * top % cellSize;y <= bottom;y += cellSize) g.drawLine(bounds.x, y, right,
	 * y); for(int x = bounds.x / cellSize * cellSize - left % cellSize;x <=
	 * right;x += cellSize) g.drawLine(x, bounds.y, x, bottom); }
	 * 
	 * // draw cells int minX = Math.max(0, getCol(bounds.x)); int minY =
	 * Math.max(0, getRow(bounds.y)); int maxX = Math.min(getCol(right),
	 * life.getCols()); int maxY = Math.min(getRow(bottom), life.getRows());
	 * 
	 * if(minX < 0 || maxX < 0 || minY < 0 || maxY < 0) return; for(int y =
	 * minY;y <= maxY;y ++) { for(int x = minX;x <= maxX;x ++) { Color color =
	 * getColorForCell(x, y); if(color != null &&
	 * !color.equals(Config.colorDead)) drawCell(x, y, color, g); } } }
	 * 
	 * drawSelectionFrame(g); }
	 */

	public Rectangle getCells(Rectangle bounds) {
		return new Rectangle(bounds.x / cellSize + left, bounds.y / cellSize + top,
				(bounds.x + bounds.width - 1) / cellSize - bounds.x / cellSize + 1,
				(bounds.y + bounds.height - 1) / cellSize - bounds.y / cellSize + 1);
	}

	@Override
	public void paintComponent(Graphics g) {
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
				for (int y = bounds.y / cellSize * cellSize; y <= bottom; y += cellSize)
					g.drawLine(bounds.x, y, right, y);
				for (int x = bounds.x / cellSize * cellSize; x <= right; x += cellSize)
					g.drawLine(x, bounds.y, x, bottom);
			}

			// draw cells
			int minX = Math.max(0, getCol(bounds.x));
			int minY = Math.max(0, getRow(bounds.y));
			int maxX = Math.min(getCol(right), life.getCols());
			int maxY = Math.min(getRow(bottom), life.getRows());

			if (minX < 0 || maxX < 0 || minY < 0 || maxY < 0)
				return;
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
		int cx = getCellX(col);
		int cy = getCellY(row);
		int cs = cellSize - (cellSize < Config.gridMinSize ? 0 : 1);

		if (g == null)
			g = getGraphics();
		if (!g.hitClip(cx, cy, cellSize, cellSize))
			return;

		g.setColor(color);
		g.fillRect(cx, cy, cs, cs);
		// Toolkit.getDefaultToolkit().sync();
	}

	private Color getColorForCell(int col, int row) {
		if (col < 0 || col >= life.getCols() || row < 0 || row >= life.getRows())
			return null;
		Color color = Config.colorDead;
		if (life.test(col, row))
			color = Config.colorLive;

		if (selection != null && selection.contains(col, row))
			color = Tools.colorFusion(color, Config.colorSelected);
		if (selectionFrame == null && cellX >= 0 && cellY >= 0 && cellX == col && cellY == row) {
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

}
