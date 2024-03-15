package com.inn.cafe.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)   // set each fields in the class as private
@Builder
public class UserResponseDto {
    String name;
    String contactNumber;
    String email;
    String password;
}
