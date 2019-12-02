package edu.ufp.afmiguez.tk.esof.models.pt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Atendimento extends BaseModel {

    private Aluno aluno;

    private Explicador explicador;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime inicio;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fim;

    public Atendimento(LocalDateTime inicio, LocalDateTime end){
        this.inicio = inicio;
        this.fim =end;
    }

    public Atendimento(Explicador explainer) {
        this.explicador=explainer;
    }

    public boolean overlaps(Atendimento other) {
        return this.isBetween(other) || other.isBetween(this) ||
                (this.inicio.equals(other.inicio) && this.fim.equals(other.fim));
    }

    private boolean isBetween(Atendimento other){
        LocalDateTime appointmentStartTime=other.getInicio();
        LocalDateTime appointmentEndTime=other.getFim();
        return this.isBetween(appointmentStartTime) || this.isBetween(appointmentEndTime);
    }
    private boolean isBetween(LocalDateTime timeToCheck){
        return this.inicio.isBefore(timeToCheck) && this.fim.isAfter(timeToCheck);
    }

}
