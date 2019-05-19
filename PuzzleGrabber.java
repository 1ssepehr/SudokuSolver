import java.io.*;
import java.net.URL;

class PuzzleGrabber {
	static int[][] get(String puzzleID, String puzzleLevel) {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		String puzzleAllValues = "";
		String puzzleHideOrShow = "";

		try {
			String address = "https://show.websudoku.com/?level=" + puzzleLevel + "&set_id=" + puzzleID;
			url = new URL(address);
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				if (line.contains("cheat ID")) {
					int start = line.indexOf("VALUE") + 7;
					puzzleAllValues = line.substring(start, start + 81);
				}
				if (line.contains("editmask")) {
					int start = line.indexOf("VALUE") + 7;
					puzzleHideOrShow = line.substring(start, start + 81);
				}
			}
		} catch (IOException mue) {
			mue.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		int[][] puzzle = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int linearLocation = 9 * i + j;
				if ((int) (puzzleHideOrShow.charAt(linearLocation)) - (int) ('0') == 0) {
					puzzle[i][j] = (int) puzzleAllValues.charAt(linearLocation) - (int) ('0');
				} else {
					puzzle[i][j] = 0;
				}
			}
		}
		return puzzle;
	}
}