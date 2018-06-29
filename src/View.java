import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.event.KeyEvent.*;

/**
 * Created by kimjisub on 2017. 6. 28..
 */
public class View extends Frame {

	final static int SCRX = 1000;
	final static int SCRY = 800;
	final static int SIZE = 10 * 2;
	final static double TAN15 = Math.tan(15);
	static float DIAG = 7f * 2;

	int index = 0;


	View() {
		super("3D engine (by Kimjisub)");


		setSize(SCRX, SCRY);
		setLayout(new FlowLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keyPress(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});


		setVisible(true);
	}


	void keyPress(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == VK_UP || code == VK_LEFT || code == VK_A) {
			if (index > 0)
				index--;
		} else if (code == VK_DOWN || code == VK_RIGHT || code == VK_D) {
			if (index < map.length - 1)
				index++;
		}
		repaint();

	}


	Matrix2 convert(int x, int y, int z) {
		return new Matrix2(x + z, y + z);
	}

	Matrix2 convert(int x, int y) {
		int xLoc = SCRX / 2 + x * SIZE - y * SIZE;
		int yLoc = SCRY * 2 / 3 + (int) (x * DIAG * TAN15) + (int) (y * DIAG * TAN15);

		return new Matrix2(xLoc, yLoc);
	}

	Matrix2 revert(int xLoc, int yLoc) {
		int a = SCRX / 2;
		int b = SIZE;
		int c = SCRY * 2 / 3;
		double d = DIAG * TAN15;
		int x = (int) ((-(a * d) - (b * c) + (b * yLoc) + (xLoc * d)) / (2 * b * d));
		int y = (int) (((a * d) - (b * c) + (b * yLoc) - (xLoc * d)) / (2 * b * d));

		return new Matrix2(x, y);
	}

	int[][][] map = {
		{
			{0,0,0, 2,0,0, 2,2,0, 0,2,0},
			{2,0,0, 2,2,0, 2,2,2, 2,0,2},
			{2,2,0, 0,2,0, 0,2,2, 2,2,2},
			{0,0,0, 2,0,0, 2,0,2, 0,0,2},
			{0,0,0, 0,2,0, 0,2,2, 0,0,2},
			{0,0,2, 2,0,2, 2,2,2, 0,2,2},
		},
		{
			{0,0,0, 0,2,0, 1,2,0, 1,5,0, 0,5,0, 0,7,0, 4,7,0, 4,0,0},
			{4,0,0, 4,7,0, 4,7,4, 4,4,4, 4,4,3, 4,2,3, 4,2,4, 4,0,4},
			{1,2,0, 1,5,0, 1,5,2, 1,2,2},//1
			{2,2,3, 2,4,3, 4,4,3, 4,2,3},//2
			{0,0,0, 0,2,0, 0,2,2, 0,0,2},//3
			{0,5,0, 0,7,0, 0,7,2, 0,5,2},//4
			{2,0,4, 2,2,4, 4,2,4, 4,0,4},//5
			{2,4,4, 2,7,4, 4,7,4, 4,4,4},//6
			{2,4,3, 2,4,4, 4,4,4, 4,4,3},//7
			{0,5,0, 0,5,2, 1,5,2, 1,5,0},//8
			{2,0,2, 0,0,2, 0,2,2, 1,2,2, 1,5,2, 0,5,2, 0,7,2, 2,7,2},//9
			{2,0,2, 2,7,2, 2,7,4, 2,4,4, 2,4,3, 2,2,3, 2,2,4, 2,0,4},//9
			{0,0,0, 4,0,0, 4,0,4, 2,0,4, 2,0,2, 0,0,2}//11
		},
		{
			{0,0,0, 4,0,0, 4,7,0, 0,7,0},
			{4,0,0, 4,0,4, 4,7,4, 4,7,0},
			{0,7,0, 0,7,4, 4,7,4, 4,7,0},
			{0,7,4, 4,7,4, 4,0,4, 3,0,4, 3,2,4, 0,5,4},
			{0,7,0, 0,7,4, 0,5,4, 0,2,1, 0,0,1, 0,0,0},
			{3,2,4, 0,5,4, 0,2,1},
			{0,0,1, 0,2,1, 3,2,4, 3,0,4},
			{0,0,0, 0,0,1, 3,0,4, 4,0,4, 4,0,0},
		},
	};


	public void paint(Graphics g) {
		g.drawString(index + "", 10, 50);

		for (int i = 0; i < map[index].length; i++) {
			int[] m = map[index][i];

			int length = m.length / 3;

			Matrix2[] mat = new Matrix2[length];
			for (int j = 0; j < length; j++) {
				mat[j] = convert(m[0 + j * 3], m[1 + j * 3], m[2 + j * 3]);
			}

			Matrix2[] loc = new Matrix2[length];
			for (int j = 0; j < length; j++) {
				loc[j] = convert(mat[j].x, mat[j].y);
			}

			int[] x = new int[length];
			int[] y = new int[length];
			for (int j = 0; j < length; j++) {
				x[j] = loc[j].x;
				y[j] = loc[j].y;
			}

			g.setColor(new Color(0xDDFFFFFF, true));
			g.fillPolygon(x, y, length);

			g.setColor(Color.black);
			g.drawPolygon(x, y, length);
		}

		//Matrix2 mat = convert(clickedPoint.x, clickedPoint.y);
		//g.fillOval(mat.x, mat.y,3 ,3);
	}
}
