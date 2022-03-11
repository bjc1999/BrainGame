import java.util.ArrayList;

public class DepthFirstSearch implements Search{
    private SearchSpace space;
    private int currentTime, currentDistance, root, start, connection;
    private ArrayList<Integer> goal;
    private String console;
    
    public DepthFirstSearch(SearchSpace space){
        this.space = space;
        currentTime = 0;
        currentDistance = 0;
        goal = new ArrayList<Integer>();
    }
    
    public void search(int start, int end){
        depthSearch(start, end, 0);
    }
    
    public void reset(){
        goal.clear();
        currentTime = 0;
        currentDistance = 0;
    }
    
    public void preSearch(int start){
        reset();
        root = start;
        this.start = start;
        connection=0;
    }
    
    public ArrayList<Integer> getPath() {
        return goal;
    }

    public ArrayList<Integer> trackPath() {
        return goal;
    }

    public String console() {
        return console;
    }
    
    public void depthSearch(int begin, int end, int connect){
        if(start!=root || space.hasNext(start, connection)){
            if(start == end){
                currentTime += space.get(goal.get(goal.size()-1)).getTimeTo(start);
                currentDistance += space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                goal.add(start);
                console = toString()+" (Goal)\n";
                SearchPaneController.setIsEnd(true);
                console += "\nBest Path: \nPath: "+toString()+"\nTime: "+currentTime+"\nDistance: "+currentDistance;
            }else if(!space.hasNext(start, connection)){
                console = toString()+" - "+start+" - (Leaf node)";
                connection = start;
                start = goal.remove(goal.size()-1);
                if(!goal.isEmpty()){
                    currentTime -= space.get(goal.get(goal.size()-1)).getTimeTo(start);
                    currentDistance -= space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                }
            }else if(goal.contains(start)){
                console = toString()+" - "+start+" - (Loop)";
                connection = start;
                start = goal.remove(goal.size()-1);
                currentTime -= space.get(goal.get(goal.size()-1)).getTimeTo(start);
                currentDistance -= space.get(goal.get(goal.size()-1)).getDistanceTo(start);
            }else{
                if(!goal.isEmpty()){
                    currentTime += space.get(goal.get(goal.size()-1)).getTimeTo(start);
                    currentDistance += space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                }
                goal.add(start);
                console = toString()+" - ";
                start = space.nextNode(start, connection);
                connection = 0;
            }
        }else{
            console = start+" - (Leaf node)\n";
            SearchPaneController.setIsEnd(true);
            goal = null;
        }
    }
    
    public String toString(){
        String path = "";
        if(goal.isEmpty())
            path+="No path available";
        else{
            for(Integer ptr: goal)
                if(goal.indexOf(ptr)==goal.size()-1)
                    path+=ptr;
                else
                    path+=ptr+" - ";
        }
        return path;
    }

    
}
