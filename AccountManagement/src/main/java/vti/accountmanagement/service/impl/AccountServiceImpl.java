package vti.accountmanagement.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vti.accountmanagement.enums.Role;
import vti.accountmanagement.exception.NotFoundException;
import vti.accountmanagement.exception.DuplicateException;
import vti.accountmanagement.model.Account;
import vti.accountmanagement.model.Department;
import vti.accountmanagement.model.Position;
import vti.accountmanagement.model.dto.account.AccountDto;
import vti.accountmanagement.payload.PageResponse;
import vti.accountmanagement.repository.AccountRepository;
import vti.accountmanagement.repository.DepartmentRepository;
import vti.accountmanagement.repository.PositionRepository;
import vti.accountmanagement.request.account.AccountCreateRequest;
import vti.accountmanagement.request.account.AccountUpdateRequest;
import vti.accountmanagement.request.authenticate.AuthenticationRequest;
import vti.accountmanagement.response.dto.account.AccountInfoDto;
import vti.accountmanagement.response.dto.account.AccountListDto;
import vti.accountmanagement.service.AccountService;
import vti.accountmanagement.utils.MessageUtil;
import vti.accountmanagement.utils.ObjectMapperUtils;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();
    private final PasswordEncoder passwordEncoder;
    private static final String ACCOUNT_ID_NOT_EXISTS = "account.id.not.exists";

    @Override
    public PageResponse<AccountListDto> getAll(Pageable pageable, String search) {
        objectMapperUtils.getModelMapper().typeMap(Account.class, AccountListDto.class)
                .addMappings(m -> {
                    m.map(acc -> acc.getDepartment().getDepartmentName(), AccountListDto::setDepartmentName);
                    m.map(acc -> acc.getPosition().getPositionName(), AccountListDto::setPositionName);
                });
        Page<Account> accounts = accountRepository.findAll(pageable, search);
        return new PageResponse<>(objectMapperUtils.mapEntityPageIntoDtoPage(accounts, AccountListDto.class));
    }

    @Override
    public AccountInfoDto getAccountById(int id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new NotFoundException(MessageUtil.getMessage(ACCOUNT_ID_NOT_EXISTS));
        }
        objectMapperUtils.getModelMapper().typeMap(Account.class, AccountInfoDto.class)
                .addMappings(m -> {
                    m.map(acc -> acc.getDepartment().getDepartmentName(), AccountInfoDto::setDepartmentName);
                    m.map(acc -> acc.getPosition().getPositionName(), AccountInfoDto::setPositionName);
                });
        return objectMapperUtils.map(account, AccountInfoDto.class);
    }

    @Override
    public void save(AccountCreateRequest account) {
        if (Boolean.TRUE.equals(accountRepository.existsAccountByEmail(account.getEmail()))) {
            throw new DuplicateException(MessageUtil.getMessage("account.email.exists"));
        }
        if (Boolean.TRUE.equals(accountRepository.existsAccountByUsername(account.getUsername()))) {
            throw new DuplicateException(MessageUtil.getMessage("account.username.exists"));
        }
        if (!positionRepository.existsById(account.getPositionId())) {
            throw new NotFoundException(MessageUtil.getMessage("position.id.not.exists"));
        }
        if (!departmentRepository.existsById(account.getDepartmentId())) {
            throw new NotFoundException(MessageUtil.getMessage("department.id.not.exists"));
        }
        Account acc = objectMapperUtils.map(account, Account.class);
        acc.setDepartment(new Department(account.getDepartmentId()));
        acc.setPosition(new Position(account.getPositionId()));
        acc.setPassword(passwordEncoder.encode(acc.getPassword()));
        acc.setRole(Role.valueOf(account.getRole().toUpperCase()));
        accountRepository.save(acc);
    }

    @Override
    public void update(AccountUpdateRequest account) {
        Account acc = accountRepository.findById(account.getAccountId()).orElse(null);
        if (acc == null) {
            throw new NotFoundException(MessageUtil.getMessage(ACCOUNT_ID_NOT_EXISTS));
        }
        if (Boolean.TRUE.equals(accountRepository.existsAccountByEmailAndAccountIdNot(account.getEmail(), account.getAccountId()))) {
            throw new DuplicateException(MessageUtil.getMessage("account.email.exists"));
        }
        if (!positionRepository.existsById(account.getPositionId())) {
            throw new NotFoundException(MessageUtil.getMessage("position.id.not.exists"));
        }
        if (!departmentRepository.existsById(account.getDepartmentId())) {
            throw new NotFoundException(MessageUtil.getMessage("department.id.not.exists"));
        }
        objectMapperUtils.getModelMapper().map(account, acc);
        acc.setPosition(new Position(account.getPositionId()));
        acc.setDepartment(new Department(account.getDepartmentId()));
        accountRepository.save(acc);
    }

    @Override
    public void delete(Integer id) {
        Account acc = accountRepository.findById(id).orElse(null);
        if (acc == null) {
            throw new NotFoundException(MessageUtil.getMessage(ACCOUNT_ID_NOT_EXISTS));
        }
        accountRepository.delete(acc);
    }

    @Override
    public AccountDto findByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElse(null);
        if (account == null) {
            return null;
        }
        return objectMapperUtils.map(account, AccountDto.class);
    }

    @Override
    public AccountDto auth(AuthenticationRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername()).orElse(null);
        if (account == null || !passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return null;
        }
        return objectMapperUtils.map(account, AccountDto.class);
    }
}
