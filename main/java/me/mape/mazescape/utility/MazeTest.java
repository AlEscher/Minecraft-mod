package me.mape.mazescape.utility;

public class MazeTest {

	private int max_x;
	private int max_y;
	private Coordinate startcoor;
	private int matrix[][];

	private class Coordinate {
		private int x;
		private int y;

		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public void print() {
			System.out.println("(" + x + "," + y + ")");
		}
	}

	
	public MazeTest(int x, int y, int length, int minLength) {
		this.max_y = y;
		this.max_x = x;

		this.matrix = new int[y][x];
		generateMainPath(length, minLength);
	}
	
	public int[][] getMatrix() {
		return matrix;
	}

	private boolean validateCoor(int x, int y) {
		if (x < max_x-1 && y < max_y-1 && x > 0 && y > 0 && matrix[y][x] == 0) {
			int nummainpath = matrix[y + 1][x] + matrix[y - 1][x] + matrix[y][x + 1] + matrix[y][x - 1];
			//System.out.println(nummainpath);
			if (nummainpath > 0)
				return false;
			else
				return true;
		}
		return false;
	}

	private void printmatrix() {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("[");
			for (int k = 0; k < matrix[i].length; k++) {
//				if(matrix[i][k] == 1) {
//					System.err.print(" " + matrix[i][k] + ",");
//				}
//				if(matrix[i][k] == 0) {
					System.out.print(" " + matrix[i][k] + ",");
				//}
			}
			System.out.println("]");
		}
	}

	private void generateMainPath(int lenght, int minLength) {
		int steps=0;
		int start = (int) (Math.random() * (max_x - 2));
		int current_x = 1 + start;
		int current_y = 1;
		Coordinate current = new Coordinate(current_x, current_y);
		startcoor=current;
		current.print();
		Coordinate last = null;
		for (int i = 0; i < lenght; i++) {
			if (Math.random() < 0.4) {
				if (Math.random() < 0.5 && validateCoor(current_x + 1, current_y)) {
					System.out.println("x + 1");
					current_x++;

				} else if (validateCoor(current_x - 1, current_y)) {
					System.out.println("x - 1");
					current_x--;
				}
			} else {
				if (Math.random() < 0.6 && validateCoor(current_x, current_y + 1)) {
					System.out.println("y + 1");
					current_y++;
				} else if (validateCoor(current_x, current_y - 1)) {
					System.out.println("y - 1");
					current_y--;
				}
			}
			if (current_x != current.getX() || current_y != current.getY()) {
				last = current;
				matrix[current.getY()][current.getX()] = 1;
				steps++;
				current = new Coordinate(current_x, current_y);

			}
			//current.print();
		}
		matrix[current.getY()][current.getX()]=3;
		if(steps<minLength) {
			matrix=new int [max_y][max_x];
			generateMainPath(lenght, minLength);
		}
	}

	public void generateSidePath() {
		int begin=(int) (Math.random()*6)+1;
		
	}
	
//	public static void main(String[] args) {
//		MazeTest m1 = new MazeTest(30, 30);
//		// m1.printmatrix();
//		m1.generateMainPath(300);
//		System.out.println("---------------------------------------------------");
//		m1.printmatrix();
//	}
}
