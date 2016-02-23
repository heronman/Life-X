package net.agl.life.view;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

public class LifeStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;

	public final JLabel labelAlives;
	public final JLabel labelCurrent;
	public final JLabel labelCellSize;
	public final JLabel labelFieldSize;
	public final JLabel labelCounter;
	
	public LifeStatusBar () {
		super();
		setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		((FlowLayout)getLayout()).setVgap(3);
		((FlowLayout)getLayout()).setAlignment(FlowLayout.LEFT);

		labelAlives = new JLabel("999999");
		labelCurrent = new JLabel("9999:9999");
		labelCellSize = new JLabel("99:99");
		labelFieldSize = new JLabel("1000:1000");
		labelCounter = new JLabel("00000");
		labelAlives.setPreferredSize(labelAlives.getPreferredSize());
		labelCurrent.setPreferredSize(labelCurrent.getPreferredSize());
		labelCellSize.setPreferredSize(labelCellSize.getPreferredSize());
		labelFieldSize.setPreferredSize(labelFieldSize.getPreferredSize());
		labelCounter.setPreferredSize(labelCounter.getPreferredSize());
		labelAlives.setText("0");
		labelCurrent.setText("");
		labelCellSize.setText("0");
		labelFieldSize.setText("0:0");
		labelCounter.setText("0");

		JPanel tp = new JPanel();
		tp.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tp.add(new JLabel("Generation:"));
		tp.add(labelCounter);
		add(tp);

		tp = new JPanel();
		tp.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tp.add(new JLabel("Live cells:"));
		tp.add(labelAlives);
		add(tp);

		tp = new JPanel();
		tp.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tp.add(new JLabel("Cursor cell:"));
		tp.add(labelCurrent);
		add(tp);

		tp = new JPanel();
		tp.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tp.add(new JLabel("Cell size:"));
		tp.add(labelCellSize);
		add(tp);

		tp = new JPanel();
		tp.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tp.add(new JLabel("Field size:"));
		tp.add(labelFieldSize);
		add(tp);
	}
}
