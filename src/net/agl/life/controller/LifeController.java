package net.agl.life.controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import net.agl.life.Application;
import net.agl.life.config.Config;
import net.agl.life.model.Life;
//import net.agl.life.model.LifeBBA;
import net.agl.life.model.LifeInfinity;
import net.agl.life.view.ILifeView;

public class LifeController implements ILifeController {

	private static final int
			MM_NONE = 0,
			MM_BURN = 1,
			MM_KILL = 2,
			MM_INV_READY = 3,
			MM_INVERSE = 4,
			MM_SELECT_READY = 5,
			MM_SELECT = 6,
			MM_DRAG_READY = 7,
			MM_DRAG = 8,
			MM_COPY_READY = 9,
			MM_COPY_SEL = 10,
			MM_MOVE_READY = 11,
			MM_MOVE_SEL = 12;

	// private ActionsController actionsController;
	// private ActionMap actions;
	private volatile Life life;
	// private LifeView view;
	// private LifeStatusBar app.statusBar;
	private Application app;

	private int cellSize = Config.cellsize;
	private Point current = null;
	private Point mouse = null;
	private int mode = MM_NONE;
	private int keyPressed = -1;
	private Rectangle selection, selectionFrame;

	public LifeController(Application app) {
		this.app = app;
		// life = new LifeBBA(Config.cols, Config.rows);
		life = new LifeInfinity();

		app.actionsController.addResponder(this);
		app.statusBar.labelCellSize.setText(String.valueOf(cellSize));

		app.runner.setLife(life);
		app.runner.addMouseListener(this);
		app.runner.addMouseMotionListener(this);
		app.runner.addMouseWheelListener(this);

		// app.runner.addKeyListener(this);
	}

	private void setCursor() {
		Cursor c = Config.curBurn;
		switch (mode) {
		case MM_NONE:
		case MM_BURN:
		case MM_INV_READY:
		case MM_INVERSE:
			// c = Config.curBurn;
			break;
		case MM_KILL:
			c = Config.curKill;
			break;
		case MM_SELECT_READY:
		case MM_SELECT:
			c = Config.curSelect;
			break;
		case MM_DRAG_READY:
		case MM_MOVE_READY:
			c = Config.curMoveReady;
			break;
		case MM_DRAG:
		case MM_MOVE_SEL:
			c = Config.curMove;
			break;
		case MM_COPY_READY:
			c = Config.curCopyReady;
			break;
		case MM_COPY_SEL:
			c = Config.curCopy;
			break;
		default:
			return;
		}
		app.runner.setCursor(c);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int newsize = Math.max(Config.minCellsize, Math.min(Config.maxCellsize, cellSize - e.getWheelRotation()));
		if (newsize == cellSize) {
			return;
		}
		cellSize = newsize;
		app.runner.setCellSize(cellSize);
		app.statusBar.labelCellSize.setText(String.valueOf(cellSize));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMoved(e, false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e, true);
	}

	public void mouseMoved(MouseEvent e, boolean dragging) {
		int col = app.runner.getCol(e.getX()),
				row = app.runner.getRow(e.getY());

		if (mode == MM_DRAG && mouse != null) {
			app.runner.shift(mouse.x - e.getX(), mouse.y - e.getY());
			mouse.setLocation(e.getX(), e.getY());
			return;
		}

		if(mouse != null)
			mouse.setLocation(e.getPoint());
		else
			mouse = e.getPoint();

		if (current == null || current.x != col || current.y != row) {
			if(current != null)
				current.setLocation(col, row);
			else
				current = new Point(col, row);

			if (mode == MM_BURN || mode == MM_KILL || mode == MM_INVERSE) {
				boolean b;
				if (mode == MM_BURN) {
					b = true;
				} else if (mode == MM_KILL) {
					b = false;
				} else {
					b = !life.test(col, row);
				}
				alterCell(col, row, b);
			}

			app.runner.updateCursorCell(col, row);
		}
		app.statusBar.labelCurrent.setText(current.x + ":" + current.y);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent e) {
		app.runner.resetCursorCell();
		app.statusBar.labelCurrent.setText("");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button == MouseEvent.BUTTON1) {
			if (mode == MM_NONE) {
				mode = MM_BURN;
			} else if (mode > 2 && (mode % 2) > 0) {
				mode++;
			} else
				return;
		} else if (button == MouseEvent.BUTTON3) {
			if (mode == MM_NONE) {
				mode = MM_KILL;
			} else
				return;
		}

		switch (mode) {
		case MM_BURN:
			life.burn(current.x, current.y);
			app.runner.updateCell(current.x, current.y);
			app.statusBar.labelAlives.setText(String.valueOf(life.getAlives()));
			break;
		case MM_KILL:
			life.kill(current.x, current.y);
			app.runner.updateCell(current.x, current.y);
			app.statusBar.labelAlives.setText(String.valueOf(life.getAlives()));
			break;
		}

		setCursor();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON2) {
			if (mode > MM_KILL && (mode % 2) == 0) {
				mode--;
				setCursor();
			} else if (mode == MM_BURN || mode == MM_KILL) {
				mode = MM_NONE;
				setCursor();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (keyPressed > 0 || mode > 0)
			return;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_CONTROL:
			if (selection != null && selection.contains(current.x, current.y))
				mode = MM_COPY_READY;
			else
				mode = MM_SELECT_READY;
			break;
		case KeyEvent.VK_ALT:
			mode = MM_INV_READY;
			break;
		case KeyEvent.VK_SHIFT:
			if (selection != null && selection.contains(current.x, current.y))
				mode = MM_MOVE_READY;
			break;
		case KeyEvent.VK_SPACE:
			mode = MM_DRAG_READY;
			break;
		default:
			return;
		}
		keyPressed = e.getKeyCode();
		setCursor();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == keyPressed) {
			keyPressed = 0;
			if (mode == MM_DRAG_READY || mode == MM_MOVE_READY || mode == MM_COPY_READY || mode == MM_SELECT_READY
					|| mode == MM_INV_READY) {
				mode = MM_NONE;
				setCursor();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void displayRunningState() {
		LifeAction start = (LifeAction) app.actions.get("start");
		LifeAction stop = (LifeAction) app.actions.get("stop");
		start.setEnabled(!running);
		stop.setEnabled(running);
		app.toolBar.btnStart.setVisible(!running);
		app.toolBar.btnStop.setVisible(running);
	}

	private volatile boolean running = false;

	public void actionStart(ActionEvent e) {
		Object o = e.getSource();
		System.out.println("Action start: " + o.getClass().getName());
		running = true;
		displayRunningState();
		Thread t = new Thread() {
			@Override
			public void run() {
				while (running) {
					step();
					try {
						Thread.sleep(Config.timegap);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

	public void actionStop(ActionEvent e) {
		running = false;
		displayRunningState();
	}

	public void actionStep(ActionEvent e) {
		step();
	}

	public void actionExit(ActionEvent e) {
		System.exit(0);
	}

	public void actionNew(ActionEvent e) {
		if(running) {
			running = false;
			displayRunningState();
		}
		life.clear();
		app.runner.setOffset(0, 0);
		current.x = app.runner.getCol(mouse.x);
		current.y = app.runner.getRow(mouse.y);
		app.statusBar.labelFieldSize.setText(life.getCols() + "x" + life.getRows());
		app.statusBar.labelFieldSize.validate();
		app.statusBar.labelAlives.setText(String.valueOf(life.getAlives()));
		app.statusBar.labelCounter.setText(String.valueOf(step));
		app.statusBar.labelCurrent.setText(current.x + ":" + current.y);
	}

	private int step = 0;

	private void step() {
		step++;
		life = life.turn();
		app.runner.setLife(life);
		app.statusBar.labelFieldSize.setText(life.getCols() + "x" + life.getRows());
		app.statusBar.labelFieldSize.validate();
		app.statusBar.labelAlives.setText(String.valueOf(life.getAlives()));
		app.statusBar.labelCounter.setText(String.valueOf(step));
	}

	private void alterCell(int col, int row, boolean state) {
		if (state)
			life.burn(col, row);
		else
			life.kill(col, row);
		app.statusBar.labelAlives.setText(String.valueOf(life.getAlives()));
	}

	////////////////////////////////////////////
	@Override
	public ILifeView getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setView(ILifeView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCellSize() {
		return cellSize;
	}

	@Override
	public boolean testCell(int col, int row) {
		return life.test(col, row);
	}

	@Override
	public Point getCursor() {
		return current;
	}

}
