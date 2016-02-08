package net.agl.life.view;

import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class LifeMenu extends JMenuBar {
	public LifeMenu(ActionMap actions) {
		JMenu m;

		add(m = new JMenu("File"));
		m.setMnemonic(KeyEvent.VK_F);
		m.add(new JMenuItem(actions.get("new")));
		m.add(new JMenuItem(actions.get("open")));
		m.add(new JMenuItem(actions.get("save")));
		m.add(new JMenuItem(actions.get("save-as")));
		m.addSeparator();
		m.add(new JMenuItem(actions.get("import")));
		m.add(new JMenuItem(actions.get("export")));
		m.addSeparator();
		m.add(new JMenuItem(actions.get("exit")));

		add(m = new JMenu("Edit"));
		m.setMnemonic(KeyEvent.VK_E);
		m.add(new JMenuItem(actions.get("copy")));
		m.add(new JMenuItem(actions.get("cut")));
		m.add(new JMenuItem(actions.get("paste")));
		m.addSeparator();
		m.add(new JMenuItem(actions.get("select-all")));
		m.add(new JMenuItem(actions.get("deselect")));

		add(m = new JMenu("Run"));
		m.setMnemonic(KeyEvent.VK_R);
		m.add(new JMenuItem(actions.get("start")));
		m.add(new JMenuItem(actions.get("stop")));
		m.add(new JMenuItem(actions.get("step")));

		add(m = new JMenu("Snapshots"));
		m.setMnemonic(KeyEvent.VK_S);
		m.add(new JMenuItem(actions.get("snapshot")));
		m.add(new JMenuItem(actions.get("snapshot-list")));
		m.add(new JMenuItem(actions.get("save-track")));
	}
}
