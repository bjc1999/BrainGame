import java.util.ArrayList;

public class BreadthFirstSearch implements Search {
    private SearchSpace space;
    private int currentTime, currentDistance, connection;
    private Node front;
    private ArrayList<Node> open, close;
    private ArrayList<Integer> goal;
    private String console;
    
    public BreadthFirstSearch(SearchSpace space){
        this.space = space;
        open = new ArrayList<Node>();
        close = new ArrayList<Node>();
        goal = new ArrayList<Integer>();
        currentTime = 0;
        currentDistance = 0;
    }
    
    public void search(int start, int end){
        breadthFirstSearch(start, end, 0);
    }
    
    public void reset(){
        open.clear();
        close.clear();
        goal.clear();
        currentTime = 0;
        currentDistance = 0;
    }
    
    public void preSearch(int start){
        reset();
        open.add(new Node(-1, start));
        connection = 0;
    }

    public ArrayList<Integer> getPath(){
        return goal;
    }

    
    public ArrayList<Integer> trackPath(){
        return goal;
    }

    
    public String console() {
        return console;
    }
    
    public void breadthFirstSearch(int start, int end, int connect){
        if(!open.isEmpty()){
            console = "Open: "+open+"\nClose: "+close;
            front = open.remove(0);
            close.add(front);
            connection = 0;
            goal = backtracking(front, false);
            if (front.getId() == end) {
                goal.add(0, end);
                console += "\nBacktracking...\n";
                goal = backtracking(front, true);
                if (goal.get(0) != start){
                    goal.clear();
                }else{
                    for(int j = goal.size()-1  ; j > 0 ; j--){
                        currentTime += space.get (goal.get(j-1)).getTimeTo (goal.get(j));
                        currentDistance += space.get (goal.get(j-1)).getDistanceTo (goal.get(j));
                    }
                }
                open.clear();
                SearchPaneController.setIsEnd(true);
                console += "\nBest Path: \nPath: "+toString()+"\nTime: "+currentTime+"\nDistance: "+currentDistance;
            }else{
                while (space.hasNext(front.getId(), connection)) {
                    int next = space.nextNode(front.getId(), connection);
                    if (!nodeClosed(next)) {
                        open.add(new Node(front.getId(), next));
                        connection = space.nextNode(front.getId(),connection);
                    }else{
                        connection = space.nextNode(front.getId(),connection);
                    }
                }
            }
        }else{
            SearchPaneController.setIsEnd(true);
            goal = null;
        }
    }
    
    public ArrayList<Integer> backtracking(Node currentNode, boolean end){
        ArrayList<Integer> path = new ArrayList<Integer>();
        path.add(currentNode.getId());
        if(end)
            console += showPath(path)+"\n";
        for(int i = 0 ; i< close.size() -1 ; i++){
            if(currentNode.getParent() == close.get(i).getId()){
                path.add(0, close.get(i).getId());
                currentNode = close.get(i);
                if(i !=0 ){
                    i = -1; // re-searching from index 0
                    if(end)
                        console += showPath(path)+"\n";
                }
                else{
                    if(end)
                        console += showPath(path)+"\n";
                    break;
                }
            }
        }
        return path;
    }
    
    public boolean nodeClosed(int id){
        return close.stream().anyMatch((ptr) -> (ptr.getId() == id));
    }
    
    public String showPath(ArrayList<Integer> path){
        String pathStr = "";
        for(Integer ptr: path)
            if(path.indexOf(ptr)==path.size()-1)
                pathStr+=ptr;
            else
                pathStr+=ptr+" - ";
        
        return pathStr;
    }
    
    
    public String toString(){
        String path = "";
        if(goal.isEmpty())
            path+="No path available";
        else{
            path = goal.stream().map((ptr) -> ptr+" - ").reduce(path, String::concat);
            path+=" goal!";
        }
        return path;
    }
    
    
}
