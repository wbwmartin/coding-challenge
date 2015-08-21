import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Transactions are performed in a temporary space, and only written in database
 * when commit() is called.
 * 
 * @author Bowen Wang
 * @date 08/21/2015
 */

public class Transaction {

	private static boolean isAlive = false;
	private static Map<String, LinkedList<Integer>> map = new HashMap<String, LinkedList<Integer>>();
	private static Set<String> set = new HashSet<String>();
	private static int transCount = 0;

	/**
	 * @return if there is an active transaction.
	 */
	public static boolean isAlive() {
		return isAlive;
	}

	/**
	 * Open a new transaction block. Transaction blocks can be nested; a BEGIN
	 * can be issued inside of an existing block.
	 */
	public static void begin() {
		transCount++;
		isAlive = true;
		setRest();
		set = new HashSet<String>();
	}

	/**
	 * Undo all of the commands issued in the most recent transaction block, and
	 * close the block. Print nothing if successful, or print NO TRANSACTION if
	 * no transaction is in progress.
	 */
	public static void rollback() {
		if (transCount < 1) {
			System.out.println("NO TRANSACTION");
		}
		transCount--;
		for (Map.Entry<String, LinkedList<Integer>> entry : map.entrySet()) {
			LinkedList<Integer> list = entry.getValue();
			list.removeFirst();
			if (list.isEmpty()) {
				map.remove(entry.getKey());
			} else {
				map.put(entry.getKey(), list);
			}
		}
	}

	/**
	 * Close all open transaction blocks, permanently applying the changes made
	 * in them. Print nothing if successful, or print NO TRANSACTION if no
	 * transaction is in progress.
	 */
	public static void commit() {
		if (transCount < 1) {
			System.out.println("NO TRANSACTION");
		}
		for (Map.Entry<String, LinkedList<Integer>> entry : map.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue().getFirst();
			if (value == null && Database.containsKey(key)) {
				Database.unset(key);
			}
			Database.set(key, value);
		}
		map = new HashMap<String, LinkedList<Integer>>();
		transCount = 0;
		isAlive = false;
	}

	/**
	 * Set the variable name to the value value. Neither variable names nor
	 * values will contain spaces. Performed in transaction.
	 * 
	 * @param name
	 * @param value
	 */
	public static void set(String name, Integer value) {
		if (!map.containsKey(name)) {
			LinkedList<Integer> list = new LinkedList<Integer>();
			list.addFirst(value);
			map.put(name, list);
			set.add(name);
			return;
		}
		LinkedList<Integer> list = map.get(name);
		list.addFirst(value);
		map.put(name, list);
	}

	/**
	 * Print out the value of the variable name, or NULL if that variable is not
	 * set. Performed in transaction.
	 * 
	 * @param name
	 * @return the value of the variable
	 */
	public static int get(String name) {
		if (map.containsKey(name)) {
			int res = map.get(name).getFirst();
			return res;
		}
		return Database.get(name);
	}

	/**
	 * Unset the variable name, making it just like that variable was never set.
	 * Performed in transaction.
	 * 
	 * @param name
	 */
	public static void unset(String name) {
		if (!map.containsKey(name) && !Database.containsKey(name)) {
			return;
		}
		set(name, Integer.MIN_VALUE);
	}

	/**
	 * Print out the number of variables that are currently set to value. If no
	 * variables equal that value, print 0. Performed in transaction.
	 * 
	 * @param value
	 * @return the number of variables
	 */
	public static int numEqualTo(Integer value) {
		int count = Database.numEqualTo(value);
		for (Map.Entry<String, LinkedList<Integer>> entry : map.entrySet()) {
			String key = entry.getKey();
			Integer head = entry.getValue().getFirst();
			if (Database.get(key) == value && head != value) {
				count--;
			}
			if (Database.get(key) != value && head == value) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Helper function to keep the value alive during each transaction.
	 */
	private static void setRest() {
		for (String key : map.keySet()) {
			if (!set.contains(key)) {
				Integer head = map.get(key).getFirst();
				LinkedList<Integer> list = map.get(key);
				list.addFirst(head);
				map.put(key, list);
			}
		}
	}

}
