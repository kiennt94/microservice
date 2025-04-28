package vti.accountmanagement.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.accountmanagement.model.dto.account.AccountDto;
import vti.accountmanagement.payload.PageResponse;
import vti.accountmanagement.request.account.AccountCreateRequest;
import vti.accountmanagement.request.account.AccountUpdateRequest;
import vti.accountmanagement.request.authenticate.AuthenticationRequest;
import vti.accountmanagement.response.dto.account.AccountInfoDto;
import vti.accountmanagement.response.dto.account.AccountListDto;


@Service
public interface AccountService {
    PageResponse<AccountListDto> getAll(Pageable pageable, String search);
    AccountInfoDto getAccountById(int id);
    void save(AccountCreateRequest account);
    void update(AccountUpdateRequest account);
    void delete(Integer id);
    AccountDto findByUsername(String username);
    AccountDto auth(AuthenticationRequest request);
}
