import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Comparator;


public class TravelingSalesmanDemo {
	
	static String startingCity;
	static TreeSet<String> citiesToVisit = new TreeSet<String>();

	public static void main(String[] args) {
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("cities.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		startingCity = lines.get(10);
		
		//note: I will only do a few cities to begin with
		for(String line : lines.subList(8, 19)){//took forever with 0, 13
			citiesToVisit.add(line);
		}
		
		travSalesTest();

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
			System.out.println("is Hamiltonian path");
		}else{
			System.out.println("not Hamiltonian path");
		}
		
	}
}
