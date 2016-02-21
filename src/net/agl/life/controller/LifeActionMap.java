package net.agl.life.controller;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import net.agl.life.tools.Tools;

@SuppressWarnings("serial")
public class LifeActionMap extends ActionMap {
	private ActionListener listener;

	public LifeActionMap(final ActionListener listener) {
		this.listener = listener;
		initialize();
	}

	public LifeActionMap() {
		initialize();
	}

	public ActionListener getListener() {
		return listener;
	}

	public void setListener(ActionListener listener) {
		this.listener = listener;
		Object[] keys = keys();

		for (int i = 0; i < keys.length; i++) {
			Action a = get(keys[i]);
			if (a instanceof LifeAction) {
				((LifeAction) a).setActionListener(listener);
			}
		}
	}

	@Override
	public void put(Object key, Action action) {
		super.put(key, action);
		if (listener != null && action instanceof LifeAction) {
			((LifeAction) action).setActionListener(listener);
		}
	}

	private void initialize() {
		put("new", new LifeAction() {
			{
				setText("New");
				setToolTipText("Make new life field");
				setActionCommand("new");
				setAccelerator(KeyStroke.getKeyStroke("control N"));
				setMnemonic(KeyEvent.VK_N);
				setIcon(new ImageIcon(Tools.loadImage("icons/New document.png")));
			}
		});

		put("open", new LifeAction() {
			{
				setText("Open...");
				setToolTipText("Open file");
				setActionCommand("open");
				setAccelerator(KeyStroke.getKeyStroke("control O"));
				setMnemonic(KeyEvent.VK_O);
				setIcon(new ImageIcon(Tools.loadImage("icons/Open.png")));
			}
		});

		put("save", new LifeAction() {
			{
				setText("Save");
				setActionCommand("save");
				setAccelerator(KeyStroke.getKeyStroke("control S"));
				setMnemonic(KeyEvent.VK_S);
				// setEnabled(false);
				setIcon(new ImageIcon(Tools.loadImage("icons/Save.png")));
			}
		});

		put("save-as", new LifeAction() {
			{
				setText("Save as...");
				setToolTipText("Save in alternative file");
				setActionCommand("save-as");
				setAccelerator(KeyStroke.getKeyStroke("control shift S"));
				setMnemonic(KeyEvent.VK_A);
				setDisplayedMnemonicIndex(5);
				setIcon(new ImageIcon(Tools.loadImage("icons/Save as.png")));
			}
		});

		put("import", new LifeAction() {
			{
				setText("Import...");
				setActionCommand("import");
				setMnemonic(KeyEvent.VK_I);
			}
		});

		put("export", new LifeAction() {
			{
				setText("Export...");
				setActionCommand("export");
				setMnemonic(KeyEvent.VK_E);
			}
		});

		put("exit", new LifeAction() {
			{
				setText("Exit");
				setToolTipText("Exit life. This does not mean you will die. Maybe...");
				setActionCommand("exit");
				setMnemonic(KeyEvent.VK_X);
				setDisplayedMnemonicIndex(1);
				setAccelerator(KeyStroke.getKeyStroke("alt X"));
				setIcon(new ImageIcon(Tools.loadImage("icons/Exit.png")));
			}
		});

		put("copy", new LifeAction() {
			{
				setText("Copy");
				setToolTipText("Copy selected area to clipboard");
				setActionCommand("copy");
				setMnemonic(KeyEvent.VK_C);
				setAccelerator(KeyStroke.getKeyStroke("control C"));
				setEnabled(false);
				setIcon(new ImageIcon(Tools.loadImage("icons/Copy.png")));
			}
		});

		put("cut", new LifeAction() {
			{
				setText("Cut");
				setToolTipText("Copy and clean selected area");
				setActionCommand("cut");
				setMnemonic(KeyEvent.VK_T);
				setAccelerator(KeyStroke.getKeyStroke("control X"));
				setEnabled(false);
				setIcon(new ImageIcon(Tools.loadImage("icons/Cut.png")));
			}
		});

		put("paste", new LifeAction() {
			{
				setText("Paste");
				setToolTipText("Paste cells from clipboard");
				setActionCommand("paste");
				setMnemonic(KeyEvent.VK_P);
				setAccelerator(KeyStroke.getKeyStroke("control V"));
				setEnabled(false);
				setIcon(new ImageIcon(Tools.loadImage("icons/Paste.png")));
			}
		});

		put("delete", new LifeAction() {
			{
				setText("Delete");
				setToolTipText("Clear selected cells");
				setActionCommand("delete");
				setMnemonic(KeyEvent.VK_D);
				setAccelerator(KeyStroke.getKeyStroke("control V"));
				setEnabled(false);
				setIcon(new ImageIcon(Tools.loadImage("icons/Paste.png")));
			}
		});

		put("select-all", new LifeAction() {
			{
				setText("Select all");
				setActionCommand("select-all");
				setMnemonic(KeyEvent.VK_A);
				setAccelerator(KeyStroke.getKeyStroke("control A"));
			}
		});

		put("deselect", new LifeAction() {
			{
				setText("Deselect");
				setActionCommand("deselect");
				setMnemonic('d');
				setAccelerator(KeyStroke.getKeyStroke("control D"));
				setEnabled(false);
			}
		});

		put("snapshot", new LifeAction() {
			{
				setText("Take snapshot");
				setToolTipText("Take snapshot");
				setActionCommand("snapshot");
				setMnemonic('t');
				setIcon(new ImageIcon(Tools.loadImage("icons/Camera.png")));
			}
		});

		put("snapshot-list", new LifeAction() {
			{
				setText("Snapshots");
				setActionCommand("snapshot-list");
				setMnemonic('s');
			}
		});

		put("save-track", new LifeAction() {
			{
				setText("Save track");
				setActionCommand("save-track");
				setMnemonic('t');
			}
		});

		put("start", new LifeAction() {
			{
				setText("Start");
				setToolTipText("Start life");
				setActionCommand("start");
				setMnemonic('s');
				setIcon(new ImageIcon(Tools.loadImage("icons/Play.png")));
				setAccelerator(KeyStroke.getKeyStroke("alt ctrl S"));
			}
		});

		put("stop", new LifeAction() {
			{
				setText("Stop");
				setToolTipText("Stop running");
				setActionCommand("stop");
				setMnemonic('s');
				setIcon(new ImageIcon(Tools.loadImage("icons/Pause.png")));
				setAccelerator(KeyStroke.getKeyStroke("alt ctrl S"));
				setEnabled(false);
				setVisible(false);
			}
		});

		put("step", new LifeAction() {
			{
				setText("Step forward");
				setToolTipText("Make 1 step forward");
				setActionCommand("step");
				setMnemonic('t');
				setIcon(new ImageIcon(Tools.loadImage("icons/Next track.png")));
				setAccelerator(KeyStroke.getKeyStroke("alt shift S"));
			}
		});

		put("resize", new LifeAction() {
			{
				setText("Resize");
				setToolTipText("Change field size");
				setActionCommand("resize");
				setMnemonic('r');
				setIcon(new ImageIcon(Tools.loadImage("icons/Expand.png")));
			}
		});

		put("set-snapshot", new LifeAction() {
			{
				setText("Select");
				setToolTipText("Activate snapshot");
				setActionCommand("set-snapshot");
			}
		});
	}
}
