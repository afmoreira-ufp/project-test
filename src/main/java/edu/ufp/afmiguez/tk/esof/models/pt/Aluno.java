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
public class Aluno extends BaseModel {

    private String utilizador;

    private Set<Explicador> explicadores =new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Atendimento> atendimentos =new HashSet<>();

    public Aluno(String username) {
        this.setUtilizador(username);
    }

    public void addExplainer(Explicador explicador){
        this.explicadores.add(explicador);
    }

    public void addAppointment(Atendimento atendimento){
        this.atendimentos.add(atendimento);
        atendimento.setAluno(this);
    }
}