package net.agl.life.model;

public interface Life {
	public static final Formula DEFAULT_FORMULA = new Formula(3, 2, 3);

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
			if (o == this)
				return true;
			if (!(o instanceof Formula))
				return false;
			Formula f = (Formula) o;
			return f.burn == burn && f.surviveMin == surviveMin && f.surviveMax == surviveMax;
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

	// settings
	Formula getFormula();
	public void setFormula(Formula f);
	public void setFormula(int burn, int smin, int smax);

	// export/import
	public boolean[][] getData();
	public void setData(boolean[][] data);
	public byte[] pack();
	public boolean unpack(byte[] data, int cols, int rows);

	// state
	public int getAlives();
	public int getCols();
	public int getRows();
	public int getMaxCols();
	public int getMaxRows();
	public int getTop();
	public int getLeft();
	public int getBottom();
	public int getRight();

	// service
	public void clear();
	public boolean test(int x, int y);
	public void burn(int x, int y);
	public void kill(int x, int y);
	public Life turn();
}
