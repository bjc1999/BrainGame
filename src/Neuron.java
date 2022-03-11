import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Neuron {
    private Integer lifetime;
    private Integer connectNum;
    private ArrayList<Synapse> synapseList;
    
    /**A constructor that create an object with 
     * specific lifetime and number of connected node.
     * @param connectNum
     * @param lifetime
     */
    public Neuron(Integer connectNum , Integer lifetime ){
        this.lifetime  = lifetime ;
        this.connectNum = connectNum;
        synapseList = new ArrayList<Synapse>();
    }

    /**
     * @return lifetime of neuron
     */
    public Integer getLifetime () {
        return lifetime ;
    }

    /**
     * @param lifetime the lifetime to set
     */
    public void setLifetime(Integer lifetime ) {
        this.lifetime = lifetime ;
    }

    /**
     * @return number of connected neurons to neuron
     */
    public Integer getConnectNum() {
        return connectNum;
    }

    /**
     * @param connectNum the connectNum to set
     */
    public void setConnectNum(Integer connectNum) {
        this.connectNum = connectNum;
    }
    
    /**
     * @return all synapses of neuron
     */
    public ArrayList<Synapse> getSynapseList(){
        return this.synapseList;
    }
    
    /**Overwriting toString method
     * @return connectNum, lifetime and all synapses of neuron
     */
    public String toString(){
        String info = "";
        info += "Number of connected nodes: "+connectNum+"\tLifetime: "+lifetime+"\nConnection:\n";
        if(connectNum!=0)
            for(Synapse ptr: synapseList)
                info += ptr.toString();
        else
            info += "No Connection\n";
        return info;
    }
    
    /**Add a synapse that is starting from this neuron in ascending order 
     * in terms of time followed by distance
     * @param toID (destination) of synapse
     * @param time of synapse
     * @param distance of synapse
     */
    public void addSynapse(int toID, Integer time, int distance){
        if(synapseList.size()==0)
            synapseList.add(new Synapse(toID, time, distance));
        else
            for(int i=0; i<synapseList.size(); i++){
                if(synapseList.get(i).getTime().compareTo(time)>=0){
                    if(synapseList.get(i).getTime().compareTo(time)==0){
                        if(synapseList.get(i).getDistance().compareTo(distance)<0){
                            synapseList.add(i+1, new Synapse(toID, time, distance));
                            break;
                        }else{
                            synapseList.add(i, new Synapse(toID, time, distance));
                            break;
                        }
                    }else{
                        synapseList.add(i, new Synapse(toID, time, distance));
                        break;
                    }
                }else if(i == synapseList.size()-1){
                    synapseList.add(new Synapse(toID, time, distance));
                    break;
                }
            }
    }
    
    /**Remove a synapse with specific destination
     * @param toID (destination) of synapse
     */
    public void removeSynapse(int toID){
        if(containsSynapse(toID)){
            synapseList.remove(getIndexOf(toID));
            this.connectNum--;
        }
        else
            System.out.println("Synapse not found.");
    }
    
    /**Retrieve index of a synapse with specific destination
     * @param toID (destination) of synapse
     * @return index of synapse if exits else -1
     */
    public int getIndexOf(int toID){
        for(Synapse ptr: synapseList)
            if(ptr.getID() == toID)
                return synapseList.indexOf(ptr);
        return -1;
    }
    
    /**Check whether this neuron contains a synapse with specific destination
     * @param toID (destination) of synapse
     * @return true if contains else false
     */
    public boolean containsSynapse(int toID){
        return getIndexOf(toID)!=-1;
    }
    
    /**Check whether this neuron still has synapse that haven't explored
     * @param currentNode the id of last synapse explored
     * @return true if yes else false
     */
    public boolean hasNext(int currentNode){
        return getIndexOf(currentNode)<synapseList.size()-1 && !synapseList.isEmpty();
    }
    
    /**Retrieve the next synapse that haven't been explore
     * @param currentNode the id of last synapse explored
     * @return the next synapse to be explored
     */
    public int getNext(int currentNode){
        return synapseList.get(getIndexOf(currentNode)+1).getID();
    }
    
    /**Retrieve time traveled from this neuron to a specific neuron
     * @param toID id of destination (neuron)
     * @return time traveled
     */
    public int getTimeTo(int toID){
        return synapseList.get(getIndexOf(toID)).getTime();
    }
    
    /**Retrieve distance traveled from this neuron to a specific neuron
     * @param toID id of destination (neuron)
     * @return distance traveled
     */
    public int getDistanceTo(int toID){
        return synapseList.get(getIndexOf(toID)).getDistance();
    }
    
    /**Retrieve synapse with this node as source given a specific destination
     * @param ID of destination for the synapse
     * @return synapse object with this neuron as source and given id as destination
     */
    public Synapse getSynapseTo(int ID){
        for(int iterate = 0 ; iterate < synapseList.size() ; iterate++){
            if(synapseList.get(iterate).getID() == ID)
                return synapseList.get(iterate);
        }
        return null;
    }
    
    /**Deduct lifetime of neuron by one
     */
    public void deductLifeTime(){
        this.lifetime = lifetime -1 ;
    }
    
    public boolean checkHaveNext(HashSet<Integer> set){
        for(int iterate = 0 ; iterate < synapseList.size() ; iterate ++){
            if(!(set.contains(synapseList.get(iterate).getID()))){
                //cond = true;
                return true;
            }
        }
        return false;
    }
    
    
    public int getRandomNext(HashSet<Integer> set){
        boolean cond = false;
        ArrayList<Synapse> tempList = new ArrayList<>();
        for(int iterate = 0 ; iterate < synapseList.size() ; iterate ++){
            if(!(set.contains(synapseList.get(iterate).getID()))){
                cond = true;
                tempList.add(synapseList.get(iterate));
            }
        }
        if(cond){
            Random r = new Random();
            int temp = r.nextInt(tempList.size());
            int nextNode = tempList.get(temp).getID();
            return nextNode;
        }else{
            return -1;
        }
    }
}