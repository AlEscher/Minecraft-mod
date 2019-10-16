package me.mape.mazescape.utility;

public class MazeTest {

	private int max_x;
	private int max_y;
	private Coordinate startcoor;
	private int matrix[][];
	private int mainPlength;
	private int minMainLength;
	private int mainSteps;

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

		public boolean equals(Coordinate c) {
			if (c.x == this.x && c.y == this.y)
				return true;
			return false;
		}
	}

	public MazeTest(int x, int y, int length, int minLength) {
		this.max_y = y;
		this.max_x = x;
		this.minMainLength = minLength;
		this.mainPlength = length;
		this.matrix = new int[y][x];

	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void emptyMatrix() {
		this.matrix = new int[max_y][max_x];
	}

	public boolean matrixIsEmpty() {
		for (int i = 0; i < matrix.length; i++)
			for (int k = 0; k < matrix[i].length; k++)
				if (matrix[i][k] != 0) {
					return false;
				}
		return true;
	}

	public void generateMaze() {
		generateMainPath(this.mainPlength, this.minMainLength);
		generateSidePaths();

	}

	private boolean validateCoor(int x, int y) {
		if (x < max_x - 1 && y < max_y - 1 && x > 0 && y > 0 && matrix[y][x] == 0) {
			int nummainpath = matrix[y + 1][x] + matrix[y - 1][x] + matrix[y][x + 1] + matrix[y][x - 1];
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

				System.out.print(" " + matrix[i][k] + ",");

			}
			System.out.println("]");
		}
	}

	private void generateMainPath(int lenght, int minLength) {
		int steps = 0;
		int start = (int) (Math.random() * (max_x - 2));
		int current_x = 1 + start;
		int current_y = 1;
		Coordinate current = new Coordinate(current_x, current_y);
		startcoor = current;
		Coordinate last = null;
		for (int i = 0; i < lenght; i++) {
			if (Math.random() < 0.4) {
				if (Math.random() < 0.5 && validateCoor(current_x + 1, current_y)) {
					current_x++;

				} else if (validateCoor(current_x - 1, current_y)) {
					current_x--;
				}
			} else {
				if (Math.random() < 0.6 && validateCoor(current_x, current_y + 1)) {
					current_y++;
				} else if (validateCoor(current_x, current_y - 1)) {
					current_y--;
				}
			}
			if (current_x != current.getX() || current_y != current.getY()) {
				last = current;
				if (steps == 0)
					matrix[current.getY()][current.getX()] = 4;
				else
					matrix[current.getY()][current.getX()] = 1;
				steps++;
				current = new Coordinate(current_x, current_y);

			}
		}
		matrix[current.getY()][current.getX()] = 3;
		if (steps < minLength) {
			matrix = new int[max_y][max_x];
			generateMainPath(lenght, minLength);
		}
		mainSteps = steps;
	}

	private Coordinate nextMain(Coordinate c) {
		int x = c.getX();
		int y = c.getY();
		Coordinate result = null;
		if (matrix[y + 1][x] == 1 || matrix[y + 1][x] == 3) {
			result = new Coordinate(x, y + 1);
		}
		if (matrix[y - 1][x] == 1 || matrix[y - 1][x] == 3) {
			result = new Coordinate(x, y - 1);
		}
		if (matrix[y][x + 1] == 1 || matrix[y][x + 1] == 3) {
			result = new Coordinate(x + 1, y);
		}
		if (matrix[y][x - 1] == 1 || matrix[y][x - 1] == 3) {
			result = new Coordinate(x - 1, y);
		}
		return result;
	}

	private Coordinate lastMain(Coordinate c) {
		int x = c.getX();
		int y = c.getY();
		Coordinate result = null;
		if (matrix[y + 1][x] == 5 || matrix[y + 1][x] == 4) {
			result = new Coordinate(x, y + 1);
		}
		if (matrix[y - 1][x] == 5 || matrix[y - 1][x] == 4) {
			result = new Coordinate(x, y - 1);
		}
		if (matrix[y][x + 1] == 5 || matrix[y][x + 1] == 4) {
			result = new Coordinate(x + 1, y);
		}
		if (matrix[y][x - 1] == 5 || matrix[y][x - 1] == 4) {
			result = new Coordinate(x - 1, y);
		}
		return result;
	}

 	public void generateSidePaths() {
		int wayDone = 0;
		Coordinate recent = startcoor;
		while (true) {
			int begin = (int) (Math.random() * 5) + 1;
			Coordinate nextPath1 = null;
			Coordinate nextPath2 = null;
			wayDone = wayDone + begin;
			if (wayDone >= mainSteps) {
				for (int i = 0; i < matrix.length; i++)
					for (int k = 0; k < matrix[i].length; k++)
						if (matrix[i][k] == 5) {
							matrix[i][k] = 1;
						}
				return;
			}
			for (; begin > 0; begin--) {
				recent = nextMain(recent);
				matrix[recent.getY()][recent.getX()] = 5;
			}
			if (matrix[recent.getY() + 1][recent.getX()] == 0)
				nextPath1 = new Coordinate(recent.getX(), recent.getY() + 1);
			if (matrix[recent.getY() - 1][recent.getX()] == 0) {
				if (nextPath1 == null)
					nextPath1 = new Coordinate(recent.getX(), recent.getY() - 1);
				nextPath2 = new Coordinate(recent.getX(), recent.getY() - 1);
			}
			if (matrix[recent.getY()][recent.getX() + 1] == 0) {
				if (nextPath1 == null)
					nextPath1 = new Coordinate(recent.getX() + 1, recent.getY());
				nextPath2 = new Coordinate(recent.getX() + 1, recent.getY());
			}
			if (matrix[recent.getY()][recent.getX() - 1] == 0) {
				if (nextPath1 == null)
					nextPath1 = new Coordinate(recent.getX() - 1, recent.getY());
				nextPath2 = new Coordinate(recent.getX() - 1, recent.getY());
			}
			if (nextPath1 == null) {

			} else {
				System.out.println("Possible SidePaths:");
				nextPath1.print();
				if (nextPath2 == null) {
					System.out.println("Trying generatin sidePath at: ");
					nextPath1.print();
					generateSidePath(nextPath1);
				} else {
					nextPath2.print();
					if (Math.random() < 0.5) {
						System.out.println("Trying generatin sidePath at: ");
						nextPath1.print();
						boolean tooShort = generateSidePath(nextPath1);
						if (!tooShort) {
							generateSidePath(nextPath2);
						}

					} else {
						System.out.println("Trying generatin sidePath at: ");
						nextPath1.print();
						boolean tooShort = generateSidePath(nextPath2);
						if (!tooShort) {
							generateSidePath(nextPath1);
						}
					}
				}
			}
		}
	}

	public boolean validateCoorS(int x, int y) {
		if (x < max_x - 1 && y < max_y - 1 && x > 0 && y > 0 && (matrix[y][x] == 0 || matrix[y][x] == 2)) {
			if (nextMain(new Coordinate(x, y)) == null && lastMain(new Coordinate(x, y)) == null) {
				return true;
				}
			else
				return false;
		}
		return false;
	}

	private boolean generateSidePath(Coordinate c) {
		Coordinate current = c;
		Coordinate last = null;
		int current_x = c.getX();
		int current_y = c.getY();
		int lenght = max_x * 2;
		int steps = 0;

		for (int i = 0; i < lenght; i++) {
			if (Math.random() < 0.4) {
				if (Math.random() < 0.5 && !current.equals(new Coordinate(current_x + 1, current_y)) && validateCoorS(current_x + 1, current_y)) {
					current_x++;
				} else if (!current.equals(new Coordinate(current_x - 1, current_y)) && validateCoorS(current_x - 1, current_y)) {
					current_x--;
				}
			} else {
				if (Math.random() < 0.6 && !current.equals(new Coordinate(current_x, current_y + 1)) && validateCoorS(current_x, current_y + 1)) {
					current_y++;
				} else if (!current.equals(new Coordinate(current_x, current_y - 1)) && validateCoorS(current_x, current_y - 1)) {
					current_y--;
				}
			}
			if (current_x != current.getX() || current_y != current.getY()) {
				last = current;
				matrix[current.getY()][current.getX()] = 2;
				steps++;
				current = new Coordinate(current_x, current_y);
				if (matrix[current.getY()][current.getX()] == 2) {
					System.out.println("Hit another sidepath");
					System.out.println("length of the path:" + steps);
					if (steps == 0) {
						System.out.println("Path too short!");
						return false;
					}
					return true;
				}
			}
		}
		System.out.println("length of the path:" + steps);
		if (steps == 0) {
			System.out.println("Path too short!");
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		MazeTest m1 = new MazeTest(30, 30, 300, 20);
		m1.generateMaze();
		// m1.printmatrix();
		System.out.println("---------------------------------------------------");
		m1.printmatrix();
		
	}
}
