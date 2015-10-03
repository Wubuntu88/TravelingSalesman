import java.util.ArrayList;

/*
 * the K is the path: ArrayList<String>??
 * the V is the distance: Double
 */
public class PathDistPair{//<K, V> {

    private final ArrayList<String> path;
    private final Double dist;
    /*
    public static <K, V> Pair<K, V> createPair(K element0, V element1) {
        return new Pair<K, V>(element0, element1);
    }
	*/
    public PathDistPair(ArrayList<String> path, Double dist) {
        this.path = path;
        this.dist = dist;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public Double getDist() {
        return dist;
    }
    
    public String toString(){
    		StringBuffer sb = new StringBuffer("");
    		sb.append("( ");
    		for(String item : path){
    			sb.append(item + ", ");
    		}
    		sb = new StringBuffer(sb.substring(0, sb.length() - 2));
    		sb.append(")");
    		sb.append(" -> " + dist.toString());
		return sb.toString();
    }

}












