package net.agl.life.test;

import static org.junit.Assert.*;

import org.junit.Test;

import net.agl.life.model.Life;
import net.agl.life.model.LifeInfinity;

public class LifeInfinityTest {

	private Life create() {
		return create(10, 10);
	}

	private Life create(int wd, int ht) {
		return new LifeInfinity();
	}

	private void burn(Life life) {
		/*
		 * 
		 * 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1
		 * 1 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
		 * 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
		 * 
		 */

		life.burn(3, 3);
		life.burn(4, 3);
		life.burn(5, 3);
		life.burn(3, 4);
		life.burn(5, 4);
		life.burn(3, 5);
		life.burn(5, 5);
	}

	@Test
	public void testCreate() {

		Life life = create();

		assertTrue("Life object is inconditional", life.getCols() == 0 && life.getRows() == 0 && life.getAlives() == 0);
	}

	@Test
	public void testBurn() {

		Life life = create();
		burn(life);

		assertTrue(life.getAlives() == 7);
	}

	@Test
	public void testTurn() {

		Life life = create();
		burn(life);
		life = life.turn().turn(); // make 2 generations

		try {
			boolean[][] ld = life.getData();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		}

		boolean[][] controlMatrix = new boolean[10][10];
		controlMatrix[2][4] = controlMatrix[3][2] = controlMatrix[3][3] = controlMatrix[4][2] = controlMatrix[4][3] = controlMatrix[3][5] = controlMatrix[3][6] = controlMatrix[4][5] = controlMatrix[4][6] = true;

		for (int y = 0; y < controlMatrix.length; y++) {
			for (int x = 0; x < controlMatrix.length; x++) {
				assertEquals(life.test(x, y), controlMatrix[y][x]);
			}
		}
	}

	@Test
	public void testPack() {

		Life life = create(4, 2);
		life.burn(1, 0);
		life.burn(3, 0);
		life.burn(0, 1);

		byte[] pack = life.pack();
		assertTrue("packed: " + (int) pack[0], pack[0] == 26);
	}
}
