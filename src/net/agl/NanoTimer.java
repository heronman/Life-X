package net.agl;

public class NanoTimer {
	private long start;

	public NanoTimer() {
		reset();
	}

	public long getNanoSec() {
		return System.nanoTime() - start;
	}

	public long getSec() {
		return getNanoSec() / 1000000000;
	}

	public long getMilliSec() {
		return getNanoSec() / 1000000;
	}

	public long getUltraSec() {
		return getNanoSec() / 1000;
	}

	public void reset() {
		start = System.nanoTime();
	}

	public long getStartedNano() {
		return start;
	}
}
