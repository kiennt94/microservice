package vti.accountmanagement.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.accountmanagement.request.account.AccountCreateRequest;
import vti.accountmanagement.request.account.AccountUpdateRequest;
import vti.accountmanagement.request.authenticate.AuthenticationRequest;
import vti.accountmanagement.response.dto.account.AccountInfoDto;
import vti.accountmanagement.response.dto.account.AccountListDto;
import vti.common.dto.AccountDto;
import vti.common.payload.PageResponse;
import vti.common.service.CommonAccountService;


@Service
public interface AccountService extends CommonAccountService {
    PageResponse<AccountListDto> getAll(Pageable pageable, String search);
    AccountInfoDto getAccountById(int id);
    void save(AccountCreateRequest account);
    void update(AccountUpdateRequest account);
    void delete(Integer id);
    AccountDto auth(AuthenticationRequest request);
}
