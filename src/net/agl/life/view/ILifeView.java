package net.agl.life.view;

import java.awt.Rectangle;

import net.agl.life.controller.ILifeController;

public interface ILifeView {
	public ILifeController getController();
	public void setController(ILifeController controller);
	public void updateAll();
	public void updateCell(int col, int row);
	public void updateRange(int col, int row, int width, int height);
	public void updateRange(Rectangle r);
}
