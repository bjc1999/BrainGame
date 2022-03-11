import java.util.ArrayList;

public class BestFirstSearch implements Search{
    private SearchSpace space;
    private int currentTime, currentDistance, connection;
    private Node front;
    private ArrayList<Node> open, close;
    private ArrayList<Integer> goal;
    private String console;
    
    public BestFirstSearch(SearchSpace space){
        this.space = space;
        open = new ArrayList<Node>();
        close = new ArrayList<Node>();
        goal = new ArrayList<Integer>();
        currentTime = 0;
        currentDistance = 0;
    }
    
    public void search(int start, int end){
        bestFirstSearch(start, end, 0);
    }
    
    public void reset(){
        open.clear();
        close.clear();
        goal.clear();
        currentTime = 0;
    }
    
    public void preSearch(int start){
        reset();
        open.add(new Node(-1, start, 0));
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
    
    public void bestFirstSearch(int start, int end, int connect){
        
        if(!open.isEmpty()) {
            console = "Open: "+open+"\nClose: "+close;
            front = open.remove(0);
            if(!nodeClosed(front.getId())){
                close.add(front);
                goal = backtracking(front, false);
                console += "\nExploring "+front.getId()+"...";
                connection = 0;
                if (front.getId() == end) {
                    goal.add(0, end);
                    console += "\nBacktracking...";
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
                            double heuristicValue = front.getHeuristic() + space.get(front.getId()).getTimeTo(next) + 1.0/(space.get(front.getId()).getDistanceTo(next));
                            addPriority(front.getId(), next, heuristicValue);
                            connection = space.nextNode(front.getId(),connection);
                        }else{
                            connection = space.nextNode(front.getId(),connection);
                        }
                    }
                }
            }else{
                console += "\nIgnoring "+front.getId()+"...";
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
    
    public String showPath(ArrayList<Integer> path){
        String pathStr = "";
        for(Integer ptr: path)
            if(path.indexOf(ptr)==path.size()-1)
                pathStr+=ptr;
            else
                pathStr+=ptr+" - ";
        
        return pathStr;
    }
    
    public boolean nodeClosed(int id){
        return close.stream().anyMatch((ptr) -> (ptr.getId() == id));
    }
    
    public void addPriority(int parent, int nextNode, double heuristic){
        if(open.isEmpty())
            open.add(new Node(parent, nextNode, heuristic));
        else
            for(int i=0; i<open.size(); i++){
                if(open.get(i).getHeuristic() >= heuristic){
                    if(open.get(i).getHeuristic() == heuristic){
                        open.add(i+1, new Node(parent, nextNode, heuristic));
                        break;
                    }else{
                        open.add(i, new Node(parent, nextNode, heuristic));
                        break;
                    }
                }else if(i == open.size()-1){
                    open.add(new Node(parent, nextNode, heuristic));
                    break;
                }
            }
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