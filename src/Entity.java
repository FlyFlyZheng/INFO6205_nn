import java.util.ArrayList;
import java.util.List;

public class Entity implements Comparable<Entity>{
    public List<Integer> cities;
    public double totalLength;
    public double reproducePrecent;

    public Entity(List<Integer> cities) {
        this.cities = cities;
        this.cities.addAll(cities);
    }

    public Entity clone(){
        List<Integer> list = new ArrayList<>();
        list.addAll(this.cities);
        Entity clone = new Entity(list);
        clone.totalLength = this.totalLength;
        clone.reproducePrecent = this.reproducePrecent;
        return clone;
    }

    public boolean equals(Entity entity){
        List<Integer> antoherCities = entity.cities;
        for(int i=0; i<this.cities.size(); i++){
            if(this.cities.get(i) == antoherCities.get(i)){
                return false;
            }
        }
        return true;
    }

    public String toString(){
        return this.cities.toString();
    }

    @Override
    public int compareTo(Entity o) {
        return this.sign(this.totalLength - o.totalLength);
    }

    public int sign(double x){
        return (x>0) ? 1 : ((x==0) ? 0: -1);
    }
}