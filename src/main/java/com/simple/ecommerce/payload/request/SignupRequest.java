package com.simple.ecommerce.payload.request;

import lombok.Data;

import java.util.Set;

import javax.validation.constraints.*;

@Data
public class SignupRequest {

    private String username;

    private String name;

    private int type;

    private Set<String> role;

    private String password;

    private int age;

}
