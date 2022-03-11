import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

public class Population {

    private TreeMap<Integer , Neuron> treemap;
    private Individual[] population;
    private double populationFitness = -1;

    /**
     *	Initializes blank population of individuals
     *	@param treemap the tree map of ID and Neuron
     *	@param populationSize The size of the population
     */
    public Population(TreeMap<Integer , Neuron> treemap , int populationSize) {
        // Initial a blank individual Array
        this.population = new Individual[populationSize];
        this.treemap = treemap;
    }

    /**
     *	Initializes population of individuals
     *	@param treemap the tree map of ID and Neuron
     *	@param populationSize The size of the population
     *	@param startID the start node of the search
     *      @param endID the end node of the search
     */
    public Population(TreeMap<Integer , Neuron> treemap , int populationSize, int startID , int endID) {
        // Initial population Array
        this.population = new Individual[populationSize];
        this.treemap = treemap;

        // Loop over population size
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
                // Create individual
                Individual individual = new Individual(treemap, startID , endID , populationSize);
                // Add individual to population Array
                this.population[individualCount] = individual;
        }
    }

    /**
     * Get individuals array from the population
     * @return individual's Array
     */
    public Individual[] getIndividuals() {
        return this.population;
    }

    /**
     * Find fittest individual in the population
     * @param offset
     * @return individual Fittest individual at offset
     */
    public Individual getFittest(int offset) {
        // Order population by fitness
        Arrays.sort(this.population, new Comparator<Individual>() {
                @Override
                public int compare(Individual o1, Individual o2) {
                        if (o1.getFitness() > o2.getFitness()) {
                                return -1;
                        } else if (o1.getFitness() < o2.getFitness()) {
                                return 1;
                        }
                        return 0;
                }
        });

        // Return the fittest individual
        return this.population[offset];
    }

    public void arrange(){
        Arrays.sort(this.population, new Comparator<Individual>() {
                @Override
                public int compare(Individual o1, Individual o2) {
                        if (o1.getFitness() > o2.getFitness()) {
                                return -1;
                        } else if (o1.getFitness() < o2.getFitness()) {
                                return 1;
                        }else{/*
                            if(o1.getDistance() > o2.getDistance()){
                                return 1;
                            }else if(o1.getDistance() < o2.getDistance()){
                                return -1;
                            }*/
                            return 0;
                        }
                }
        });
    }

    /**
     * Set population fitness
     * @param fitness The population total fitness
     */
    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    /**
     * Get population fitness
     * @return populationFitness The population total fitness
     */
    public double getPopulationFitness() {
        return this.populationFitness;
    }

    /**
     * Get population size
     * @return size The population size
     */
    public int size() {
        return this.population.length;
    }

    /**
     * Set individual at offset
     * @param individual the individual object to be set
     * @param offset
     * @return individual object of the specific individual
     */
    public Individual setIndividual(int offset, Individual individual) {
        return population[offset] = individual;
    }

    /**
     * Get individual at offset
     * @param offset
     * @return individual object at the offset
     */
    public Individual getIndividual(int offset) {
        return population[offset];
    }

    /**
     * Shuffles the population in-place
     */
    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                Individual a = population[index];
                population[index] = population[i];
                population[i] = a;
        }
    }

    // need optimization
    public String toString(int ge){
        String output ="";
        for(int iterate = 1 ; iterate <= population.length ; iterate++){
                output+="Path: "+iterate+"\n";
                output+=population[iterate-1].toString();
        }
        return output;
    }

}