package edu.ufp.afmiguez.tk.esof.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Course extends BaseModel  {

    private String name;

    private int ects;
    private int semester;
    private int year;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Degree degree;

    private Set<Explainer> explainers=new HashSet<>();

    protected Course(String name, int ects, int semester, int year, Degree degree) {
        this.name = name;
        this.ects = ects;
        this.semester = semester;
        this.year = year;
        this.degree = degree;
    }

    public void addExplainer(Explainer explainer){
        this.explainers.add(explainer);
    }
    public void removeExplainer(Explainer explainer){this.explainers.remove(explainer);}
}

