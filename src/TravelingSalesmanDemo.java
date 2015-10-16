import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
<<<<<<< HEAD

=======
>>>>>>> origin/master

public class TravelingSalesmanDemo {

	public static void main(String[] args) {
<<<<<<< HEAD
		//long maxMem = Runtime.getRuntime().maxMemory();
		//System.out.println(maxMem);
		//java -Xmx8g -jar travSales.jar
=======
>>>>>>> origin/master

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("cities.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		startingCity = lines.get(0);
<<<<<<< HEAD
		
		//note: I will only do a few cities to begin with
		for(String line : lines.subList(0, 14)){//took too much memory with 0, 15
			citiesToVisit.add(line);
		}
		long startMillis = System.currentTimeMillis();
		travSalesTest();
		long endMillis = System.currentTimeMillis();
		long elapsedMillis = endMillis - startMillis;
		System.out.println("elapsed Millis: " + elapsedMillis);
		
	}
	
	public static void travSalesTest(){
		UniformCost uc = new UniformCost(startingCity, citiesToVisit);
		PathDistPair bestPath = uc.findOptimumPath();
		ArrayList<String> path = bestPath.getPath();
		
		for(String str : path){
			System.out.println(str);
		}
		
		//System.out.println(bestPath);
		
	}
	
	public static void test(){
		TreeSet<String> cities = new TreeSet<String>();
		//cities.add("34\t63");
		cities.add("47\t1");
		//cities.add("c");
		//cities.add("d");
		//cities.add("e");
		ArrayList<String> path = new ArrayList<String>();
		path.add("34\t63");
		path.add("47\t1");
		path.add("34\t63");
		//path.add("d");
		//path.add("e");
		//path.add("a");
		UniformCost uc = new UniformCost("34\t63", cities);
		if(uc.isHamiltonianCircuit(path, cities)){
=======

		// note: I will only do a few cities to begin with for (String line :
		lines = lines.subList(0, 10); // took forever with 0, 13
		citiesToVisit.addAll(lines);

		travSalesTest();
	}

	public static void test() {
		TreeSet<String> citiesStr = new TreeSet<String>();
		TreeSet<Byte> cities = new TreeSet<Byte>();
		citiesStr.add("34\t63");
		cities.add((byte) 0);
		cities.add((byte) 1);
		cities.add((byte) 2);

		citiesStr.add("c");
		citiesStr.add("d");
		// cities.add("e");
		/*
		 * ArrayList<String> path = new ArrayList<String>(); path.add("34\t63");
		 * path.add("47\t1"); path.add("34\t63");
		 */
		// path.add("d");
		// path.add("e");
		// path.add("a");
		byte[] path = new byte[4];
		path[0] = 0;
		path[1] = 1;
		path[2] = 2;
		path[3] = 0;

		EfficientUniformCost uc = new EfficientUniformCost("34\t63", citiesStr);
		if (uc.isHamiltonianCircuit(path, cities)) {
>>>>>>> origin/master
			System.out.println("is Hamiltonian path");
		} else {
			System.out.println("not Hamiltonian path");
		}

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
