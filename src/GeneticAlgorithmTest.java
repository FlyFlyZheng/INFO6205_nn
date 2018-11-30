public class GeneticAlgorithmTest extends GeneticAlgorithm{

    public static final int NUM = 1 << 24;

    public GeneticAlgorithmTest(){
        super(24);
    }

    @Override
    public double chagneX(Chromosome chromosome) {
        return ((1.0 * chromosome.getNum()/NUM) * 100) + 6;
    }

    @Override
    public double calculateY(double x) {
        return 100 - Math.log(x);
    }

    public static void main(String[] args){
        GeneticAlgorithm test = new GeneticAlgorithmTest();
        test.caculte();
    }



}
