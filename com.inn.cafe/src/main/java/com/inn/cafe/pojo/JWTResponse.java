package com.inn.cafe.pojo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTResponse {
    String jwtToken;
    String userName;
}
