package vti.authenticationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vti.authenticationservice.client.AccountClient;
import vti.authenticationservice.request.AuthenticationRequest;
import vti.authenticationservice.response.AuthenticationResponse;
import vti.authenticationservice.service.AuthenticationService;
import vti.common.config.JwtService;
import vti.common.dto.AccountDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountClient accountClient;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Authenticating user: {}", request.getUsername());
        AccountDto account = accountClient.getAccountDto(request);
        if (account == null || !passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new BadCredentialsException("Account not found");
        }
        String jwtToken = jwtService.generateToken(account);
        Long expiresIn = jwtService.getExpirationTime(jwtToken);
        return new AuthenticationResponse(jwtToken, expiresIn);
    }
}
