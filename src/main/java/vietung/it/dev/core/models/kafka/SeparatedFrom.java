package vietung.it.dev.core.models.kafka;

import java.util.List;

public class SeparatedFrom {
    private String id;
    private List<CountFrom> countFroms;

    public SeparatedFrom(String id, List<CountFrom> countFroms) {
        this.id = id;
        this.countFroms = countFroms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CountFrom> getCountFroms() {
        return countFroms;
    }

    public String toString (){
//        StringBuilder stringBuilder = new StringBuilder("[");
//        for(int i=0;i<this.getCountFroms().size();i++){
//            if()
//            stringBuilder.append("{ \"name \":").append(this.countFroms.get(i).getName()).append("},");
//        }
        return "";
    }

    public void setCountFroms(List<CountFrom> countFroms) {
        this.countFroms = countFroms;
    }
}
