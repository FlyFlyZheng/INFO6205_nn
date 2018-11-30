import javax.swing.text.AsyncBoxView;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class GeneticAlgorithm {

    private int geneSize;

    private int generation = 1;

    private double bestScore;
    private double worstScore;
    private double totalScore;
    private double averageScore;

    private double x;
    private double y;
    private int geneI;

    private List<Chromosome> population;

    public GeneticAlgorithm(int size){
        this.geneSize = size;

    }

    public void caculte(){
        generation = 1;
        init();
        while(generation < Config.maxIterNum){
            evlove();
            print();
            generation++;
        }
    }


    private void init(){
        for(int i=0; i<Config.POPULATION_SIZE; i++){
            population = new ArrayList<>();
            Chromosome chromosome = new Chromosome(geneSize);
            population.add(chromosome);
        }
    }

    //get a parent
    private Chromosome getParentChromosome(){
        double slice = Math.random() * totalScore;
        double sum = 0;
        for(Chromosome chro : population){
            sum += chro.getScore();
            if(sum > slice && chro.getScore()>=averageScore){
                return chro;
            }
        }
        return null;

    }

    //generate next generation
    public void evlove(){
        List<Chromosome> childPopulation = new ArrayList<>();

        while(childPopulation.size() < Config.POPULATION_SIZE){
            Chromosome parent1 = getParentChromosome();
            Chromosome parent2 = getParentChromosome();
            List<Chromosome> children = Chromosome.genetic(parent1, parent2);
            if(children != null){
                for(Chromosome child: children){
                    childPopulation.add(child);
                }
            }
        }

        List<Chromosome> temp = population;
        population = childPopulation;
        temp.clear();

        mutation();
        calculateScore();
    }

    public void mutation(){
        for(Chromosome chro: population){
            if(Math.random() < Config.mutationRate){
                int mutationNum = (int)(Math.random() * Config.MaxMutationNum);
                chro.mutation(mutationNum);
            }
        }
    }

    private void calculateScore(){
        setChromosomeScore(population.get(0));
        bestScore = population.get(0).getScore();
        bestScore = population.get(0).getScore();
        totalScore=0;

        for(Chromosome chro: population){
            setChromosomeScore(chro);
            if(chro.getScore() > bestScore){
                bestScore = chro.getScore();
                if(y < bestScore){
                    x = chagneX(chro);
                    y = bestScore;
                    geneI = generation;
                }
            }
            if(chro.getScore() < worstScore){
                worstScore = chro.getScore();
            }
            totalScore += chro.getScore();
        }

        averageScore = totalScore/Config.POPULATION_SIZE;
        averageScore = averageScore > bestScore ? bestScore:averageScore;
    }

    private void setChromosomeScore(Chromosome chromosome){
        if(chromosome == null){
            return;
        }
        double x = chagneX(chromosome);
        double y = calculateY(x);
        chromosome.setScore(y);
    }

    //transfer gene to X
    public abstract double chagneX(Chromosome chromosome);

    //calculate Y according to X. Y = F(X);
    public abstract double calculateY(double x);

    private void print(){
        System.out.println("----------------------------------------------------");
        System.out.println("the generation is:" + generation);
        System.out.println("the best y is: "+ bestScore);
        System.out.println("the worst fitness is: "+worstScore);
        System.out.println("the average fitness is: "+averageScore);
        System.out.println("the total fitness is: "+totalScore);
        System.out.println("geneI: "+geneI + "\tx:" + x + "\ty:" + y);

    }



}
