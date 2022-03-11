public class TableNode {
    private Integer nodeID;
    private Integer numConnection;
    private Integer lifetime;
    
    private Integer source;
    private Integer destination;
    private Integer time;
    private Integer distance;
    
    /**A constructor for neuron
     * @param nodeID
     * @param numConnection
     * @param lifetime 
     */
    public TableNode(Integer nodeID, Integer numConnection, Integer lifetime){
        setNodeID(nodeID);
        setNumConnection(numConnection);
        setLifetime(lifetime);
    }
    
    /**A constructor for synapse
     * @param source
     * @param destination
     * @param time
     * @param distance 
     */
    public TableNode(Integer source, Integer destination, Integer time, Integer distance){
        setSource(source);
        setDestination(destination);
        setTime(time);
        setDistance(distance);
    }
    
    /**
     * @return the source
     */
    public Integer getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * @return the destination
     */
    public Integer getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    /**
     * @return the time
     */
    public Integer getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * @return the distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * @return the nodeID
     */
    public Integer getNodeID() {
        return nodeID;
    }

    /**
     * @param nodeID the nodeID to set
     */
    public void setNodeID(Integer nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * @return the numConnection
     */
    public Integer getNumConnection() {
        return numConnection;
    }

    /**
     * @param numConnection the numConnection to set
     */
    public void setNumConnection(Integer numConnection) {
        this.numConnection = numConnection;
    }

    /**
     * @return the lifetime
     */
    public Integer getLifetime() {
        return lifetime;
    }

    /**
     * @param lifetime the lifetime to set
     */
    public void setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
    }
}
