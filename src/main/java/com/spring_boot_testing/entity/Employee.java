package com.spring_boot_testing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import lombok.*;

//import java.util.Locale.Builder;

@Setter
@Getter
//@AllArgsConstructor
@NoArgsConstructor
//@Builder
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;

    private Employee( String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    private Employee(long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private long id;
        private String firstName;

        private String lastName;

        private String email;

        public Builder() {
        }
        public Builder id(long id){
            this.id = id;
            return this;
        }
        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }
        public Builder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }
        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Employee build(){
            return new Employee(id, firstName, lastName, email);
        }
    }
}
