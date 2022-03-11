import java.util.ArrayList;

public class BasicSearch implements Search {
    private SearchSpace space;
    private int currentTime, currentDistance, root, start, connection;
    private int bestPathTime, bestPathDistance;
    private ArrayList<Integer> goal;
    private ArrayList<Integer> timeList, distanceList;
    private ArrayList<ArrayList<Integer>> pathList;
    private String console;
    
    public BasicSearch(SearchSpace space){
        this.space = space;
        currentTime = 0;
        currentDistance = 0;
        goal = new ArrayList<Integer>();
        pathList = new ArrayList<ArrayList<Integer>>();
        timeList = new ArrayList<Integer>();
        distanceList = new ArrayList<Integer>();
    }
    
    public void search(int start, int end){
        DFSbasedSearch(start, end, 0);
    }
    
    public void reset(){
        currentTime = 0;
        currentDistance = 0;
        goal.clear();
        pathList.clear();
        timeList.clear();
        distanceList.clear();
    }
    
    public void preSearch(int start){
        reset();
        root = start;
        this.start = start;
        connection = 0;
    }
    
    public ArrayList<Integer> getPath(){
        if(getOptimized()==-1)
            return null;
        else
            return pathList.get(getOptimized());
    }
    
    public ArrayList<Integer> trackPath() {
        return goal;
    }
    
    public String console(){
        return console;
    }
    
    public void DFSbasedSearch(int begin, int end, int connect){
        
        if(start!=root || space.hasNext(start, connection)){
            if(start == end){
                currentTime += space.get(goal.get(goal.size()-1)).getTimeTo(start);
                currentDistance += space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                goal.add(start);
                console = showPath(goal)+"(Goal"+goal.size()+1+")";
                pathList.add((ArrayList<Integer>) goal.clone());
                timeList.add(currentTime);
                distanceList.add(currentDistance);
                connection = goal.remove(goal.size()-1);
                currentTime -= space.get(goal.get(goal.size()-1)).getTimeTo(connection);
                currentDistance -= space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                start = goal.remove(goal.size()-1);
                if(!goal.isEmpty()){
                    currentTime -= space.get(goal.get(goal.size()-1)).getTimeTo(start);
                    currentDistance -= space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                }
            }else if(!space.hasNext(start, connection)){
                console = showPath(goal)+" - "+start+" - (Leaf node)";
                connection = start;
                start = goal.remove(goal.size()-1);
                if(!goal.isEmpty()){
                    currentTime -= space.get(goal.get(goal.size()-1)).getTimeTo(start);
                    currentDistance -= space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                }
            }else if(goal.contains(start)){
                console = showPath(goal)+" - "+start+" - (Loop)";
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
                console = showPath(goal)+" - ";
                start = space.nextNode(start, connection);
                connection = 0;
            }
        }else{
            console = start+" - (Leaf node)";
            SearchPaneController.setIsEnd(true);
            if(getPath()!=null)
                console += "\n\nBest Path: \nPath: "+showPath(getPath())+"\nTime: "+bestPathTime+"\nDistance: "+bestPathDistance;
        }
        
    }
    
    public String showPath(ArrayList<Integer> goal){
        String path = "";
        if(goal == null || goal.isEmpty())
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
    
    
    public int getOptimized(){
        int minTime = Integer.MAX_VALUE;
        int minDistance = Integer.MAX_VALUE;
        int minIndex=-1;
        for(int i=0; i<timeList.size(); i++){
            if(timeList.get(i).compareTo(minTime)<0){
                minTime = timeList.get(i);
                minDistance = distanceList.get(i);
                minIndex = i;
            }else if(timeList.get(i).compareTo(minTime)==0){
                if(distanceList.get(i).compareTo(minDistance)<0)
                    minIndex = i;
            }
        }
        bestPathTime = minTime;
        bestPathDistance = minDistance;
        return minIndex;
    }
    
    public String toString(){
        int i = getOptimized();
        String path = "";
        if(!pathList.isEmpty())
                path+=showPath(pathList.get(i))+"\nTime used: "+timeList.get(i)+"s"+"\nDistance used: "+distanceList.get(i)+"\n";
        else
            path+="No path available\n";
        return path;
    }

}
