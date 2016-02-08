package net.agl.life.model;

import java.awt.Dimension;

public interface Life {
	static final class Formula {
		public final int burn;
		public final int surviveMin;
		public final int surviveMax;

		public Formula(int burn, int surviveMin, int surviveMax) {
			this.burn = burn;
			this.surviveMin = surviveMin;
			this.surviveMax = surviveMax;
		}

		public Formula(Formula f) {
			this.burn = f.burn;
			this.surviveMin = f.surviveMin;
			this.surviveMax = f.surviveMax;
		}

		@Override
		public boolean equals(Object o) {
			if(o == this) return true;
			if(!(o instanceof Formula))
				return false;
			Formula f = (Formula) o;
			return f.burn == burn
					&& f.surviveMin == surviveMin
					&& f.surviveMax == surviveMax;
		}

		@Override
		public Formula clone() {
			return new Formula(this);
		}

		@Override
		public String toString() {
			return "Burn: " + burn + "; Survive: " + surviveMin + " - " + surviveMax;
		}

		@Override
		public int hashCode() {
			return (surviveMax << 16) | (surviveMin << 8) | burn;
		}
	}

	Formula getFormula();
	public void setFormula(Formula f);
	public void setFormula(int burn, int smin, int smax);

	public boolean[][] getData();
	public byte[] pack();
	public boolean unpack(byte[] data, int cols, int rows);

	public boolean isValid(int x, int y);
	public int getAlives();
	public Dimension getSize();
	public int getCols();
	public int getRows();
	public void setSize(int width, int height);

	public void clear();
	public boolean test(int x, int y);
	public void burn(int x, int y);
	public void kill(int x, int y);
	public Life turn();
}
