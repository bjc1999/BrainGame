/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braingame;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 *
 * @author Jing Chong
 */
public class SearchSpace {
    private TreeMap<Neuron, Attribute> space;
    private Stack<Integer> goal = new Stack();
    
    /**A constructor that create a tree map with
     * Neuron class as key and Attribute class as value.
     */
    public SearchSpace(){
        space = new TreeMap();
    }
    
    /**Add a new node to the search space.
     * @param id is the id of newly added node
     * @param connectNum is the number of connected node to the newly added node
     */
    public void addNode(int id, int connectNum){
        space.put(new Neuron(id, connectNum), new Attribute());
    }
    
    /**Retrieve the object id of Neuron class with a given node's id.
     * @param id is the id of a node
     * @return the object id of the node with given id
     */
    public Neuron getID(int id){
        for(Map.Entry<Neuron, Attribute> entry: space.entrySet())
            if(entry.getKey().getId().equals(id))
                return entry.getKey();
        return null;
    }
    
    /**Add a new connected node info to a specific node.
     * @param fromID is the id of a node in search space
     * @param toID is the id of a node connected to the fromID node
     * @param time is the time traveled from fromID node to toID node
     * @param distance is the distance traveled from fromID node to toID node
     */
    public void addAttribute(int fromID, int toID, int time, int distance){
        space.get(getID(fromID)).addNode(toID, time, distance);
    }
    
    /**Remove a specific node from search space.
     * @param id is the id of the node want to remove
     */
    public void removeNode(int id){
        space.remove(getID(id));
        for(Map.Entry<Neuron, Attribute> entry: space.entrySet())
            if(entry.getValue().contains(id)){
                entry.getValue().removeNode(id);
                entry.getKey().setConnectNum(entry.getKey().getConnectNum()-1);
            }
    }
    
    /**Remove all the nodes in search space.
     */
    public void clear(){
        space.clear();
    }
    
    /**Convert all the info in search space to string
     * @return a string contain all instances in this class
     */
    public String toString(){
        String info = "Search Space\n";
        for(Map.Entry<Neuron, Attribute> entry: space.entrySet())
            info+=entry.getKey().toString()+entry.getValue().toString()+"\n";
        return info;
    }
    
    /*
    public int nextNode(int id, int checkedNode){
        if(!space.get(getID(id)).isEmpty()){
            if(space.get(getID(id)).nextNode(checkedNode) != null)
                return (Integer)space.get(getID(id)).nextNode(checkedNode);
        }
        return 0;
    }
    private int checkedNode = 0;
    
    public void search(int start, int end, int connection){
        System.out.println(getID(start).getConnectNum());
        if(start == end){
            goal.push(start);
            goal.pop();
            return;
        }else if(connection!=0){
            goal.push(start);
            search((Integer)space.get(getID(start)).getID(nextNode(start, checkedNode)), end, getID(nextNode(start, checkedNode)).getConnectNum());
            goal.pop();
            search(start, end, getID(start).getConnectNum()-1);
        }
    }
    
    public void solution(){
        while(!goal.empty())
            System.out.println(goal.pop());
    }
*/

    
}
