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
public class Cadeira extends BaseModel {

    private String nome;

    private int ects;
    private int semestre;
    private int ano;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Curso curso;

    private Set<Explicador> explicadores =new HashSet<>();

    protected Cadeira(String nome, int ects, int semestre, int ano, Curso curso) {
        this.nome = nome;
        this.ects = ects;
        this.semestre = semestre;
        this.ano = ano;
        this.curso = curso;
    }

    public void addExplainer(Explicador explicador){
        this.explicadores.add(explicador);
    }
    public void removeExplainer(Explicador explicador){this.explicadores.remove(explicador);}
}

