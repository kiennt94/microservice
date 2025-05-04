package vti.accountservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.accountservice.request.account.AccountCreateRequest;
import vti.accountservice.request.account.AccountUpdateRequest;
import vti.accountservice.response.dto.account.AccountInfoDto;
import vti.accountservice.response.dto.account.AccountListDto;
import vti.common.payload.PageResponse;

import java.util.List;


@Service
public interface AccountService  {
    PageResponse<AccountListDto> getAll(Pageable pageable, String search);
    AccountInfoDto getAccountById(int id);
    void save(AccountCreateRequest account);
    void update(AccountUpdateRequest account);
    void delete(Integer id);
    List<AccountInfoDto> getAccountByDepartmentId(int departmentId);
    List<String> getAllRoles();
}
