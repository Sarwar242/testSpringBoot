package com.sarwar.test.model.entity;

import org.hibernate.type.descriptor.jdbc.TinyIntJdbcType;

import com.sarwar.test.model.enums.EducationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "employee")
public class EmployeeEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private EducationType type;

    @Column(name = "institutionName", nullable = false)
    private String institutionName;

    @Column(name = "board", nullable = true)
    private String board;

    @Column(name = "passingYear", nullable = true)
    private String passingYear;

    @Column(name = "result", nullable = true)
    private String result;

    @Column(name = "scale", nullable = true)
    private String scale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id" , nullable = false)
    private Employee employee;
}