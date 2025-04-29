package vti.common.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vti.common.dto.AccountDto;
import vti.common.enums.Role;

import java.util.List;

public class AccountMapper {

    private AccountMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static AccountDto toDto(AccountDto account) {
        Role role = account.getRole();
        List<String> authorities = role.getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AccountDto.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .password(account.getPassword())
                .fullName(account.getFullName())
                .authorities(authorities)
                .build();
    }
}
