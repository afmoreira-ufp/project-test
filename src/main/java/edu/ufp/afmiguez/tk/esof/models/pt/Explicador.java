package edu.ufp.afmiguez.tk.esof.models.pt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.ufp.afmiguez.tk.esof.models.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Explicador extends BaseModel {

    private String utilizador;

    private Curso curso;

    private Set<Disponibilidade> disponibilidades =new HashSet<>();

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    private Set<Cadeira> cadeiras =new HashSet<>();


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Aluno> alunos =new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Atendimento> atendimentos =new HashSet<>();

    public Explicador(String utilizador, Curso curso) {
        this.utilizador = utilizador;
        this.curso = curso;
    }

    public Explicador(String utilizador) {
        this.setUtilizador(utilizador);
    }

    public void addCourse(Cadeira cadeira){
        this.cadeiras.add(cadeira);
        cadeira.addExplainer(this);
    }

    public void addStudent(Aluno aluno){
        this.alunos.add(aluno);
        aluno.addExplainer(this);
    }

    @JsonInclude
    @ToString.Include
    public Set<String> courses() {
        Set<String> names=new HashSet<>();
        for(Cadeira cadeira :this.cadeiras){
            names.add(cadeira.getNome());
        }
        return names;
    }

    @JsonInclude
    @ToString.Include
    public Set<String> students() {
        Set<String> names=new HashSet<>();
        for(Aluno aluno :this.alunos) {
            names.add(aluno.getUtilizador());
        }
        return names;
    }

    public void update(Explicador explicador) {
        this.setAlunos(explicador.getAlunos());
        this.setCadeiras(explicador.getCadeiras());
    }

    public void addSchedule(Disponibilidade disponibilidade){
        this.disponibilidades.add(disponibilidade);
    }

    public void addAppointment(Atendimento atendimento){
        this.atendimentos.add(atendimento);
        atendimento.setExplicador(this);
        Aluno aluno = atendimento.getAluno();
        if(aluno !=null && !aluno.getAtendimentos().contains(atendimento)){
            aluno.addAppointment(atendimento);
        }
    }

    public boolean canSchedule(Atendimento appointment) {
        for(Disponibilidade schedule:this.disponibilidades){
            if(schedule.contains(appointment)){
                return true;
            }
        }
        return false;
    }

}
