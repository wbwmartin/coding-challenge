/**
 * Parse the input and perform its functions including setting/getting/unsetting
 * values, and get number of variables that equals to a certain value from
 * database. It also supports these functions performed in transactions, and
 * basic transaction functions including begin, rollback and commit as well.
 * 
 * @author Bowen Wang
 * @date 08/21/2015
 */

public class Parser {

	/**
	 * @param input
	 */
	public static void parse(String input) {
		String[] table = input.split("\\s+");
		String result = "";

		switch (table[0]) {

		case "SET":
			if (table.length != 3) {
				System.out.println("WRONG INPUT");
			} else if (Transaction.isAlive()) {
				Transaction.set(table[1], Integer.parseInt(table[2]));
			} else {
				Database.set(table[1], Integer.parseInt(table[2]));
			}
			break;

		case "GET":
			if (table.length != 2) {
				System.out.println("WRONG INPUT");
			} else if (Transaction.isAlive()) {
				if (Transaction.get(table[1]) == Integer.MIN_VALUE) {
					result = "NULL";
				} else {
					result = Transaction.get(table[1]) + "";
				}
			} else {
				if (Database.get(table[1]) == Integer.MIN_VALUE) {
					result = "NULL";
				} else {
					result = Database.get(table[1]) + "";
				}
			}
			System.out.println(result);
			break;

		case "UNSET":
			if (table.length != 2) {
				System.out.println("WRONG INPUT");
			} else if (Transaction.isAlive()) {
				Transaction.unset(table[1]);
			} else {
				Database.unset(table[1]);
			}
			break;

		case "NUMEQUALTO":
			if (table.length != 2) {
				System.out.println("WRONG INPUT");
			} else if (Transaction.isAlive()) {
				result = Transaction.numEqualTo(Integer.parseInt(table[1]))
						+ "";
			} else {
				result = Database.numEqualTo(Integer.parseInt(table[1])) + "";
			}
			System.out.println(result);
			break;

		case "END":
			System.exit(0);
			break;

		case "BEGIN":
			if (table.length != 1) {
				System.out.println("WRONG INPUT");
			} else {
				Transaction.begin();
			}
			break;

		case "ROLLBACK":
			if (table.length != 1) {
				System.out.println("WRONG INPUT");
			} else {
				Transaction.rollback();
			}
			break;

		case "COMMIT":
			if (table.length != 1) {
				System.out.println("WRONG INPUT");
			} else {
				Transaction.commit();
			}
			break;
			
		default:
			System.out.println("WRONG INPUT");
		}
	}
}
