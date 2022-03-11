import java.util.ArrayList;

public interface Search {
    
    /**A common search method to be called to run different type of searching
     * @param start id of starting neuron
     * @param end id of destination neuron
     */
    public abstract void search(int start, int end);
    
    /**Reset all the attributes in search class before running next search
     */
    public abstract void reset();
    
    /**A pre-setting for some attributes in every search class before it can run searching
     * @param start id of starting neuron
     */
    public abstract void preSearch(int start);
    
    /**Retrieve the result of searching in term of a path between starting and ending neurons
     * @return ArrayList of type Integer that contains id of every neuron between two neurons
     */
    public abstract ArrayList<Integer> getPath();
    
    /**Track the searching process by retrieving the current path
     * @return  ArrayList of type Integer that contains id of every neuron that currently explore
     */
    public abstract ArrayList<Integer> trackPath();
    
    /**Track the searching process by showing searching path in terms of String
     * @return String that shows the current searching process
     */
    public abstract String console();
}
