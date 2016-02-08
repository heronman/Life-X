package net.agl.life.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class ActionsController implements ActionListener {

	private final HashSet<Object> responders = new HashSet<Object>();

	public ActionsController () {
	}

	public void addResponder(Object o) {
		if(o != this)
			responders.add(o);
	}

	public void removeResponder(Object o) {
		responders.remove(o);
	}

	private static String getActionMethodName(String cmd) {
		StringBuilder sb = new StringBuilder("action");
		boolean big = true;
		int l = cmd.length();
		for(int i = 0;i < l;i++) {
			char c = cmd.charAt(i);
			if(c == '-') {
				big = true;
				continue;
			}
			if(big) {
				c = Character.toUpperCase(c);
				big = false;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public boolean actionPerformed(Object o, String methodName, ActionEvent e) {
		try {
			Method method = o.getClass().getMethod(methodName, ActionEvent.class);
			if(method != null) {
				method.invoke(o, e);
				return true;
			}
		} catch(SecurityException
				| IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
		}
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String mname = getActionMethodName(e.getActionCommand());
		for(Object r : responders) {
			if(actionPerformed(r, mname, e))
				return;
		}
		if(!actionPerformed(this, mname, e)) {
			System.err.println("Method not found for action " + e.getActionCommand());
		}
	}

	public void actionStart(ActionEvent e) {
		System.out.println("Action \"start\" performed");
	}
}
