import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class Runner {

	public static void main(String[] args) {
		Puzzle puzzle = new Puzzle();
		String puzzlePath = new File("").getAbsolutePath();
		puzzlePath = puzzlePath.concat("\\src\\Hard.txt");
		File PuzzleFile = new File(puzzlePath);
		try {
			Scanner PuzzleReader = new Scanner(PuzzleFile);
			for (int i = 0; i < 9; i++)
				for (int j = 0; j < 9; j++)
					puzzle.board[i][j].value = PuzzleReader.nextInt();
			System.out.println("INITIAL PUZZLE");
			puzzle.display();
			System.out.println("PROCESSING...");
			long startTime = System.nanoTime();
			while(!puzzle.isComplete()) {
				puzzle.iterate();
				if (!puzzle.hasTheBoardChanged) {
					puzzle.display();
					System.out.println("STUCK :(");
					System.exit(0);
				}
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			int ms = (int) (duration / 1000000);
			puzzle.display();
			System.out.println("PUZZLE SOLVED IN " + ms + "ms.");
			PuzzleReader.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
	}
}