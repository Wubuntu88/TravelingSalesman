import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class TravelingSalesmanDemo {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("enter the number of cities: ");
		int numberOfCities = keyboard.nextInt();
		System.out.println();

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("cities.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		startingCity = lines.get(0);

		// note: I will only do a few cities to begin with for (String line :
		
		
		lines = lines.subList(0, numberOfCities); // took forever with 0, 13
		citiesToVisit.addAll(lines);
		long startTime = System.currentTimeMillis();
		//EffTestNoCross();
		//UnifCostNoCross();
		GreedyTest();
		long endTime = System.currentTimeMillis();
		System.out.println("Millis Elapsed:" + (endTime - startTime));
		keyboard.close();
	}
	
	public static void GreedyTest(){
		Greedy greedy = new Greedy(startingCity, citiesToVisit);
		ArrayList<String> path = greedy.findOptimumPath();
		for (String str : path) {
			System.out.println(str);
		}
	}

	public static void EffTestNoCross(){
		EffUnifCostNoCrossingPaths euc = new EffUnifCostNoCrossingPaths(startingCity, citiesToVisit);
		ArrayList<String> path = euc.findOptimumPath();
		
		for (String str : path) {
			System.out.println(str);
		}
	}
	
	public static void UnifCostNoCross(){
		UniformCostNoCross uc = new UniformCostNoCross(startingCity, citiesToVisit);
		PathDistPair pdp = uc.findOptimumPath();
		ArrayList<String> path = pdp.getPath();
		
		for (String str : path) {
			System.out.println(str);
		}
	}
	
	public static void EffTest() {
		
		EfficientUniformCost uc = new EfficientUniformCost(startingCity, citiesToVisit);
		ArrayList<String> path = uc.findOptimumPath();
		for (String str : path) {
			System.out.println(str);
		}		
	}

	static String startingCity;

	static TreeSet<String> citiesToVisit = new TreeSet<String>();
}
