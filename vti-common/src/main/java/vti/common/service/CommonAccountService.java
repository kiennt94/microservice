package vti.common.service;

import org.springframework.stereotype.Service;
import vti.common.dto.AccountDto;

@Service
public interface CommonAccountService {
    AccountDto findByUsername(String username, String token);
}
