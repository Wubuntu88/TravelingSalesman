import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class EffUnifCostNoCrossingPaths {
	public class PathLengthComparator implements Comparator<byte[]> {

		@Override
		public int compare(byte[] path1, byte[] path2) {
			double path1Distance = 0.0;
			for (int i = 0; i <= path1.length - 2; i++) {// iterate until second
				// to last index
				String fromCityAsString = EffUnifCostNoCrossingPaths.this.byteCitiesToStringCitesDict
						.get(path1[i]);
				String nextCityAsString = EffUnifCostNoCrossingPaths.this.byteCitiesToStringCitesDict
						.get(path1[i + 1]);
				String[] fromCityComps = fromCityAsString.split("\t");
				String[] nextCityComps = nextCityAsString.split("\t");

				int fromCityX = Integer.parseInt(fromCityComps[0]);
				int fromCityY = Integer.parseInt(fromCityComps[1]);
				int nextCityX = Integer.parseInt(nextCityComps[0]);
				int nextCityY = Integer.parseInt(nextCityComps[1]);

				path1Distance += EffUnifCostNoCrossingPaths.this
						.euclideanDistance(fromCityX, fromCityY, nextCityX,
								nextCityY);
			}

			double path2Distance = 0;
			for (int i = 0; i <= path2.length - 2; i++) {// iterate until second
				// to last index
				String fromCityAsString = EffUnifCostNoCrossingPaths.this.byteCitiesToStringCitesDict
						.get(path2[i]);
				String nextCityAsString = EffUnifCostNoCrossingPaths.this.byteCitiesToStringCitesDict
						.get(path2[i + 1]);
				String[] fromCityComps = fromCityAsString.split("\t");
				String[] nextCityComps = nextCityAsString.split("\t");

				int fromCityX = Integer.parseInt(fromCityComps[0]);
				int fromCityY = Integer.parseInt(fromCityComps[1]);
				int nextCityX = Integer.parseInt(nextCityComps[0]);
				int nextCityY = Integer.parseInt(nextCityComps[1]);

				path2Distance += EffUnifCostNoCrossingPaths.this
						.euclideanDistance(fromCityX, fromCityY, nextCityX,
								nextCityY);
			}

			if (path1Distance < path2Distance) {
				return -1;
			} else if (path1Distance == path2Distance) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	byte startingCityAsByte;
	String startingCity;
	TreeSet<Byte> citiesToVisitAsByte = new TreeSet<Byte>();

	Hashtable<Byte, String> byteCitiesToStringCitesDict = new Hashtable<>();

	PriorityQueue<byte[]> pathQueue;
	long pathsRejected = 0;

	public EffUnifCostNoCrossingPaths() {
	}// force use of the alternate constructor.

	/**
	 * citiesToVisit includes the starting city
	 */
	public EffUnifCostNoCrossingPaths(String startingCity,
			TreeSet<String> citiesToVisit) {
		this.startingCity = startingCity;
		this.pathQueue = new PriorityQueue<byte[]>(10,
				new PathLengthComparator());

		byte counter = 0;
		for (String city : citiesToVisit) {
			this.citiesToVisitAsByte.add(counter);
			this.byteCitiesToStringCitesDict.put(new Byte(counter), city);
			if (city.equals(startingCity)) {
				this.startingCityAsByte = counter;
			}
			counter++;
		}
	}

	private double euclideanDistance(int point1x, int point1y, int point2x,
			int point2y) {
		double xDiffSquared = Math.pow(point1x - point2x, 2);
		double yDiffSquared = Math.pow(point1y - point2y, 2);
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

	/*
	 * private void testIntersects(){ boolean res1 = intersects(0, 0, 2, 2, 0,
	 * 1, 1, -1); boolean res2 = intersects(0, 0, 2, 2, 0, 0, 2, 2); boolean
	 * res3 = intersects(0, 0, 2, 2, 0, 0, 0, 2); if(res1){
	 * System.out.println("res1 intersects"); } if(res2){
	 * System.out.println("res2 intersects"); } if(res3){
	 * System.out.println("res3 intersects"); } }
	 * 
	 * public static void main(String[] args){ EffUnifCostNoCrossingPaths uc =
	 * new EffUnifCostNoCrossingPaths(); uc.testIntersects(); }
	 */
	public ArrayList<String> findOptimumPath() {

		byte[] initialPath = new byte[1];
		initialPath[0] = this.startingCityAsByte;

		this.pathQueue.add(initialPath);
		if (pathQueue.peek() == null) {
			System.out.println("peek is null");
		}

		while (!this.isHamiltonianCircuit(this.pathQueue.peek(),
				this.citiesToVisitAsByte)) {
			byte[] path = this.pathQueue.poll();
			ArrayList<Byte> thePathSoFar = new ArrayList<>();
			for (byte cityAsByte : path) {
				thePathSoFar.add(cityAsByte);
			}

			// starts will all of the cities to visit, then removes cities
			// already visited

			TreeSet<Byte> citiesYetToVisit = new TreeSet<>(
					this.citiesToVisitAsByte);
			citiesYetToVisit.removeAll(thePathSoFar);

			// if I have gone to all of the cities, I must return home (starting
			// city)
			if (citiesYetToVisit.size() == 0) {
				byte[] newPath = Arrays.copyOf(path, path.length + 1);
				newPath[newPath.length - 1] = this.startingCityAsByte;
				this.pathQueue.add(newPath);

			} else {// in this else, we go to other cities other than returning
				// home
				for (Byte city : citiesYetToVisit) {
					if (city.equals(this.startingCity)) {
						continue;
					}

					if (path.length <= 2) {
						byte[] newPath = Arrays.copyOf(path, path.length + 1);
						newPath[newPath.length - 1] = city;
						this.pathQueue.add(newPath);
						continue;
					} else {
						// extract points for line one of last city to next city
						String point1 = byteCitiesToStringCitesDict
								.get(path[path.length - 1]);
						String[] points1 = point1.split("\t");
						int x1 = Integer.parseInt(points1[0]);
						int y1 = Integer.parseInt(points1[1]);
						String point2 = byteCitiesToStringCitesDict.get(city);
						String[] points2 = point2.split("\t");
						int x2 = Integer.parseInt(points2[0]);
						int y2 = Integer.parseInt(points2[1]);

						boolean linesIntersected = false;
						for (int i = 0; i < path.length - 2; i++) {
							String point3 = byteCitiesToStringCitesDict
									.get(path[i]);
							String[] points3 = point3.split("\t");
							int x3 = Integer.parseInt(points3[0]);
							int y3 = Integer.parseInt(points3[1]);
							String point4 = byteCitiesToStringCitesDict
									.get(path[i + 1]);
							String[] points4 = point4.split("\t");
							int x4 = Integer.parseInt(points4[0]);
							int y4 = Integer.parseInt(points4[1]);

							if (Line2D.linesIntersect(x1, y1, x2, y2, x3, y3,
									x4, y4)) {
								pathsRejected++;
								linesIntersected = true;
								break;
							}
						}
						if (linesIntersected == false) {
							byte[] newPath = Arrays.copyOf(path,
									path.length + 1);
							newPath[newPath.length - 1] = city;
							this.pathQueue.add(newPath);
						}
					}
				}
			}
		}// end of while(! isHamiltonianPath() )
		System.out.println("items in queue: " + pathQueue.size());
		System.out.println("paths rejected: " + pathsRejected);
		byte[] path = this.pathQueue.poll();
		ArrayList<String> pathOfStringCities = new ArrayList<>();
		for (byte byteCity : path) {
			String cityAsString = this.byteCitiesToStringCitesDict
					.get(byteCity);
			pathOfStringCities.add(cityAsString);
		}
		return pathOfStringCities;
	}

	// will have to get ArrayList of strings for path
	public boolean isHamiltonianCircuit(byte[] path, TreeSet<Byte> cities) {

		TreeSet<Byte> citiesCopy = new TreeSet<Byte>(cities);
		if (path == null) {
			System.out.println("null");
		}
		List<Byte> pathAsList = new ArrayList<>();
		for (byte byteCity : path) {
			// String city = this.byteCitiesToStringCitesDict.get(bytePath);
			pathAsList.add(byteCity);
		}

		Byte startingCity = this.startingCityAsByte;
		int len = pathAsList.size();
		if (len != citiesCopy.size() + 1) {// a Hamiltonian path must visit all
			// of the
			// System.out.println("didn't visit all of the citis.");
			return false; // citiesToVisit, plus 1 for visiting the starting
			// city at the end
		} else if (startingCity.equals(pathAsList.get(len - 1)) == false) {
			return false;
		} else {
			citiesCopy.remove(startingCity);
			ArrayList<Byte> pathWithoutStartAndEnd = new ArrayList<Byte>(
					pathAsList.subList(1, len - 1));

			for (Byte city : pathWithoutStartAndEnd) {
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
				// all of the cities, and we return false.
			}
		}
		return true;// If we could not disprove it is a Hamiltonian path, it is.
	}
}
