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
public class Student extends BaseModel {

    private String username;

    private Set<Explainer> explainers=new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Appointment> appointments=new HashSet<>();

    public Student(String username) {
        this.setUsername(username);
    }

    public void addExplainer(Explainer explainer){
        this.explainers.add(explainer);
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
        appointment.setStudent(this);
    }
}