/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braingame;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Jing Chong
 */
public class Attribute<K, T, D> {
    private TreeMap<T, K> timeList;
    private TreeMap<D, K> distanceList;
    
    /**A constructor that create a tree map with
     * (Integer) time needed to travel to a node as key and 
     * (Integer) id of that particular node as value
     * and another tree map with
     * (Integer) distance needed to travel to a node as key and 
     * (Integer) id of that particular node as value.
     */
    public Attribute(){
        timeList = new TreeMap();
        distanceList = new TreeMap();
    }
    
    /**Add a new node with a specific id, time and distance
     * @param id is the id of connected node
     * @param time is the time traveled to the node
     * @param distance is the distance traveled to the node
     */
    public void addNode(K id, T time, D distance){
        timeList.put(time, id);
        distanceList.put(distance, id);
    }
    
    public int getConnection(){
        return timeList.size();
    }
    
    /**Retrieve time of a specific node with given id
     * @param id is the id of node
     * @return time traveled to the specified node
     */
    public T getTimeOf(K id){
        for(Map.Entry<T, K> entry: timeList.entrySet())
            if(entry.getValue().equals(id))
                return entry.getKey();
        return null;
    }
    
    /**Retrieve distance of a specific node with given id
     * @param id is the id of node
     * @return distance traveled to the specific node
     */
    public D getDistanceOf(K id){
        for(Map.Entry<D, K> entry: distanceList.entrySet())
            if(entry.getValue().equals(id))
                return entry.getKey();
        return null;
    }
    
    public K getID(T time){
        return timeList.get(time);
    }
    
    /**Remove a specified node from both tree map
     * @param id is the id of node that want to remove
     */
    public void removeNode(K id){
        timeList.remove(getTimeOf(id));
        distanceList.remove(getDistanceOf(id));
    }
    
    /**Clear all the nodes in both tree map
     */
    public void clearNode(){
        timeList.clear();
        distanceList.clear();
    }
    
    /**Check whether it has connected node or not
     * @return true if it has connected node else false
     */
    public boolean isEmpty(){
        return timeList.isEmpty();
    }
    
    /**Check whether it contains specific id of node
     * @param id of the node searching
     * @return true is it contains the node else false
     */
    public boolean contains(K id){
        return timeList.containsValue(id);
    }
    
    /**Convert all the attribute of a node to string
     * @return a string contain all instances in this class
     */
    public String toString(){
        String info = "Connected Node:\n";
        if(!isEmpty()){
            for(Map.Entry<T, K> entryT: timeList.entrySet())
                for(Map.Entry<D, K> entryD: distanceList.entrySet())
                    if(entryT.getValue().equals(entryD.getValue()))
                        info+="ID: "+entryT.getValue()+"\tTime: "+entryT.getKey()+"\t Distance: "+entryD.getValue()+"\n";
        }else
            info+="Not found\n";
        return info;
    }
    
    public T nextNode(T time){
        return timeList.higherKey(time);
    }
    
}
