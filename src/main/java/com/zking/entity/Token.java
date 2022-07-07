package com.zking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String grantType="authorization_code";
    private String clientId="2694c10f42c9d33d9257e5693bcd5183506ee62e5b00396e13e8e5b80d882048";
    private String redirectUrl="http://localhost:8081/getCode";
    private String code;
    private String state;
    private String clientSecret="6d046cc842f9e1924566d1c16e46350f4968354a98f339784ef6395bd6577f4d";
}
