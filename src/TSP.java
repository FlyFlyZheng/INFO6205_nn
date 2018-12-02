import java.io.*;
import java.util.*;


public class TSP {

    private String data_path= "/Users/liufulai/Desktop/INFO6205_nn/src/CityData.csv";
    private String data_separator = " ";

    private Entity[] entities;
    private List<Entity> sonEntities;

    private List<City> cityInfo = null;

    private double minLength = Double.MAX_VALUE;
    private Entity bestEntity = null;
    private int bestInheritance = 0;

    //read city data from city file
    private List<City> readData() throws IOException{
        List<City> cityList = new ArrayList<>();
        File cityFile = new File(this.data_path);
        FileReader fileReader = new FileReader(cityFile);
        BufferedReader br = new BufferedReader(fileReader);

        String currentLine;
        while((currentLine = br.readLine()) != null){
            int no = Integer.valueOf(currentLine.split(this.data_separator)[0]);
            int x = Integer.valueOf(currentLine.split(this.data_separator)[1]);
            int y = Integer.valueOf(currentLine.split(this.data_separator)[2]);
            cityList.add(new City(no, x, y));
        }

        return cityList;
    }

    private void initEntities(List<Integer> cityNo){
        this.entities = new Entity[Config.entity_number];

        for(int i=0; i<Config.entity_number; i++){

            Collections.shuffle(cityNo);

           // while(isExist(cityNo, entities)){
                Collections.shuffle(cityNo);
            //}

            this.entities[i] = new Entity(cityNo);
            this.entities[i].totalLength = this.calLength(cityNo);

        }
        this.setReproducePercent();

    }

    private boolean isExist(List<Integer> randomCity, Entity[] entities){
        for(int i=0; i<entities.length; i++){
            if(entities[i] == null){
                return false;
            }else{

                List<Integer> target = entities[i].cities;
                for(int j=0; j<target.size(); ++j){
                    if(randomCity.get(j) != target.get(j)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private double calLength(List<Integer> cityNo){
        double totalLength = 0.0;
        int city_Size = cityNo.size();
        for(int i=0; i<city_Size; i++){
            if(i == 0){
                totalLength += this.getDistance(cityNo.get(0), cityNo.get(city_Size-1));
            }else{
                totalLength += this.getDistance(cityNo.get(i), cityNo.get(i-1));
            }
        }

        return totalLength;
    }

    /**
     * 设置每个个体被选择繁殖的概率(根据路径长短归一化)
     * 例如有三个适应度 a b c
     * 则 a 被选择的概率为 a / (a + b + c), 以此类推...
     */
    private void setReproducePercent(){
        double sumLengthToOne = 0.0;
        for(Entity entity: this.entities){
            sumLengthToOne += 1/entity.totalLength;
        }
        for(Entity entity : this.entities){
            entity.reproducePrecent = (1 / entity.totalLength) / sumLengthToOne;
        }
    }

    private Entity getEntity(){
        Random random = new Random();
        double selectPercent = random.nextDouble(); //被选择的概率
        double distributionPercent = 0.0;
        for(int i=0; i<this.entities.length; i++){
            distributionPercent += this.entities[i].reproducePrecent;
            if(distributionPercent > selectPercent){
                return this.entities[i];
            }
        }
        return null;
    }


    private void oneHybridization(){
        //get mother and father
        Entity father = this.getEntity();

        Entity mother = this.getEntity();

        Entity best = this.getBestParent();


        while(father == null || father.equals(best)){
            father = this.getEntity();
        }

        int cnt = 0;
        while(mother == null || father.equals(mother) || mother.equals(best)){
            if(cnt > Config.entity_number/2){
                break;
            }
            cnt ++;
            mother = this.getEntity();
        }

        //get swap location
        Random random = new Random();
        //System.out.println("141");
        int crossStartIndex = random.nextInt(Config.city_number - 1) + 1;
        int crossEndIndex = random.nextInt(Config.city_number - 1) + 1;
        while(crossEndIndex == crossStartIndex){
            crossEndIndex = random.nextInt(Config.city_number - 1) + 1;
        }
        if(crossStartIndex > crossEndIndex){
            int temp = crossEndIndex;
            crossEndIndex = crossStartIndex;
            crossStartIndex = temp;
        }
        //System.out.println("152");
        //swap
        Entity[] newEntity = this.swapAndNoConflict(father, mother, crossStartIndex, crossEndIndex);
        for(Entity entity: newEntity){

            if(entity != null
                    && !isExist(entity.cities, entityListToEntityArrray(this.sonEntities))
                    && !isExist(entity.cities, this.entities)){
                this.sonEntities.add(entity);
            }
        }


    }

    private Entity getBestParent(){
        if(this.bestEntity == null){
            Entity bestParent = this.entities[0].clone();
            for(int i=1; i<this.entities.length; ++i){
                if(this.entities[i].totalLength < bestParent.totalLength){
                    bestParent = this.entities[i].clone();
                }
            }
            return bestParent;
        }else{
            return this.bestEntity.clone();
        }
    }

    private Entity[] swapAndNoConflict(Entity father, Entity mother, int startIndex, int endIndex){

        Entity[] newEntities = new Entity[2];
        Entity fatherClone = father.clone();
        Entity motherClone = mother.clone();
        Map<Integer, Integer> fatherCityRealtion = new HashMap<>();
        Map<Integer, Integer> motherCityRelation = new HashMap<>();
        for(int i=0; i<Config.city_number; i++){
            if(i>=startIndex && i<=endIndex){

                int temp = fatherClone.cities.get(i);
                fatherClone.cities.set(i, motherClone.cities.get(i));
                motherClone.cities.set(i, temp);

                fatherCityRealtion.put(fatherClone.cities.get(i), motherClone.cities.get(i));
                motherCityRelation.put(motherClone.cities.get(i), fatherClone.cities.get(i));
            }
        }

        this.handleConflict(fatherClone, fatherCityRealtion, startIndex, endIndex);
        this.handleConflict(motherClone, motherCityRelation, startIndex, endIndex);
        newEntities[0] = fatherClone;
        newEntities[1] = motherClone;

        return newEntities;
    }

    public void handleConflict(Entity entity, Map<Integer, Integer> cityRelation, int start, int end){

        while(conflictExist(entity, cityRelation, start, end)){

            for(int i=0; i<entity.cities.size(); ++i){

                if(i<start || i>end){
                    int temp = entity.cities.get(i);
                    if(cityRelation.containsKey(temp)){
                        entity.cities.set(i, cityRelation.get(temp));
                    }
                }
            }
        }
    }

    public boolean conflictExist(Entity entity, Map<Integer, Integer> cityRelation, int start, int end){
        for(int i=0; i<entity.cities.size(); i++){
            if(i<start || i>end){
                if(cityRelation.containsKey(entity.cities.get(i))){

                    return true;
                }
            }
        }

        return false;
    }

    //Mutation变异
    private void mutation(Entity entity){
        Random random = new Random();
        int index1 = random.nextInt(Config.city_number -1) + 1;
        int index2 = random.nextInt(Config.city_number -1) + 1;
        while(index1 == index2){
            index2 = random.nextInt(Config.city_number -1) + 1;
        }
        int temp = entity.cities.get(index1);
        entity.cities.set(index1, entity.cities.get(index2));
        entity.cities.set(index2, temp);
    }

    private void updateEntity(){
        for(Entity entity: this.sonEntities){
            double length = 0.0;
            List<Integer> city = entity.cities;
            for(int j=0; j<city.size(); j++){
                if(j==0){
                    length += this.getDistance(city.get(city.size()-1), city.get(0));
                }else{
                    length += this.getDistance(city.get(j), city.get(j-1));
                }
            }
            entity.totalLength = length;
        }
        List<Entity> allEntities = new ArrayList<>();
        allEntities.addAll(this.sonEntities);
        allEntities.addAll(entityArrayToEntityList(this.entities));
        Collections.sort(allEntities);

        List<Entity> bestEntities = new ArrayList<>();
        for(int i=0; i<Config.entity_number; i++){
            bestEntities.add(allEntities.get(i).clone());
        }
        Collections.shuffle(bestEntities);
        for(int i=0; i<this.entities.length; i++){
            this.entities[i] = bestEntities.get(i);
        }
        this.setReproducePercent();

    }

    private void chooes(int geneticNumber){
        boolean isUpdate = false;
        for(int i=0; i<this.entities.length; i++){
            if(this.entities[i].totalLength < this.minLength){
                pppString("IN choose function");
                this.bestEntity = this.entities[i].clone();
                this.bestEntity.reproducePrecent = this.entities[i].reproducePrecent;
                this.minLength = this.entities[i].totalLength;
                this.bestInheritance = geneticNumber;
                isUpdate = true;
            }
        }
        if(isUpdate){
            System.out.println("Best Solution: "
                    + "\n genetric: "+ this.bestInheritance
                    + "\n totalLength: " + (int)(this.minLength/Math.sqrt(10.0))
                    + "\n fitness: "+this.bestEntity.reproducePrecent
                    + "\n path: "+this.bestEntity.toString());
        }
    }

    public void init() throws IOException{
        List<City> cityList = this.readData();

        this.cityInfo = new ArrayList<>();
        this.cityInfo.addAll(cityList);
        List<Integer> cityNo = new ArrayList<>();
        for(int i=0; i<cityList.size(); i++){

            cityNo.add(cityList.get(i).no);
        }
        this.initEntities(cityNo);

    }



    private double getDistance(int from, int to){
        int x1 = this.cityInfo.get(from-1).x;
        int y1 = this.cityInfo.get(from-1).y;
        int x2 = this.cityInfo.get(to-1).x;
        int y2 = this.cityInfo.get(to-1).y;
        return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
    }

    private void genetic(){
        for(int i=0; i<Config.inheritance_number; i++){
            System.out.println("genetic No: "+i);
            this.oneGenetic(i + 1);
        }
    }

    private void oneGenetic(int geneticNumber){
        this.sonEntities = new ArrayList<>();

        this.hybridization();
        this.variableOnce();
        this.updateEntity();
        this.chooes(geneticNumber);
    }

    private void hybridization(){
        Random random = new Random();
        for(int i=0; i<Config.entity_number/2+1; i++){
            this.oneHybridization();
        }
    }

    private void variableOnce(){
        Random random = new Random();
        double percent = random.nextDouble();
        for(Entity entity : this.sonEntities){
            if(percent < Config.variable_percent){
                this.mutation(entity);
            }
            percent = random.nextDouble();
        }
    }

    private void variableMore(){
        Random random = new Random();
        double percent = random.nextDouble();
        for(Entity entity : this.sonEntities){
            if(percent < Config.variable_percent){
                int count = random.nextInt(Config.city_number);
                for(int i=0; i<count; i++){
                    this.mutation(entity);
                }
            }
            percent = random.nextDouble();
        }
    }



    private Entity[] entityListToEntityArrray(List<Entity> list){
        Entity[] arr = new Entity[list.size()];
        for(int i=0; i<list.size(); i++){
            arr[i] = list.get(i).clone();
        }
        return arr;
    }

    private List<Entity> entityArrayToEntityList(Entity[] entities){
        List<Entity> list = new ArrayList<>();
        for(Entity entity: entities){
            list.add(entity.clone());
        }
        return list;
    }

    public static void main(String[] args){
        try{
            TSP tsp = new TSP();
            System.out.println("Before init");
            tsp.init();
            System.out.print("After init :" );
            for(int i=0; i<tsp.entities.length; i++) {
                System.out.println(tsp.entities[i].toString() + " totalLength - > " +
                        tsp.entities[i].totalLength + "reproductPercent - >" + tsp.entities[i].reproducePrecent);
            }
         //   System.out.println("Before genetic");
                tsp.genetic();
            System.out.println("After genetic");
            System.out.println("Global best Solution is"
                    +"\n  Apperence Generation"+tsp.bestInheritance
                    +"\n  The length of Path"+(tsp.minLength/Math.sqrt(10.0))
                    +"\n  The fitness Score is "+ tsp.bestEntity.reproducePrecent
                    +"\n  The whole Path is"+ tsp.bestEntity.toString());



        }catch(IOException e){
            e.printStackTrace();
        }


    }
    private void pppint(int o){
        System.out.println(o);
    }
    private void pppString(String o){
        System.out.println(o);
    }








}
