class Puzzle {
	Square[][] board;
	boolean hasTheBoardChanged;

	Puzzle() {
		hasTheBoardChanged = false;
		board = new Square[9][9];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				board[i][j] = new Square();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (i < 3 			&& j < 3) 			board[i][j].region = Region.A;
				if (i < 3 			&& j >= 3 && j < 6) board[i][j].region = Region.B;
				if (i < 3 			&& j >= 6) 			board[i][j].region = Region.C;
				if (i >= 3 && i < 6 && j < 3) 			board[i][j].region = Region.D;
				if (i >= 3 && i < 6 && j >= 3 && j < 6) board[i][j].region = Region.E;
				if (i >= 3 && i < 6 && j >= 6) 			board[i][j].region = Region.F;
				if (i >= 6 			&& j < 3) 			board[i][j].region = Region.G;
				if (i >= 6 			&& j >= 3 && j < 6) board[i][j].region = Region.H;
				if (i >= 6 			&& j >= 6) 			board[i][j].region = Region.I;

			}
		}
		// end
	}

	void updateBlacklist() {
		hasTheBoardChanged = false;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int thisValue = board[i][j].value;
				if (thisValue != 0) {
					for (int k = 1; k < 10; k++)
						board[i][j].blacklist[k] = true;
					for (int k = 0; k < 9; k++) {
						board[i][k].blacklist[thisValue] = true;
						board[k][j].blacklist[thisValue] = true;
					}
					int x = (i / 3) * 3;
					int y = (j / 3) * 3;
					for (int i_0 = x; i_0 < x + 3; i_0++)
						for (int j_0 = y; j_0 < y + 3; j_0++)
							if (board[i_0][j_0].region == board[i][j].region)
								board[i_0][j_0].blacklist[thisValue] = true;
				}
			}
		}
	}

	void refillWithBlacklist() {
		// Normal blacklist checking of each square having only 1 possibility
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (board[i][j].value == 0 && board[i][j].hasOnePossibleNumber()) {
					board[i][j].value = board[i][j].getPossibleNumber();
					hasTheBoardChanged = true;
				}

		// For each region
		for (int x = 0; x < 9; x += 3) {
			for (int y = 0; y < 9; y += 3) {
				for (int k = 1; k < 10; k++) {
					int howManyCannotBeK = 0;
					int possibleK_x = -1;
					int possibleK_y = -1;
					for (int i = x; i < x + 3; i++)
						for (int j = y; j < y + 3; j++)
							if (board[i][j].blacklist[k])
								howManyCannotBeK++;
							else {
								possibleK_x = i;
								possibleK_y = j;
							}
					if (howManyCannotBeK == 8) {
						board[possibleK_x][possibleK_y].value = k;
						hasTheBoardChanged = true;
					}
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
