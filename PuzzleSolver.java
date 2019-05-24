class PuzzleSolver {
	private Square[][] board;

	/**
	 * This value is is helpful to see if we are stuck on a puzzle.
	 * That is, if the puzzle doesn't change after an iteration, then this program in incapable of solving it.
	 */
	boolean hasTheBoardChanged;

	PuzzleSolver(Square[][] board) {
		this.board = board;
		hasTheBoardChanged = false;
	}

	void iterate() {
		hasTheBoardChanged = false;

		/*
		This part is for basic blacklist updating. Each number
		updates the blacklist of itself, its row, its column, and its subgrid.
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!isEmpty(i, j)) {                        // if a square has a number K
					for (int k = 1; k < 10; k++)
						board[i][j].ban(k);                    // ban all from itself (since it can't be any of them)
					for (int k = 0; k < 9; k++) {            // for everywhere else:
						board[k][j].ban(valueAt(i, j));    // ban K for the entire row
						board[i][k].ban(valueAt(i, j));    // ban K for the entire column
					}
					int x = (i / 3) * 3;
					int y = (j / 3) * 3;                    // find the coordinates of its subgrid
					for (int m = x; m < x + 3; m++)
						for (int n = y; n < y + 3; n++)        // and for all squares of its subgrid
							board[m][n].ban(valueAt(i, j));    // ban K from the subgrid
				}
			}
		}

		// Normal blacklist checking of each square having only 1 possibility
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (board[i][j].howManyPossible() == 1)                // if it had only 1 option
					setValue(i, j, board[i][j].getPossibleNumber());    // put in the number

		for (int k = 1; k <= 9; k++) {        // This now checks the pattern of individual numbers in the entire puzzle
			// So for each number K from 1 through 9,

			for (int x = 0; x < 9; x += 3) {                    // For each subgrid,
				for (int y = 0; y < 9; y += 3) {
					int count = 0;
					for (int i = x; i < x + 3; i++)
						for (int j = y; j < y + 3; j++)
							if (board[i][j].blacklist[k])            // Count how many can't be K
								count++;
					if (count == 8)                                // If 8 of them can't be K, then the 9th one is K
						for (int i = x; i < x + 3; i++)
							for (int j = y; j < y + 3; j++)
								if (!board[i][j].blacklist[k])        // Find it,
									setValue(i, j, k);                // and put in K.
				}
			}


			for (int row = 0; row < 9; row++) {                     // For each row
				int count = 0;
				for (int column = 0; column < 9; column++)
					if (board[row][column].blacklist[k])            // Count how many can't be K
						count++;
				if (count == 8) 								// If 8 of them can't be K, then the 9th one is K
					for (int column = 0; column < 9; column++)
						if (!board[row][column].blacklist[k])        // Find it,
							setValue(row, column, k);                // and put in K.
				if (count == 7) {
					/* If 7 of them can't be K,
						there are special situations where we can get more information about the puzzle.
						If there are only two blank squares that can be K and they are in the same subgrid,
						that means that no other squares in that subgrid can be K. So we may mark extra blacklists.
					*/
					int l = 0;
					while (board[row][l].blacklist[k]) l++;        // So find the column of the first blank square
					int first_col = l++;
					while (board[row][l].blacklist[k]) l++;        // and the column of the second blank square
					int second_col = l;
					if (board[row][first_col].subgrid == board[row][second_col].subgrid)    // If they were in the same subgrid
						for (int m = (row / 3) * 3; m < (row / 3 + 1) * 3; m++)
							for (int n = (first_col / 3) * 3; n < (first_col / 3 + 1) * 3; n++)
								if (m != row && (n != first_col || n != second_col))        // Mark K as blacklist for
									board[m][n].ban(k);                                        // all squares of the subgrid
				}
			}

			for (int column = 0; column < 9; column++) {		// For each column
				int count = 0;
				for (int row = 0; row < 9; row++)
					if (board[row][column].blacklist[k])		// Count how many can't be K
						count++;
				if (count == 8) 								// If 8 of them can't be K, then the 9th one is K
					for (int row = 0; row < 9; row++)
						if (!board[row][column].blacklist[k])        // Find it,
							setValue(row, column, k);                // and put in K.
				if (count == 7) {
					/* If 7 of them can't be K,
						there are special situations where we can get more information about the puzzle.
						If there are only two blank squares that can be K and they are in the same subgrid,
						that means that no other squares in that subgrid can be K. So we may mark extra blacklists.
					*/
					int l = 0;
					while (board[l][column].blacklist[k]) l++;    // So find the row of the first blank square
					int first_row = l++;
					while (board[l][column].blacklist[k]) l++;    // and the column of the second blank square
					int second_row = l;
					if (board[first_row][column].subgrid == board[second_row][column].subgrid)    // If they were in the same subgrid
						for (int m = (first_row / 3) * 3; m < (first_row / 3 + 1) * 3; m++)
							for (int n = (column / 3) * 3; n < (column / 3 + 1) * 3; n++)
								if (n != column && (n != first_row || n != second_row))            // Mark K as blacklist for
									board[m][n].ban(k);                                            // all squares of the subgrid
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

	private boolean isEmpty(int i, int j) { // Checks if a square has number in it
		return board[i][j].isEmpty();
	}

	private int valueAt(int i, int j) {	// Gives the number value of a specific square
		return board[i][j].value;
	}

	private void setValue(int i, int j, int value) { // Sets the value in a specific square
									 				// Doing so marked the puzzle as changed
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