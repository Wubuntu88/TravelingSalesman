import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class UniformCostNoCross {

	String startingCity;
	TreeSet<String> citiesToVisit;

	PriorityQueue<PathDistPair> pathQueue;
	long pathsRejected = 0;

	@SuppressWarnings("unused")
	private UniformCostNoCross() {
	}// force use of the alternate constructor.

	public UniformCostNoCross(String startingCity, TreeSet<String> citiesToVisit) {
		this.startingCity = startingCity;
		this.citiesToVisit = citiesToVisit;
		pathQueue = new PriorityQueue<PathDistPair>(10,
				new PathLengthComparator());
	}

	public PathDistPair findOptimumPath() {

		ArrayList<String> path = new ArrayList<String>();
		path.add(startingCity);
		PathDistPair initialPath = new PathDistPair(path, 0.0);

		pathQueue.add(initialPath);

		while (!isHamiltonianCircuit(pathQueue.peek().getPath(), citiesToVisit)) {
			PathDistPair pathAndDist = pathQueue.poll();

			ArrayList<String> thePathSoFar = pathAndDist.getPath();

			// starts will all of the cities to visit, then removes cities
			// already visited
			TreeSet<String> citiesYetToVisit = new TreeSet<>(citiesToVisit);
			citiesYetToVisit.removeAll(new TreeSet<String>(thePathSoFar));

			// if I have gone to all of the cities, I must return home (starting
			// city)
			if (citiesYetToVisit.size() == 0) {

				ArrayList<String> newPath = new ArrayList<String>(
						pathAndDist.getPath());
				String lastCityInPath = newPath.get(newPath.size() - 1);

				Double costSoFar = new Double(pathAndDist.getDist()
						.doubleValue());
				double extraCost = euclidianDistance(lastCityInPath,
						startingCity);
				costSoFar += extraCost;
				newPath.add(startingCity);
				PathDistPair newPathDistPair = new PathDistPair(newPath,
						costSoFar);
				pathQueue.add(newPathDistPair);

			} else {// in this else, we go to other cities other than returning
					// to the starting one
					// we make new path appending each remaining city to be
					// visited to the end of each new path.
					// then we calculate the distance to that new city and add
					// it to the total path cost of each path
					// then we push all of those paths onto the priority queue
					// and they will go on in order of distance
				for (String city : citiesYetToVisit) {
					if (city.equals(startingCity))
						continue;

					if (thePathSoFar.size() <= 2) {
						ArrayList<String> newPath = new ArrayList<String>(
								pathAndDist.getPath());
						String lastCityInPath = newPath.get(newPath.size() - 1);
						Double costSoFar = new Double(pathAndDist.getDist()
								.doubleValue());
						double extraCost = euclidianDistance(lastCityInPath,
								city);
						costSoFar += extraCost;
						newPath.add(city);
						PathDistPair newPathDistPair = new PathDistPair(
								newPath, costSoFar);

						pathQueue.add(newPathDistPair);
						continue;
					}
					// extract points for line one of last city to next city
					String point1 = thePathSoFar.get(thePathSoFar.size() - 1);
					String[] points1 = point1.split("\t");
					int x1 = Integer.parseInt(points1[0]);
					int y1 = Integer.parseInt(points1[1]);
					String point = city;
					String[] points2 = point.split("\t");
					int x2 = Integer.parseInt(points2[0]);
					int y2 = Integer.parseInt(points2[1]);

					boolean linesIntersected = false;
					for (int i = 0; i < thePathSoFar.size() - 2; i++) {
						String point3 = thePathSoFar.get(i);
						String[] points3 = point3.split("\t");
						int x3 = Integer.parseInt(points3[0]);
						int y3 = Integer.parseInt(points3[1]);
						String point4 = thePathSoFar.get(i + 1);
						String[] points4 = point4.split("\t");
						int x4 = Integer.parseInt(points4[0]);
						int y4 = Integer.parseInt(points4[1]);

						
						if (Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4,
								y4)) {
							pathsRejected++;
							linesIntersected = true;
							break;
						}

					}
					if (linesIntersected == false) {
						ArrayList<String> newPath = new ArrayList<String>(
								pathAndDist.getPath());
						String lastCityInPath = newPath.get(newPath.size() - 1);
						Double costSoFar = new Double(pathAndDist.getDist()
								.doubleValue());
						double extraCost = euclidianDistance(lastCityInPath,
								city);
						costSoFar += extraCost;
						newPath.add(city);
						PathDistPair newPathDistPair = new PathDistPair(
								newPath, costSoFar);

						pathQueue.add(newPathDistPair);
					}
				}
			}
		}// end of while(! isHamiltonianPath() )
		System.out.println("items in queue: " + pathQueue.size());
		System.out.println("paths rejected: " + pathsRejected);
		PathDistPair pdp = pathQueue.poll();
		return pdp;
	}

	public boolean isHamiltonianCircuit(ArrayList<String> path,
			TreeSet<String> cities) {
		TreeSet<String> citiesCopy = new TreeSet<String>(cities);
		String startingCity = path.get(0);
		int len = path.size();
		if (len != citiesCopy.size() + 1) {// a Hamiltonian path must visit all
											// of the
			return false; // citiesToVisit, plus 1 for visiting the starting
							// city at the end
		} else if (startingCity != path.get(len - 1)) {
			return false;
		} else {
			citiesCopy.remove(startingCity);
			ArrayList<String> pathWithoutStartAndEnd = new ArrayList<String>(
					path.subList(1, len - 1));
			for (String city : pathWithoutStartAndEnd) {
				if (citiesCopy.contains(city)) {
					citiesCopy.remove(city);
				} else {// if this else is executed, either I already visited
						// this city
					return false;// or it was never a city to begin with
				}
			}
			if (citiesCopy.size() != 0) {// if at the end the set of cities to
											// visit
				return false;// has not been exhausted, then we have not visited
								// all
								// of the cities, and we return false.

			}
		}
		return true;// If we could not disprove it is a Hamiltonian path, it is.
					// (I hope)
	}

	private double euclidianDistance(String city1, String city2) {
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

	private boolean intersects(int x1, int y1, int x2, int y2, int x3, int y3,
			int x4, int y4) {
		int det = (x1 - x2) * (y4 - y3) - (x4 - x3) * (y1 - y2);
		double a = ((x4 - x2) * (y4 - y3) - (x4 - x3) * (y4 - y2))
				/ (double) det;
		double b = ((x1 - x2) * (y4 - y2) - (x4 - x2) * (y1 - y2))
				/ (double) det;
		return (0 <= a && a <= 1 & 0 <= b && b <= 1);
	}

	public class PathLengthComparator implements Comparator<PathDistPair> {

		@Override
		public int compare(PathDistPair path1, PathDistPair path2) {
			double path2Dist = (double) path2.getDist();
			double path1Dist = (double) path1.getDist();
			if (path2Dist > path1Dist) {
				return -1;
			} else if (path2Dist == path1Dist) {
				return 0;
			} else {// if path2Dist < path1Dist
				return 1;
			}
		}
	}

}
