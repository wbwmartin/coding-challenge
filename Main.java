import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Implement an in-memory database similar to Redis. It supports read and write
 * in database, and temporary transactions functionalities as well.
 * 
 * @author Bowen Wang
 * @date 08/21/2015
 */

public class Main {

	private static Scanner input;

	/**
	 * Supports reading from a file, or from user's interactive input.
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			while (true) {
				input = new Scanner(System.in);
				String res = input.nextLine();
				Parser.parse(res);
			}
		} else {
			try {
				input = new Scanner(new File(args[0]));
				while (input.hasNext()) {
					String res = input.nextLine();
					Parser.parse(res);
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
