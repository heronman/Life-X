package net.agl.life.view;

import java.awt.Insets;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JToolBar;

import net.agl.life.controller.LifeAction;

@SuppressWarnings("serial")
public class LifeToolBar extends JToolBar {

	public final JButton btnStart;
	public final JButton btnStop;
	public final JButton btnStep;
	public final JButton btnSnapshot;
	public final JButton btnPaste;
	public final JButton btnCopy;
	public final JButton btnCut;
	public final JButton btnResize;
	
	public static class ToolbarButton extends JButton {
		public ToolbarButton(Action action) {
			super(action);
			setFocusable(false);
			setText(null);
			if(action instanceof LifeAction) {
				setIconTextGap(((LifeAction)action).getIconTextGap());
				setVisible(((LifeAction)action).isVisible());
			}

		}
	};

	public LifeToolBar(ActionMap actions) {
		setFloatable(false);
		setMargin(new Insets(10, 5, 5, 5));

		add(btnStart = new ToolbarButton(actions.get("start")));
		add(btnStop = new ToolbarButton(actions.get("stop")));
		add(btnStep = new ToolbarButton(actions.get("step")));
		add(btnSnapshot = new ToolbarButton(actions.get("snapshot")));
		addSeparator();
		add(btnPaste = new ToolbarButton(actions.get("paste")));
		add(btnCopy = new ToolbarButton(actions.get("copy")));
		add(btnCut = new ToolbarButton(actions.get("cut")));
		addSeparator();
		add(btnResize = new ToolbarButton(actions.get("resize")));

	}
}
