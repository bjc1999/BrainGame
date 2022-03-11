import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SearchSpace {
    private static TreeMap<Integer, Neuron> space;
    
    
    /**A constructor that create a tree map with
     * id (Integer) as key and Neuron class as value.
     */
    public SearchSpace(){
        space = new TreeMap<Integer, Neuron>();
    }
    
    public SearchSpace(int i){
        
    }
    
    /**Add a new node to the search space.
     * @param id is the id of the newly added node
     * @param connectNum is the number of connected node to the newly added node
     * @param lifetime is the number of time a node can pass the message
     */
    public void addNode(int id, int connectNum, int lifetime){
        space.put(id, new Neuron(connectNum, lifetime));
    }
    
    /**Add a new connected node info to a specific node.
     * @param fromID is the id of a node in search space
     * @param toID is the id of a node connected to the fromID node
     * @param time is the time traveled from fromID node to toID node
     * @param distance is the distance traveled from fromID node to toID node
     */
    public void addSynapse(int fromID, int toID, int time, int distance){
        space.get(fromID).addSynapse(toID, time, distance);
    }
    
    /**Remove a specific node from search space.
     * @param id is the id of the node to be removed
     */
    public void removeNode(int id){
        space.remove(id);
        for(Map.Entry<Integer, Neuron> entry: space.entrySet())
            if(entry.getValue().containsSynapse(id)){
                entry.getValue().removeSynapse(id);
            }
    }
    
    /**Remove all the nodes in search space.
     */
    public void clear(){
        space.clear();
    }
    
    /**
     * @return size (number of neurons) of search space
     */
    public int getSize(){
        return space.size();
    }
    
    /**
    * Deduct lifetime of all node in the path
    * @param path is the path which lifetime of its node to be deducted.
    */
    public void deductLifeTimes(ArrayList<Integer> path) {
        for (int i = 0; i < path.size(); i++) {
            int id = path.get(i);
            space.get(id).deductLifeTime();
            TableController.deductLifetime(id);
            if (space.get(id).getLifetime() == 0) {
                space.remove(id);
                GraphSetup.deleteNeuron(id);
                TableController.removeNode(id);
                TableController.removeSynapse(id);
                for (Map.Entry<Integer, Neuron> entry : space.entrySet()) {
                    if (entry.getValue().containsSynapse(id)) {
                        entry.getValue().removeSynapse(id);
                    }
                }
            }
        }
    }
    
    /**Overwriting toString method
     * @return id and all others info of neuron
     */
    public String toString(){
        String info = "Search Space\n";
        for(Map.Entry<Integer, Neuron> entry: space.entrySet())
            info+="Node ID: "+entry.getKey()+"\t"+entry.getValue().toString()+"\n";
        return info;
    }
    
    /**Retrieve the next synapse of a specific neuron that haven't been explore
     * @param  id of a neuron
     * @param currentNode the id of last synapse explored
     * @return the next synapse to be explored
     */
    public int nextNode(int id, int currentNode){
        return space.get(id).getNext(currentNode);
    }
    
    /**Check whether a specific neuron still has synapse that haven't explored
     * @param id of a neuron
     * @param currentNode the id of last synapse explored
     * @return true if yes else false
     */
    public boolean hasNext(int id, int currentNode){
        return space.get(id).hasNext(currentNode);
    }
    
    /**Retrieve a neuron with specific id
     * @param id of neuron
     * @return Neuron class with the specified id
     */
    public Neuron get(int id){
        return space.get(id);
    }
    
    /**Retrieve search space
     * @return search space
     */
    public TreeMap<Integer, Neuron> getTreeMap(){
        return this.space;
    }
    
    /**Check whether search space contains specific node
     * @param id of a node
     * @return true if the specific node exists in search space else false
     */
    public boolean contains(int id){
        return space.containsKey(id);
    }
    
    /**Check whether search space is empty
     * @return true if search space is empty else false
     */
    public boolean isEmpty(){
        return space.isEmpty();
    }
}
