import java.util.Scanner;
public class Runner {

	public static void main(String[] args) {
		Scanner Console = new Scanner(System.in);
		System.out.print("Give the puzzle ID: ");
		String puzzleID = Console.nextLine();
		System.out.print("Give the difficulty (easy = 1, medium = 2, hard = 3, evil = 4): ");
		String puzzleLevel = Console.nextLine();

		Puzzle puzzle = new Puzzle(PuzzleGrabber.get(puzzleID, puzzleLevel));

		System.out.println("INITIAL PUZZLE");
		puzzle.display();
		System.out.println("PROCESSING...");

		long startTime = System.nanoTime();

		while (!puzzle.isComplete()) {
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
	}
}