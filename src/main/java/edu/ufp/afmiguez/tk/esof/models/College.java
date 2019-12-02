package edu.ufp.afmiguez.tk.esof.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class College extends BaseModel {
    private String name;
    public Set<Degree> degrees=new HashSet<>();

    public College(String name) {
        this.name=name;
    }

    public void addDegree(Degree degree){
        this.degrees.add(degree);
        degree.setCollege(this);
    }

    public void removeDegree(Degree degree){
        this.degrees.remove(degree);
    }
}
