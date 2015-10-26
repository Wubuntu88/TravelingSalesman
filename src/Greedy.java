import java.util.ArrayList;
import java.util.TreeSet;


public class Greedy {
	String startingCity;
	TreeSet<String> citiesToVisit;
	
	@SuppressWarnings("unused")
	private Greedy(){}//force use of the alternate constructor.
	
	public Greedy(String startingCity, TreeSet<String> citiesToVisit) {
		this.startingCity = startingCity;
		this.citiesToVisit = citiesToVisit;
	}
	
	public ArrayList<String> findOptimumPath(){
		ArrayList<String> path = new ArrayList<>();
		path.add(startingCity);
		while(citiesToVisit.size() > 1){
			String nextCity = null;
			double distanceToNextCity = Double.MAX_VALUE;
			String currentCity = path.get(path.size() - 1);
			for(String city : citiesToVisit){
				if(city.equals(startingCity)){
					continue;
				}
				double distance = this.euclidianDistance(currentCity, city);
				if(distance < distanceToNextCity){
					nextCity = city;
					distanceToNextCity = distance;
				}
			}
			path.add(nextCity);
			citiesToVisit.remove(nextCity);
		}
		path.add(startingCity);
		
		return path;
	}
	
	private double euclidianDistance(String city1, String city2){
		String[] city1xy = city1.split("\t");
		String[] city2xy = city2.split("\t");
		int city1x = Integer.parseInt(city1xy[0]);
		int city1y = Integer.parseInt(city1xy[1]);
		int city2x = Integer.parseInt(city2xy[0]);
		int city2y = Integer.parseInt(city2xy[1]);
		double xDiffSquared = Math.pow(city1x - city2x, 2);
		double yDiffSquared = Math.pow(city1y - city2y, 2);
		double distance = Math.sqrt(xDiffSquared + yDiffSquared);
		return distance;
	}

}
