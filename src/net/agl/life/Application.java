package net.agl.life;

import java.awt.BorderLayout;

import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import net.agl.life.config.Config;
import net.agl.life.controller.ActionsController;
import net.agl.life.controller.LifeActionMap;
import net.agl.life.controller.LifeController;
import net.agl.life.tools.Tools;
import net.agl.life.view.LifeMenu;
import net.agl.life.view.LifeRunner;
import net.agl.life.view.LifeStatusBar;
import net.agl.life.view.LifeToolBar;

public class Application implements Runnable {
	// public static final Application app = new Application();

	public final LifeController controller;
	public final ActionsController actionsController;
	public final ActionMap actions;
	public final LifeRunner runner;
	public final LifeStatusBar statusBar;
	public final LifeToolBar toolBar;
	public final JMenuBar menuBar;
	public final JFrame frame;
	public final JPanel ui;

	public Application() {
		frame = new JFrame();
		actionsController = new ActionsController();
		actions = new LifeActionMap(actionsController);
		statusBar = new LifeStatusBar();
		statusBar.setFocusable(false);
		toolBar = new LifeToolBar(actions);
		toolBar.setFocusable(false);
		menuBar = new LifeMenu(actions);
		menuBar.setFocusable(false);
		runner = new LifeRunner();
		runner.setFocusable(true);
		controller = new LifeController(this);
		ui = new JPanel();
		ui.setFocusable(false);
	}

	private void createAndShowGUI() {
		frame.setContentPane(ui);
		ui.setLayout(new BorderLayout());
		ui.add(toolBar, BorderLayout.NORTH);
		ui.add(statusBar, BorderLayout.SOUTH);
		ui.add(runner, BorderLayout.CENTER);

		frame.setTitle(Config.appName);
		frame.setIconImage(Tools.loadImage(Config.appIcon));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);

		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setVisible(true);
		runner.requestFocusInWindow();

		runner.addKeyListener(controller);
	}

	@Override
	public void run() {
		createAndShowGUI();
	}
}
