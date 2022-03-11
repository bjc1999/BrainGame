public class Node {
    private int parent ;
    private int id ;
    private double heuristic; //heuristic value
    
    /**Constructor for BreadthFirstSearch
     * @param parent of the node added
     * @param id of the node added
     */
    public Node(int parent, int id) {
        this.parent = parent;
        this.id = id;
    }
    
    /**Constructor for BestFirstSearch
     * @param parent of the node added
     * @param id of the node added
     * @param heuristic is the heuristic value
     */
    public Node(int parent, int id, double heuristic) {
        this.parent = parent;
        this.id = id;
        this.heuristic = heuristic;
    }
    
    /**
     * @return id of this node
     */
    public int getId(){
        return this.id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return id of parent of this node
     */
    public int getParent(){
        return this.parent;
    }
    
    /**
     * @param parent the id of parent to set
     */
    public void setParent(int parent){
        this.parent = parent ;
    }
    
    /**
     * @return heuristic value of this node
     */
    public double getHeuristic(){
        return this.heuristic;
    }
    
    /**
     * @param heuristic the heuristic value to set
     */
    public void setHeuristic(double heuristic){
        this.heuristic = heuristic;
    }
    
    /**Overwriting toString method
     * @return the node's id following by its heuristic value
     */
    public String toString(){
        return String.format(getId()+"(%.2f)", getHeuristic());
    }

    
}
