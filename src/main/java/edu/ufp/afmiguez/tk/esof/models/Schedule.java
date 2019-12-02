package edu.ufp.afmiguez.tk.esof.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Schedule extends BaseModel {

    private DayOfWeek dayOfWeek;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime start;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime end;

    public boolean contains(Appointment appointment){
        DayOfWeek dayOfWeek=appointment.getStartTime().getDayOfWeek();
        if(dayOfWeek.equals(this.dayOfWeek)){
            LocalTime appointmentStart=appointment.getStartTime().toLocalTime();
            LocalTime appointmentEnd=appointment.getExpectedEndTime().toLocalTime();
            return this.contains(appointmentStart,appointmentEnd);
        }
        return false;
    }

    private boolean contains(LocalTime start, LocalTime end){
        return (this.start.isBefore(start) || this.start.equals(start))
                &&
                (this.end.isAfter(end) || this.end.equals(end)) ;
    }
}
