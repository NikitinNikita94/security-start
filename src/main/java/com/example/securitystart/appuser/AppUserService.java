package com.example.securitystart.appuser;

import com.example.securitystart.registration.token.ConfirmationToken;
import com.example.securitystart.registration.token.ConfirmationTokenRepository;
import com.example.securitystart.registration.token.ConfirmationTokenService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "user with email %s not found";

    AppUserRepository appUserRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND, email)));
    }

    public String singUpUser(final AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        String passwordEncoded = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(passwordEncoded);
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken
        );

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
