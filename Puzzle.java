class Puzzle {
	Square[][] board;
	boolean hasTheBoardChanged;

	Puzzle() {
		hasTheBoardChanged = false;
		board = new Square[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = new Square();
				if (i < 3 && j < 3) board[i][j].subGrid = subGrid.A;
				if (i < 3 && j >= 3 && j < 6) board[i][j].subGrid = subGrid.B;
				if (i < 3 && j >= 6) board[i][j].subGrid = subGrid.C;
				if (i >= 3 && i < 6 && j < 3) board[i][j].subGrid = subGrid.D;
				if (i >= 3 && i < 6 && j >= 3 && j < 6) board[i][j].subGrid = subGrid.E;
				if (i >= 3 && i < 6 && j >= 6) board[i][j].subGrid = subGrid.F;
				if (i >= 6 && j < 3) board[i][j].subGrid = subGrid.G;
				if (i >= 6 && j >= 3 && j < 6) board[i][j].subGrid = subGrid.H;
				if (i >= 6 && j >= 6) board[i][j].subGrid = subGrid.I;
			}
		}
	}

	void iterate() {
		hasTheBoardChanged = false;

		/*
		This part is for basic blacklist updating. Each number
		updates the blacklist of itself, its row, its column, and its subgrid.
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!isEmpty(i, j)) { 						// if a square has a number
					for (int k = 1; k < 10; k++)
						board[i][j].ban(k);    				// ban all from itself
					for (int k = 0; k < 9; k++) {    		// for everywhere else:
						board[k][j].ban(valueAt(i, j)); 	// ban it for the entire row
						board[i][k].ban(valueAt(i, j)); 	// ban it the entire column
					}
					int x = (i / 3) * 3;
					int y = (j / 3) * 3; 					// find the coordinates subgrid's start
					for (int m = x; m < x + 3; m++)
						for (int n = y; n < y + 3; n++)
							board[m][n].ban(valueAt(i, j));	// Ban it from the subgrid
				}
			}
		}

		// Normal blacklist checking of each square having only 1 possibility
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (board[i][j].howManyPossible() == 1)
					setValue(i, j, board[i][j].getPossibleNumber());

		for (int k = 1; k <= 9; k++) {

			// For each subgrid
			for (int x = 0; x < 9; x += 3) {
				for (int y = 0; y < 9; y += 3) {
					int count = 0;
					for (int i = x; i < x + 3; i++)
						for (int j = y; j < y + 3; j++)
							if (board[i][j].blacklist[k])
								count++;
					if (count == 8)
						for (int i = x; i < x + 3; i++)
							for (int j = y; j < y + 3; j++)
								if (!board[i][j].blacklist[k])
									setValue(i, j, k);
				}
			}

			// For each row
			for (int row = 0; row < 9; row++) {
				int count = 0;
				for (int column = 0; column < 9; column++)
					if (board[row][column].blacklist[k])
						count++;
				if (count == 8)
					for (int column = 0; column < 9; column++)
						if (!board[row][column].blacklist[k])
							setValue(row, column, k);
				if (count == 7) {
					int l = 0;
					while (board[row][l].blacklist[k]) l++;
					int first_col = l++;
					while (board[row][l].blacklist[k]) l++;
					int second_col = l;
					if (board[row][first_col].subGrid == board[row][second_col].subGrid)
						for (int m = (row / 3) * 3; m < (row / 3 + 1) * 3; m++)
							for (int n = (first_col / 3) * 3; n < (first_col / 3 + 1) * 3; n++)
								if (m != row && (n != first_col || n != second_col))
									board[m][n].ban(k);
				}
			}

			// For each column
			for (int column = 0; column < 9; column++) {
				int count = 0;
				for (int row = 0; row < 9; row++)
					if (board[row][column].blacklist[k])
						count++;
				if (count == 8)
					for (int row = 0; row < 9; row++)
						if (!board[row][column].blacklist[k])
							setValue(row, column, k);
				if (count == 7) {
					int l = 0;
					while (board[l][column].blacklist[k]) l++;
					int first_row = l++;
					while (board[l][column].blacklist[k]) l++;
					int second_row = l;
					if (board[first_row][column].subGrid == board[second_row][column].subGrid)
						for (int m = (first_row / 3) * 3; m < (first_row / 3 + 1) * 3; m++)
							for (int n = (column / 3) * 3; n < (column / 3 + 1) * 3; n++)
								if (n != column && (n != first_row || n != second_row))
									board[m][n].ban(k);
				}
			}
		}

	}

	boolean isComplete() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (board[i][j].value == 0)
					return false;
		return true;
	}

	private boolean isEmpty(int i, int j) {
		return board[i][j].isEmpty();
	}

	private int valueAt(int i, int j) {
		return board[i][j].value;
	}

	private void setValue(int i, int j, int value) {
		board[i][j].value = value;
		hasTheBoardChanged = true;
	}

	void display() {

		System.out.println("-------------------");

		for (int i = 0; i < 9; i++) {
			System.out.print("|");
			for (int j = 0; j < 9; j++) {
				System.out.print((board[i][j].value != 0) ? board[i][j].value : " ");
				System.out.print((j % 3 == 2) ? "|" : " ");
			}
			System.out.println((i % 3 == 2) ? "\n-------------------" : "");
		}
	}

}