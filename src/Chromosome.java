import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    public boolean[] gene;
    public double score;

    //random generate gene array
    public Chromosome(int size){
        if(size<=0){
            return;
        }
        initGeneSize(size);
        for(int i=0; i<size; i++){
            gene[i] = Math.random() >= 0.5;
        }
    }

    public void initGeneSize(int size){
        if(size<=0){
            return;
        }
        gene = new boolean[size];
    }

    //transfer gene array to a num， 位运算 ??
    public int getNum(){
        if(gene == null){
            return 0;
        }
        int num = 0;
        for(boolean bool : gene){
            num <<= 1; // num * 2
            if(bool){
                num+=1;
            }
        }
        return num;
    }

    public void mutation(int num){
        int size = gene.length;
        for(int i=0; i<num; i++){
            //find mutation location
            int at = ((int)(Math.random() * size)) % size;
            //change value
            boolean bool = !gene[at];
            gene[at] = bool;
        }
    }

    public static Chromosome clone(final Chromosome chromosome){
        if(chromosome == null || chromosome.gene == null){
            return null;
        }
        Chromosome copy = new Chromosome(chromosome.gene.length);
        copy.initGeneSize(chromosome.gene.length);
        for(int i=0; i<chromosome.gene.length; i++){
            copy.gene[i] = chromosome.gene[i];
        }
        return copy;
    }

    public static List<Chromosome> genetic(Chromosome parent1, Chromosome parent2){
        if(parent1 == null || parent2 == null){
            return  null;
        }

        if(parent1.gene == null || parent2.gene == null){
            return null;
        }

        if(parent1.gene.length != parent2.gene.length){
            return null;
        }

        Chromosome child1 = clone(parent1);
        Chromosome child2 = clone(parent2);

        int size = child1.gene.length;
        int a = ((int)(Math.random() * size)) % size;
        int b = ((int)(Math.random() * size)) % size;
        int min, max;
        if(a>b){
            max = a;
            min = b;
        }else{
            max = b;
            min = a;
        }

        for(int i=min; i<=max; i++){
            boolean tmp = child1.gene[i];
            child1.gene[i] = child2.gene[i];
            child2.gene[i] = tmp;
        }

        List<Chromosome> childs = new ArrayList<>();
        childs.add(child1);
        childs.add(child2);
        return childs;
    }

    public double getScore(){
        return score;
    }

    public void setScore(double score){
        this.score = score;
    }


}
