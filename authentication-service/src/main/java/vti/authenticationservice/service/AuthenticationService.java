package vti.authenticationservice.service;

import org.springframework.stereotype.Service;
import vti.authenticationservice.request.AuthenticationRequest;
import vti.authenticationservice.response.AuthenticationResponse;

@Service
public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticate);
}
