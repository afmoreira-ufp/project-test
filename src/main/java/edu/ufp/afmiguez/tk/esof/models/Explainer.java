package edu.ufp.afmiguez.tk.esof.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Explainer extends BaseModel {

    private String username;

    private Degree degree;

    private Set<Schedule> schedules=new HashSet<>();

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    private Set<Course> courses=new HashSet<>();


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Student> students=new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Appointment> appointments=new HashSet<>();

    public Explainer(String username, Degree degree) {
        this.username = username;
        this.degree = degree;
    }

    public Explainer(String username) {
        this.setUsername(username);
    }

    public void addCourse(Course course){
        this.courses.add(course);
        course.addExplainer(this);
    }

    public void addStudent(Student student){
        this.students.add(student);
        student.addExplainer(this);
    }

    @JsonInclude
    @ToString.Include
    public Set<String> courses() {
        Set<String> names=new HashSet<>();
        for(Course course:this.courses){
            names.add(course.getName());
        }
        return names;
    }

    @JsonInclude
    @ToString.Include
    public Set<String> students() {
        Set<String> names=new HashSet<>();
        for(Student student:this.students) {
            names.add(student.getUsername());
        }
        return names;
    }

    public void update(Explainer explainer) {
        this.setStudents(explainer.getStudents());
        this.setCourses(explainer.getCourses());
    }

    public void addSchedule(Schedule schedule){
        this.schedules.add(schedule);
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
        appointment.setExplainer(this);
        Student student=appointment.getStudent();
        if(student!=null && !student.getAppointments().contains(appointment)){
            student.addAppointment(appointment);
        }
    }

    public boolean canSchedule(Appointment appointment) {
        for(Schedule schedule:this.schedules){
            if(schedule.contains(appointment)){
                return true;
            }
        }
        return false;
    }
}
