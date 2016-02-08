package net.agl.life.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import net.agl.life.tools.Tools;

public class LifeAction implements Action {
	public static final int ICON_DEFAULT = 0;
	public static final int ICON_SELECTED = 1;
	public static final int ICON_PRESSED = 2;
	public static final int ICON_ROLLOVER = 3;
	public static final int ICON_DISABLED = 4;
	public static final int ICON_ROLLOVER_SELECTED = 5;
	public static final int ICON_DISABLED_SELECTED = 6;

	public static final String ICON_SELECTED_KEY = "SelectedIcon";
	public static final String ICON_PRESSED_KEY = "PressedIcon";
	public static final String ICON_ROLLOVER_KEY = "RolloverIcon";
	public static final String ICON_DISABLED_KEY = "DisabledIcon";
	public static final String ICON_ROLLOVER_SELECTED_KEY = "RolloverSelectedIcon";
	public static final String ICON_DISABLED_SELECTED_KEY = "DisabledSelectedIcon";
	
	public static final Map<String, String> KEYS;

	static {
		Map<String, String> keys = new HashMap<String, String> ();
		keys.put(Action.SHORT_DESCRIPTION, "toolTipText");
		keys.put(Action.ACTION_COMMAND_KEY, "actionCommand");
		keys.put(Action.MNEMONIC_KEY, "mnemonic");
		keys.put(Action.NAME, "text");
		keys.put(Action.DISPLAYED_MNEMONIC_INDEX_KEY, "displayedMnemonicIndex");
		keys.put(Action.LARGE_ICON_KEY, "icon");
		keys.put(Action.SMALL_ICON, "icon");
		keys.put(Action.ACCELERATOR_KEY, "accelerator");
		keys.put(Action.SELECTED_KEY, "selected");

		KEYS = Collections.unmodifiableMap(keys);
	}

	private PropertyChangeSupport changeSupport;

	private String actionCommand;
	private String text;
	private String toolTipText;
	private int mnemonic = 0;
	private int displayedMnemonicIndex = 0;
	private KeyStroke accelerator = null;
	private Icon[] icons = { null, null, null, null, null, null, null };
//	private Icon[] largeIcons = { null, null, null, null, null, null, null };
	private int iconTextGap = 5;
	private boolean enabled = true;
	private boolean visible = true;
	private boolean selected = false;
	private ButtonModel model = null;
	
	private ActionListener listener;

	public LifeAction() {
		changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(listener != null)
			listener.actionPerformed(e);
//		Application.app.runner.grabFocus();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean b) {
		if(enabled != b) {
			enabled = b;
			changeSupport.firePropertyChange("enabled", !enabled, enabled);
		}
	}

	@Override
	public Object getValue(String key) {
		String keyTranslated = KEYS.get(key);
		if(keyTranslated != null)
			key = keyTranslated;

		String mname = "get" + key;
		for(Method m : getClass().getMethods()) {
			Class<?>[] params = m.getParameterTypes();
			if(mname.equalsIgnoreCase(m.getName())
					&& (params == null || params.length == 0)) {
				try { return m.invoke(this); }
				catch(InvocationTargetException e) {
					throw new RuntimeException(e);
				} catch(IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("Method �et" + key.substring(0, 1).toUpperCase() + key.substring(1) + "() not found");
	}

	@Override
	public void putValue(String key, Object value) {
		String keyTranslated = KEYS.get(key);
		if(keyTranslated != null)
			key = keyTranslated;

		String mname = "get" + key;
		try {
		for(Method m : getClass().getMethods()) {
			Class<?>[] params = m.getParameterTypes();
			if(mname.equalsIgnoreCase(m.getName())
					&& params != null && params.length == 1) {
				Class<?> c = m.getParameterTypes()[0];
				if(c == boolean.class) c = Boolean.class;
				if(c == char.class) c = Character.class;
				if(c == byte.class) c = Byte.class;
				if(c == short.class) c = Short.class;
				if(c == int.class) c = Integer.class;
				if(c == long.class) c = Long.class;
				if(c.isAssignableFrom(value.getClass())) {
					m.invoke(this, value);
					return;
				}
			}
		} } catch(InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		throw new RuntimeException("Method set" + key.substring(0, 1).toUpperCase() + key.substring(1) + "(" + value.getClass().getCanonicalName() + ") not found");
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		String old = this.text;
		this.text = text;
		changeSupport.firePropertyChange(Action.NAME, old, text);
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void setActionCommand(String command) {
		String old = actionCommand;
		actionCommand = command;
		changeSupport.firePropertyChange(Action.NAME, old, command);
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String text) {
		String old = toolTipText;
		toolTipText = text;
		changeSupport.firePropertyChange(Action.SHORT_DESCRIPTION, old, text);
	}

	public int getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(int m) {
		int old = mnemonic;
		mnemonic = m;
		changeSupport.firePropertyChange(Action.MNEMONIC_KEY, old, m);
	}

	public int getDisplayedMnemonicIndex() {
		return displayedMnemonicIndex;
	}

	public void setDisplayedMnemonicIndex(int m) {
		int old = displayedMnemonicIndex;
		displayedMnemonicIndex = m;
		changeSupport.firePropertyChange(Action.DISPLAYED_MNEMONIC_INDEX_KEY, old, m);
	}

	public KeyStroke getAccelerator() {
		return accelerator;
	}

	public void setAccelerator(String key) {
		setAccelerator(KeyStroke.getKeyStroke(key));
	}

	public void setAccelerator(KeyStroke key) {
		KeyStroke old = accelerator;
		accelerator = key;
		changeSupport.firePropertyChange(Action.ACCELERATOR_KEY, old, key);
	}

	public Icon getIcon() {
		return icons[ICON_DEFAULT];
	}

	public void setIcon(Icon icon) {
		Icon old = icons[ICON_DEFAULT];
		icons[0] = icon;
		changeSupport.firePropertyChange(SMALL_ICON, old, icon);
	}

	public void setIcon(String path) {
		setIcon (new ImageIcon(Tools.loadImage(path)));
	}

	public Icon getSelectedIcon() {
		return icons[ICON_SELECTED];
	}

	public void setSelectedIcon(Icon icon) {
		Icon old = icons[ICON_SELECTED];
		icons[ICON_SELECTED] = icon;
		changeSupport.firePropertyChange(ICON_SELECTED_KEY, old, icon);
	}

	public void setSelectedIcon(String path) {
		setSelectedIcon (new ImageIcon(Tools.loadImage(path)));
	}

	public Icon getPressedIcon() {
		return icons[ICON_PRESSED];
	}

	public void setPressedIcon(Icon icon) {
		Icon old = icons[ICON_PRESSED];
		icons[ICON_PRESSED] = icon;
		changeSupport.firePropertyChange(ICON_PRESSED_KEY, old, icon);
	}

	public void setPressedIcon(String path) {
		setPressedIcon (new ImageIcon(Tools.loadImage(path)));
	}

	public Icon getRolloverIcon() {
		return icons[ICON_ROLLOVER];
	}

	public void setRolloverIcon(Icon icon) {
		Icon old = icons[ICON_ROLLOVER];
		icons[ICON_ROLLOVER] = icon;
		changeSupport.firePropertyChange(ICON_ROLLOVER_KEY, old, icon);
	}

	public void setRolloverIcon(String path) {
		setRolloverIcon (new ImageIcon(Tools.loadImage(path)));
	}

	public Icon getDisabledIcon() {
		return icons[ICON_DISABLED];
	}

	public void setDisabledIcon(Icon icon) {
		Icon old = icons[ICON_DISABLED];
		icons[ICON_DISABLED] = icon;
		changeSupport.firePropertyChange(ICON_DISABLED_KEY, old, icon);
	}

	public void setDisabledIcon(String path) {
		setDisabledIcon (new ImageIcon(Tools.loadImage(path)));
	}

	public Icon getRolloverSelectedIcon() {
		return icons[ICON_ROLLOVER_SELECTED];
	}

	public void setRolloverSelectedIcon(Icon icon) {
		Icon old = icons[ICON_ROLLOVER_SELECTED];
		icons[ICON_ROLLOVER_SELECTED] = icon;
		changeSupport.firePropertyChange(ICON_ROLLOVER_SELECTED_KEY, old, icon);
	}

	public void setRolloverSelectedIcon(String path) {
		setRolloverSelectedIcon (new ImageIcon(Tools.loadImage(path)));
	}

	public Icon getDisabledSelectedIcon() {
		return icons[ICON_DISABLED_SELECTED];
	}

	public void setDisabledSelectedIcon(Icon icon) {
		Icon old = icons[ICON_DISABLED_SELECTED];
		icons[ICON_DISABLED_SELECTED] = icon;
		changeSupport.firePropertyChange(ICON_DISABLED_SELECTED_KEY, old, icon);
	}

	public void setDisabledSelectedIcon(String path) {
		setDisabledSelectedIcon (new ImageIcon(Tools.loadImage(path)));
	}

	public int getIconTextGap() {
		return iconTextGap;
	}

	public void setIconTextGap(int gap) {
		int old = iconTextGap;
		iconTextGap = gap;
		changeSupport.firePropertyChange("iconTextGap", old, gap);
	}

	public boolean getVisible() {
		return visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean v) {
		if(visible != v) {
			visible = v;
			changeSupport.firePropertyChange("iconTextGap", !visible, visible);
		}
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean v) {
		if(selected != v) {
			selected = v;
			changeSupport.firePropertyChange("iconTextGap", !selected, selected);
		}
	}

	public ButtonModel getModel() {
		return model;
	}
	
	public void setModel(ButtonModel model) {
		ButtonModel old = this.model;
		this.model = model;
		changeSupport.firePropertyChange("model", old, model);
	}

	public ActionListener getActionListener() {
		return listener;
	}

	public void setActionListener(ActionListener listener) {
		this.listener = listener;
	}
}
