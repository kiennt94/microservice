package vti.authenticationservice.service;

import org.springframework.stereotype.Service;
import vti.authenticationservice.request.AuthenticationRequest;
import vti.authenticationservice.response.AuthenticationResponse;
import vti.common.service.CommonAccountService;

@Service
public interface AuthenticationService extends CommonAccountService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticate);
}
