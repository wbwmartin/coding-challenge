import java.util.HashMap;
import java.util.Map;

/**
 * Database to save all the input data. Performed by using a hash map which costs
 * constant time complexity.
 * 
 * @author Bowen Wang
 * @date 08/21/2015
 */

public class Database {

	private static Map<String, Integer> map = new HashMap<String, Integer>();

	/**
	 * Set the variable name to the value value. Neither variable names nor
	 * values will contain spaces.
	 * 
	 * @param name
	 * @param value
	 */
	public static void set(String name, int value) {
		map.put(name, value);
	}

	/**
	 * Print out the value of the variable name, or NULL if that variable is not
	 * set.
	 * 
	 * @param name
	 * @return the value of the variable
	 */
	public static int get(String name) {
		if (!map.containsKey(name)) {
			return Integer.MIN_VALUE;
		} else {
			return map.get(name);
		}
	}

	/**
	 * Unset the variable name, making it just like that variable was never set.
	 * 
	 * @param name
	 */
	public static void unset(String name) {
		if (map.containsKey(name)) {
			map.remove(name);
		} else {
			System.out.println("Name does not exist.");
		}
	}

	/**
	 * Print out the number of variables that are currently set to value. If no
	 * variables equal that value, print 0. 
	 * 
	 * @param value
	 * @return the number of variables
	 */
	public static int numEqualTo(Integer value) {
		int count = 0;
		for (String key : map.keySet()) {
			if (map.get(key).equals(value)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * @return the size of database
	 */
	public static int size() {
		return map.size();
	}

	/**
	 * @param name
	 * @return whether a key exists in the database
	 */
	public static boolean containsKey(String name) {
		return map.containsKey(name);
	}

	/**
	 * @param value
	 * @return whether a value exists in the database
	 */
	public static boolean containsValue(Integer value) {
		return map.containsKey(value);
	}
}
