package edu.ufp.afmiguez.tk.esof.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Appointment extends BaseModel {

    private Student student;

    private Explainer explainer;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expectedEndTime;

    public Appointment(LocalDateTime startTime, LocalDateTime end){
        this.startTime=startTime;
        this.expectedEndTime=end;
    }

    public Appointment(Explainer explainer) {
        this.setExplainer(explainer);
    }

    public boolean overlaps(Appointment other) {
        return this.isBetween(other) || other.isBetween(this) ||
                (this.startTime.equals(other.startTime) && this.expectedEndTime.equals(other.expectedEndTime));
    }

    private boolean isBetween(Appointment other){
        LocalDateTime appointmentStartTime=other.getStartTime();
        LocalDateTime appointmentEndTime=other.getExpectedEndTime();
        return this.isBetween(appointmentStartTime) || this.isBetween(appointmentEndTime);
    }
    private boolean isBetween(LocalDateTime timeToCheck){
        return this.startTime.isBefore(timeToCheck) && this.expectedEndTime.isAfter(timeToCheck);
    }

}
