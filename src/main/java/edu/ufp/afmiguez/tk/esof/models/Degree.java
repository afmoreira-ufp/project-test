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
public class Degree extends BaseModel {

    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private College college;

    private Set<Course> courses=new HashSet<>();

    public Degree(String name) {
        this.name=name;
    }

    public void addCourse(Course course){
        this.courses.add(course);
        course.setDegree(this);
    }

    public void removeCourse(Course course){
        this.courses.remove(course);
    }

}
