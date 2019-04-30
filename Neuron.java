/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braingame;

/**
 *
 * @author Jing Chong
 */
public class Neuron implements Comparable {
    private Integer id;
    private Integer connectNum;
    
    /**A constructor that create an object with 
     * specific id and number of connected node.
     */
    public Neuron(Integer id, Integer connectNum){
        this.id = id;
        this.connectNum = connectNum;
    }

    /**Retrieve id of the neuron.
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**Set id of the neuron.
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**Retrieve number of connected node to the neuron.
     * @return the connectNum
     */
    public Integer getConnectNum() {
        return connectNum;
    }

    /**Set number of connected node to the neuron.
     * @param connectNum the connectNum to set
     */
    public void setConnectNum(Integer connectNum) {
        this.connectNum = connectNum;
    }
    
    /**Convert all the info of this node to string
     * @return a string contain all instances in this class
     */
    public String toString(){
        return "Node ID: "+id+"\tNumber of connected nodes: "+connectNum+"\n";
    }

    /**Compare this node's id to another node's id
     * @param o is the node want to be compared with this node
     * @return positive if greater, negative if smaller else 0
     */
    public int compareTo(Object o) {
        return this.id.compareTo(((Neuron)o).id);
    }
}
