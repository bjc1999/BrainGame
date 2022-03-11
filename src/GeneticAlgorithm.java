import java.util.ArrayList;
import java.util.TreeMap;

public class GeneticAlgorithm {

    private TreeMap<Integer, Neuron> treemap;
    private Individual bestIndividual;
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    protected int tournamentSize;
    private String console;
    private int generation = 1;
    private final int maxGenerations = 20;
    private Population population;

    /**
     * Constructor of GeneticAlgorithm and initialize constants
     *
     * @param treeObject the SearchSpace object
     * @param populationSize
     * @param mutationRate
     * @param crossoverRate
     * @param elitismCount
     * @param tournamentSize
     */
    public GeneticAlgorithm(TreeMap<Integer, Neuron> treeObject, int populationSize, double mutationRate, double crossoverRate, int elitismCount,
            int tournamentSize) {
        this.treemap = treeObject;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    /**
     * Initialize population
     *
     * @param startID the start node of the search
     * @param endID the end node of the search
     * @return population The initial population generated
     */
    public Population initPopulation(int startID, int endID) {
        // Initialize population
        Population population = new Population(this.treemap, this.populationSize, startID, endID);
        return population;
    }

    /**
     * Check if population has met termination condition -- this termination
     * condition is a simple one; simply check if we've exceeded the allowed
     * number of generations.
     *
     * @param generationsCount Number of generations passed
     * @param maxGenerations Number of generations to terminate after
     * @return boolean True if termination condition met, otherwise, false
     */
    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations, Population population) {
        if (generationsCount > maxGenerations) {
            return true;
        }
        if (population.getIndividual(0).getGoal()) {
            Individual tempIndividual = population.getIndividual(0);
            for (int iterate = 1; iterate < (int) (population.size()); iterate++) {
                if (tempIndividual.getPath().size() != population.getIndividual(iterate).getPath().size()) {
                    return false;
                }
                for (int iterate2 = 0; iterate2 < tempIndividual.getPath().size(); iterate2++) {
                    if (!(tempIndividual.getPath().get(iterate2) == population.getIndividual(iterate).getPath().get(iterate2))) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Evaluate population -- basically run calcFitness on each individual.
     *
     * @param population the population to evaluate
     * @param endID the end node of the search
     */
    public void evalPopulation(Population population, int endID) {
        double populationFitness = 0;
        Individual[] individualArray = population.getIndividuals();
        // Loop over population evaluating individuals and summing population fitness
        for (int iterate = 0; iterate < individualArray.length; iterate++) {
            individualArray[iterate].checkGoal(endID);
            individualArray[iterate].calculateFitness();
            populationFitness += individualArray[iterate].getFitness();
        }

        double avgFitness = populationFitness / population.size();
        population.setPopulationFitness(avgFitness);
    }

    /**
     * Selects parent for crossover using tournament selection
     *
     * @param population the population to select
     * @return The individual selected as a parent
     */
    public Individual selectParent(Population population) {
        // Create tournament
        Population tournament = new Population(this.treemap, this.tournamentSize);

        // Add random individuals to the tournament
        population.shuffle();
        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        // Return the best
        return tournament.getFittest(0);
    }

    /**
     * briefly explain crossover
     *
     * @param population
     * @param endID
     * @return The new population
     */
    public Population crossoverPopulation(Population population, int endID) {
        // Create new blank population
        Population newPopulation = new Population(this.treemap, population.size());

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // Get parent1
            Individual parent1 = population.getFittest(populationIndex);
            parent1.checkGoal(endID);

            if (!parent1.getGoal()) {

            }

            // Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                // Find parent2 with tournament selection
                Individual parent2 = this.selectParent(population);
                Individual offspring = parent1;

                for (int i = 0; i < parent1.getPath().size(); i++) {
                    if (parent2.getPath().contains(parent1.getPath().get(i))) {
                        int neuron = parent1.getPath().get(i);
                        int index1 = parent1.getPath().indexOf(neuron);
                        int index2 = parent2.getPath().indexOf(neuron);
                        if (parent1.getPath().size() - index1 >= parent2.getPath().size() - index2) {
                            //parent2 behind
                            //remove the neuron behind of the parent1
                            while (offspring.getPath().get(offspring.getPath().size() - 1) != neuron) {
                                int remove = offspring.getPath().remove(offspring.getPath().size() - 1);
                                offspring.getHashSet().remove(remove);
                            }
                            // add the neuron behind of the parent2
                            for (int iterate = index2 + 1; iterate < parent2.getPath().size(); iterate++) {
                                offspring.getPath().add(parent2.getPath().get(iterate));
                                offspring.getHashSet().add(offspring.getPath().get(offspring.getPath().size() - 1));
                            }
                        } else {
                            //parent 2 infront
                            //remove the neuron infront of the parent1
                            while (offspring.getPath().get(0) != neuron) {
                                int remove = offspring.getPath().remove(0);
                                offspring.getHashSet().remove(remove);
                            }
                            // add the neuron infront of the parent 2
                            for (int iterate = index2 - 1; iterate > -1; iterate--) {
                                offspring.getPath().add(0, parent2.getPath().get(iterate));
                                offspring.getHashSet().add(parent2.getPath().get(iterate));
                            }

                        }
                        break;
                    }
                }
                offspring.checkGoal(endID);
                offspring.calculateTime();
                offspring.calculateDistance();
                offspring.calculateFitness();
                
                newPopulation.setIndividual(populationIndex, offspring);

            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }

        }
        return newPopulation;
    }

    /**
     * Apply mutation to population briefly explain mutation process
     *
     * @param population The population to apply mutation to
     * @param startID the start node
     * @param endID the end node
     * @return The mutated population
     */
    public Population mutatePopulation(Population population, int startID, int endID) {
        // Initialize new blank population
        Population newPopulation = new Population(this.treemap, this.populationSize);

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            // Skip mutation if this is an elite individual
            if (populationIndex >= this.elitismCount) {
                // Does this gene need mutation?
                if (mutationRate > Math.random()) {
                    // If mutataion occurs generate a new path
                    int length = population.getFittest(0).getPath().size();
                    individual = new Individual(treemap, startID, endID, length);
                    //individual.checkLoop(individual.getPath());
                    individual.calculateTime();
                    individual.calculateDistance();
                    individual.calculateFitness();
                } else if (!individual.getGoal()) {
                    individual = new Individual(treemap, startID, endID, population.size());
                }

            }

            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual);
        }
        // Return mutated population
        return newPopulation;
    }

    public void search(int startID, int endID) {
        if (!isTerminationConditionMet(generation, maxGenerations, population)) {
            // Apply crossover
            population = crossoverPopulation(population, endID);
            // Evaluate population
            evalPopulation(population, endID);
            
            // Apply mutation
            population = mutatePopulation(population, startID, endID);
            // Evaluate population
            evalPopulation(population, endID);

            population.arrange();
            console = population.toString(generation);
            // Increment the current generation
            generation++;
        }else{
            if(population.getFittest(0).checkGoal(endID))
                bestIndividual = population.getFittest(0);
            else
                bestIndividual = null;
            GeneticPaneController.setIsEnd(true);
            console = "Stopped after " + (generation-1) + " generations.\n"+bestIndividual.goalToString()+"\n";
        }

    }
    
    public ArrayList<Integer> getPath() {
        if(bestIndividual==null)
            return null;
        return bestIndividual.getPath();
    }

    public void reset() {
        bestIndividual = null;
        generation=1;
    }

    public String toString() {
        String path = "";
        if (bestIndividual.getGoal()) {
            for (Integer ptr : bestIndividual.getPath()) {
                path += ptr + " - ";
            }
            path += " goal!\nTime used: " + bestIndividual.getTime() + "s\n" + "Distance used: " + bestIndividual.getDistance() + "\n";
        } else {
            path += "No path available";
        }
        return path;
    }
    
    public int getCurrentGeneration(){
        return generation;
    }
    
    public double getIndividualFitness(int offset){
        return population.getFittest(offset).getFitness();
    }
    
    public double getPopulationFitness(){
        return population.getPopulationFitness();
    }
    
    public void setInitPopulation(int start, int end){
        population = initPopulation(start, end);
        evalPopulation(population, end);
    }

    public Individual getFittestIndividual(){
        return population.getFittest(0);
    }
    
    public String console(){
        return this.console;
    }

}
