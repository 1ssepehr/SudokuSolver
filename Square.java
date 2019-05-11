class Square {
	int value;
	boolean[] blacklist;
	Region region;

	Square() {
		blacklist = new boolean[] {false, false, false, false, false, false, false, false, false, false};
		this.value = 0;
	}

	boolean hasOnePossibleNumber() {
		int counter = 0;
		for (int i = 1; i < 10; i++)
			if(blacklist[i])
				counter++;
		return (counter == 8);
	}

	int getPossibleNumber() {
		for(int i = 1; i < 10; i++)
			if(!blacklist[i])
				return i;
		return -1;
	}
}
