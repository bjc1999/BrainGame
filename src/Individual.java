import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;

public class Individual {

    private HashSet<Integer> nodeSet;
    private TreeMap<Integer, Neuron> treemap;
    private ArrayList<Integer> path;
    private int distance;
    private int time;
    private double fitness;
    private boolean goal;

    /**
     * Initializes individual that undergo mutation
     *
     * @param treemap the tree map of ID and Neuron
     * @param startID the start node of the Search
     * @param endID the end node of the Search
     * @param length the length of the previous generation path
     */
    public Individual(TreeMap<Integer, Neuron> treemap, int startID, int endID, int length) {
        // Create individual path
        this.path = new ArrayList<>();
        this.nodeSet = new HashSet<>();
        this.treemap = treemap;
        this.path.add(startID);
        nodeSet.add(startID);
        checkGoal(endID);
        int counter = 0;
        int lastRemovedNode = 0;
        if (!goal) {
            for (int i = 1; i < length; i++) {
                Neuron temp = treemap.get(path.get(i - 1));
                if (!temp.checkHaveNext(nodeSet)) {
                    if (i == 1) {
                        break;
                    }
                    if (counter > 10) {
                        break;
                    }
                    if (counter == 0) {
                        lastRemovedNode = path.get(path.size() - 1);
                    }
                    if (lastRemovedNode == path.get(path.size() - 1)) {
                        counter++;
                    }

                    this.path.clear();
                    nodeSet.clear();
                    path.add(startID);
                    nodeSet.add(startID);
                    i = 1;
                    temp = treemap.get(path.get(i - 1));
                }
                path.add(temp.getRandomNext(nodeSet));
                nodeSet.add(path.get(path.size() - 1));
                checkGoal(endID);
                if (goal) {
                    break;
                }
            }
        }
        checkGoal(endID);
        calculateTime();
        calculateDistance();
        calculateFitness();
    }

    /**
     * Initializes random individual
     *
     * @param treemap the tree map of ID and Neuron
     * @param startID the starting node
     */
    public Individual(TreeMap<Integer, Neuron> treemap, int startID) {
        this.treemap = treemap;
        this.path = new ArrayList<>();
        this.nodeSet = new HashSet<>();
        this.path.add(startID);
        this.nodeSet.add(startID);
        calculateTime();
        calculateDistance();
        calculateFitness();
    }

    /**
     * Retrieve total distance for this path
     *
     * @return total distance for this path
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Retrieve total time for this path
     *
     * @return total time for this path
     */
    public int getTime() {
        return time;
    }

    /**
     * Gets individual's path list
     *
     * @return the path list
     */
    public ArrayList<Integer> getPath() {
        return path;
    }

    public HashSet<Integer> getHashSet() {
        return nodeSet;
    }

    /**
     * Set individual's fitness
     *
     * @param fitness The individuals fitness
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Gets individual's fitness
     *
     * @return The individual's fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Get individual's goal condition
     *
     * @return the individual's goal boolean
     */
    public boolean getGoal() {
        return goal;
    }

    /**
     * Check individual's goal condition and update import
     * junit.framework.TestCase;
     *
     * @param ID the end node of the search
     * @return the condition of the path
     */
    public boolean checkGoal(int ID) {
        if (path.get(path.size() - 1) == ID) {
            goal = true;
        } else {
            goal = false;
        }
        return goal;
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < path.size(); i++) {
            output+=path.get(i) + "-";
        }
        output+=getGoal() + " Fitness: " + getFitness() + " Time: " + getTime() + " Distance: "+getDistance()+"\n\n";
        return output;
    }
    
    public String goalToString(){
        String output = "";
        for (int i = 0; i < path.size(); i++) {
            output+=path.get(i) + "-";
        }
        output+=getGoal() + "\nFitness: " + getFitness() + "\nTime: " + getTime() + "\nDistance: "+getDistance()+"\n\n";
        return output;
    }

    /**
     * Calculate the total time for the path
     */
    public void calculateTime() {
        if (path.size() > 1) {
            time = 0;
            for (int i = 1; i < path.size(); i++) {
                time += this.treemap.get(path.get(i - 1)).getTimeTo(path.get(i));
            }
        } else {
            time = 0;
        }
    }

    /**
     * Calculate the total distance for the path
     */
    public void calculateDistance() {
        if (path.size() > 1) {
            distance = 0;
            for (int i = 1; i < path.size(); i++) {
                distance += this.treemap.get(path.get(i - 1)).getDistanceTo(path.get(i));
            }
        }
    }

    /**
     * calculate the individual fitness value
     */
    public void calculateFitness() {
        if (getTime() == 0) {
            fitness = 0;
        } else {
            if(fitness==1)
                fitness = 1 / (Math.log10((double) getTime() + 0.5));
            else
                fitness = 1 / (Math.log10((double) getTime()));
            if (!getGoal()) {
                fitness = -1* (1 - fitness);
            }
        }       
    }
    
}
