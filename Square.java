/**
 * A class to represent squares of a sudoku puzzle
 */
class Square {
	int value;
	/**
	 * The blacklist is the boolean list of the numbers that a square
	 * cannot be, based on other relative values in the puzzle
	 */
	boolean[] blacklist;
	Subgrid subgrid;

	Square() {
		this(0);
	}

	Square(int value) {
		blacklist = new boolean[] {true, false, false, false, false, false, false, false, false, false};
		this.value = value;
	}

	boolean isEmpty() {
		return (value == 0);
	}

	int howManyPossible() {
		int counter = 0;
		for (int i = 1; i < 10; i++)
			if(blacklist[i])
				counter++;
		return 9 - counter;
	}

	void ban(int n) {
		blacklist[n] = true;
	}

	int getPossibleNumber() {
		assert howManyPossible() == 1;
		for(int i = 1; i < 10; i++)
			if(!blacklist[i])
				return i;
		return -1;
	}
}
