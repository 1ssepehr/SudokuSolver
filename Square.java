class Square {
	int value;
	boolean[] blacklist;
	subGrid subGrid;

	Square() {
		blacklist = new boolean[] {true, false, false, false, false, false, false, false, false, false};
		this.value = 0;
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
