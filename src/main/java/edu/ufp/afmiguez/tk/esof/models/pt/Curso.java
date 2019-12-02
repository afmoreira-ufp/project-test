package edu.ufp.afmiguez.tk.esof.models.pt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Curso extends BaseModel {

    private String nome;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Faculdade faculdade;

    private Set<Cadeira> cadeiras =new HashSet<>();

    public Curso(String nome) {
        this.nome = nome;
    }

    public void addCourse(Cadeira cadeira){
        this.cadeiras.add(cadeira);
        cadeira.setCurso(this);
    }

    public void removeCourse(Cadeira cadeira){
        this.cadeiras.remove(cadeira);
    }

}
