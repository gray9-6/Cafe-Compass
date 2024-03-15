package com.inn.cafe.pojo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;


// query
@NamedQuery(name = "User.findByEmailId",query = "select u from User u where email=:email")

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;    // primary key

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;     // this will be assigned after the user got sign up

    @Column(name = "role")
    private String role;      // this will be assigned after the user got sign up
}
