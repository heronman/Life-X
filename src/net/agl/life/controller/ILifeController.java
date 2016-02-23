package net.agl.life.controller;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import net.agl.life.view.ILifeView;

public interface ILifeController extends MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {
	public ILifeView getView();
	public void setView(ILifeView view);
	public int getCellSize();
	public boolean testCell(int col, int row);
	public Point getCursor();
}
