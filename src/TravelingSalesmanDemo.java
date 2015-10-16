import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TravelingSalesmanDemo {

	public static void main(String[] args) {


		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("cities.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		startingCity = lines.get(0);

		// note: I will only do a few cities to begin with for (String line :
		lines = lines.subList(0, 10); // took forever with 0, 13
		citiesToVisit.addAll(lines);

		travSalesTest();
	}


	public static void travSalesTest() {
		// UniformCost uc = new UniformCost(startingCity, citiesToVisit);
		// PathDistPair bestPath = uc.findOptimumPath();
		// ArrayList<String> path = bestPath.getPath();
		EfficientUniformCost euc = new EfficientUniformCost(startingCity,
				citiesToVisit);
		ArrayList<String> path = euc.findOptimumPath();
		for (String str : path) {
			System.out.println(str);
		}

	}

	static String startingCity;

	static TreeSet<String> citiesToVisit = new TreeSet<String>();
}
