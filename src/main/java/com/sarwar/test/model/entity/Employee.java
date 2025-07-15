package com.sarwar.test.model.entity;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "educationDetails")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "age", nullable = false)
    private int age;
    @Column(name = "gender", nullable = true)
    private String gender;
    @Column(name = "dob", nullable = true)
    private Date dob;
    @Column(name = "birth_place", nullable = true)
    private String birthPlace;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeEducation> educationDetails;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


}
