package edu.ufp.afmiguez.tk.esof.models.pt;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Faculdade extends BaseModel {
    private String nome;
    public Set<Curso> cursos =new HashSet<>();

    public Faculdade(String nome) {
        this.nome = nome;
    }

    public void addDegree(Curso curso){
        this.cursos.add(curso);
        curso.setFaculdade(this);
    }

    public void removeDegree(Curso curso){
        this.cursos.remove(curso);
    }
}
