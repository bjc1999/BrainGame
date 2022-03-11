public class Synapse{
    private int toID;
    private Integer time;
    private Integer distance;
    
    /**A constructor that create a synapse with specific toID, time and distance.
     * @param toID
     * @param time
     * @param distance
     */
    public Synapse (int toID, int time, int distance){
        this.toID = toID;
        this.time = time;
        this.distance = distance;
    }
    
    /**
     * @return time of synapse
     */
    public Integer getTime(){
        return this.time;
    }

    /**
     * @return distance of synapse
     */
    public Integer getDistance(){
        return this.distance;
    }

    /**
     * @return toID of synapse
     */
    public int getID(){
        return this.toID;
    }
    
    /**Overwriting toString method
     * @return time of synapse
     */ 
    public String toString(){
        return "Time: "+time;
    }
    
}
