import java.util.ArrayList;

public class PruneSearch implements Search{
    private SearchSpace space;
    private int currentTime, currentDistance, minTime, root, start, connection;
    private ArrayList<Integer> goal;
    private ArrayList<Integer> timeList, distanceList;
    private ArrayList<ArrayList<Integer>> pathList;
    private String console;
    
    public PruneSearch(SearchSpace space){
        this.space = space;
        currentTime = 0;
        currentDistance = 0;
        goal = new ArrayList<Integer>();
        pathList = new ArrayList<ArrayList<Integer>>();
        timeList = new ArrayList<Integer>();
        distanceList = new ArrayList<Integer>();
    }
    
    public void search(int start, int end){
        pruneSearch(start, end, 0);
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
        minTime = Integer.MAX_VALUE;
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

    public String console() {
        return console;
    }
    
    public void pruneSearch(int begin, int end, int connect){
        if(start!=root || space.hasNext(start, connection)){
            if(start == end){
                currentTime += space.get(goal.get(goal.size()-1)).getTimeTo(start);
                currentDistance += space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                goal.add(start);
                console = showPath(goal) + " (Goal "+pathList.size()+")";
                if(currentTime<=minTime){
                    pathList.add((ArrayList<Integer>) goal.clone());
                    timeList.add(currentTime);
                    distanceList.add(currentDistance);
                    minTime = currentTime;
                }
                connection = goal.remove(goal.size()-1);
                currentTime -= space.get(goal.get(goal.size()-1)).getTimeTo(connection);
                currentDistance -= space.get(goal.get(goal.size()-1)).getDistanceTo(connection);
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
            }else if(currentTime > minTime){
                System.out.println("Prune!");
                console = showPath(goal)+" - "+start+" - (Prune)";
                connection = start;
                start = goal.remove(goal.size()-1);
                if(!goal.isEmpty()){
                    currentTime -= space.get(goal.get(goal.size()-1)).getTimeTo(start);
                    currentDistance -= space.get(goal.get(goal.size()-1)).getDistanceTo(start);
                }
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
            console = start+" - (Leaf node)\n";
            SearchPaneController.setIsEnd(true);
            if(getPath()!=null)
                console += "\nBest Path: \nPath: "+toString();
        }
        
    }
    
    public String showPath(ArrayList<Integer> goal){
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
    
    public int getOptimized(){
        int minIndex = timeList.size()-1;
        for(int i=timeList.size()-2; i>=0; i--){
            if(timeList.get(i).compareTo(timeList.get(minIndex))==0)
                if(distanceList.get(i).compareTo(distanceList.get(minIndex))<0)
                    minIndex = i;
            else
                break;
        }
        return minIndex;
    }
    
    public String toString(){
        int i = getOptimized();
        String path = "";
        if(!pathList.isEmpty())
            path+=showPath(pathList.get(i))+"\nTime: "+timeList.get(i)+"\nDistance: "+distanceList.get(i);
        else
            path+="No path available\n";
        return path;
    }

}
