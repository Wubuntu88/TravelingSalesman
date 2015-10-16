import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class EfficientUniformCost {
	public class PathLengthComparator implements Comparator<byte[]> {

		@Override
		public int compare(byte[] path1, byte[] path2) {
			double path1Distance = 0.0;
			for (int i = 0; i <= path1.length - 2; i++) {// iterate until second
				// to last index
				String fromCityAsString = EfficientUniformCost.this.byteCitiesToStringCitesDict
						.get(path1[i]);
				String nextCityAsString = EfficientUniformCost.this.byteCitiesToStringCitesDict
						.get(path1[i + 1]);
				String[] fromCityComps = fromCityAsString.split("\t");
				String[] nextCityComps = nextCityAsString.split("\t");

				int fromCityX = Integer.parseInt(fromCityComps[0]);
				int fromCityY = Integer.parseInt(fromCityComps[1]);
				int nextCityX = Integer.parseInt(nextCityComps[0]);
				int nextCityY = Integer.parseInt(nextCityComps[1]);

				path1Distance += EfficientUniformCost.this.euclideanDistance(
						fromCityX, fromCityY, nextCityX, nextCityY);
			}

			double path2Distance = 0;
			for (int i = 0; i <= path2.length - 2; i++) {// iterate until second
				// to last index
				String fromCityAsString = EfficientUniformCost.this.byteCitiesToStringCitesDict
						.get(path2[i]);
				String nextCityAsString = EfficientUniformCost.this.byteCitiesToStringCitesDict
						.get(path2[i + 1]);
				String[] fromCityComps = fromCityAsString.split("\t");
				String[] nextCityComps = nextCityAsString.split("\t");

				int fromCityX = Integer.parseInt(fromCityComps[0]);
				int fromCityY = Integer.parseInt(fromCityComps[1]);
				int nextCityX = Integer.parseInt(nextCityComps[0]);
				int nextCityY = Integer.parseInt(nextCityComps[1]);

				path2Distance += EfficientUniformCost.this.euclideanDistance(
						fromCityX, fromCityY, nextCityX, nextCityY);
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

	public EfficientUniformCost() {
	}// force use of the alternate constructor.

	/**
	 * citiesToVisit includes the starting city
	 */
	public EfficientUniformCost(String startingCity,
			TreeSet<String> citiesToVisit) {
		this.startingCity = startingCity;
		this.pathQueue = new PriorityQueue<byte[]>(10,
				new PathLengthComparator());

		byte counter = 0;
		for (String city : citiesToVisit) {
			this.citiesToVisitAsByte.add(counter);
			this.byteCitiesToStringCitesDict.put(new Byte(counter), city);
			counter++;
			if (city.equals(startingCity)) {
				this.startingCityAsByte = counter;
			}
		}
	}

	private double euclideanDistance(int point1x, int point1y, int point2x,
			int point2y) {
		double xDiffSquared = Math.pow(point1x - point2x, 2);
		double yDiffSquared = Math.pow(point1y - point2y, 2);
		double distance = Math.sqrt(xDiffSquared + yDiffSquared);
		return distance;
	}

	public ArrayList<String> findOptimumPath() {

		byte[] initialPath = new byte[1];
		initialPath[0] = this.startingCityAsByte;

		this.pathQueue.add(initialPath);

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
					byte[] newPath = Arrays.copyOf(path, path.length + 1);
					newPath[newPath.length - 1] = city;
					this.pathQueue.add(newPath);
				}
			}
		}// end of while(! isHamiltonianPath() )

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
		for (byte cityInPath : path) {
			// System.out.print(cityInPath + ", ");
		}
		// System.out.println("cities: " + cities);

		TreeSet<Byte> citiesCopy = new TreeSet<Byte>(cities);

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
