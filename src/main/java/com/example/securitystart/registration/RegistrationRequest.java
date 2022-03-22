package com.example.securitystart.registration;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    String firstName;
    String lastName;
    String password;
    String email;
}
