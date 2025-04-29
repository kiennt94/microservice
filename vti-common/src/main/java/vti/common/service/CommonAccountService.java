package vti.common.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CommonAccountService {
    UserDetails findByUsername(String username) throws UsernameNotFoundException;
}
