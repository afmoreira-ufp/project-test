package edu.ufp.afmiguez.tk.esof.models.pt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class Disponibilidade extends BaseModel {

    private DayOfWeek diaDaSemana;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime inicio;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime fim;

    public boolean contains(Atendimento atendimento){
        DayOfWeek dayOfWeek= atendimento.getInicio().getDayOfWeek();
        if(dayOfWeek.equals(this.diaDaSemana)){
            LocalTime appointmentStart= atendimento.getInicio().toLocalTime();
            LocalTime appointmentEnd= atendimento.getFim().toLocalTime();
            return this.contains(appointmentStart,appointmentEnd);
        }
        return false;
    }

    private boolean contains(LocalTime start, LocalTime end){
        return (this.inicio.isBefore(start) || this.inicio.equals(start))
                &&
                (this.fim.isAfter(end) || this.fim.equals(end)) ;
    }
}
