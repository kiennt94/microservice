package vti.accountmanagement.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vti.accountmanagement.client.DepartmentClient;
import vti.accountmanagement.client.PositionClient;
import vti.accountmanagement.model.Account;
import vti.accountmanagement.repository.AccountRepository;
import vti.accountmanagement.request.account.AccountCreateRequest;
import vti.accountmanagement.request.account.AccountUpdateRequest;
import vti.accountmanagement.request.authenticate.AuthenticationRequest;
import vti.accountmanagement.response.dto.account.AccountInfoDto;
import vti.accountmanagement.response.dto.account.AccountListDto;
import vti.accountmanagement.service.AccountService;
import vti.common.config.JwtService;
import vti.common.dto.AccountDto;
import vti.common.enums.PositionName;
import vti.common.enums.Role;
import vti.common.exception_handler.DuplicateException;
import vti.common.exception_handler.NotFoundException;
import vti.common.payload.PageResponse;
import vti.common.utils.MessageUtil;
import vti.common.utils.ObjectMapperUtils;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DepartmentClient departmentClient;
    private final PositionClient positionClient;

    private static final String ACCOUNT_ID_NOT_EXISTS = "account.id.not.exists";
    private static final String ACCOUNT_USERNAME_NOT_EXISTS = "account.username.not.exists";
    private static final String ACCOUNT_EMAIL_EXISTS = "account.email.exists";
    private static final String ACCOUNT_USERNAME_EXISTS = "account.username.exists";

    @Override
    public PageResponse<AccountListDto> getAll(Pageable pageable, String search) {
        Page<Account> accounts = accountRepository.findAll(pageable, search);

        List<Integer> departmentIds = accounts.getContent().stream()
                .map(Account::getDepartmentId)
                .distinct()
                .toList();

        List<Integer> positionIds = accounts.getContent().stream()
                .map(Account::getPositionId)
                .distinct()
                .toList();

        Map<Integer, String> departmentMap = departmentClient.getDepartmentsAsMap(departmentIds);
        Map<Integer, PositionName> positionMap = positionClient.getPositionsAsMap(positionIds);

        List<AccountListDto> accountListDtos = accounts.getContent().stream()
                .map(account -> {
                    String departmentName = departmentMap.get(account.getDepartmentId());
                    PositionName positionName = positionMap.get(account.getPositionId());
                    return AccountListDto.builder()
                            .accountId(account.getAccountId())
                            .username(account.getUsername())
                            .email(account.getEmail())
                            .fullName(account.getFullName())
                            .createDate(account.getCreateDate())
                            .role(account.getRole())
                            .departmentId(account.getDepartmentId())
                            .departmentName(departmentName)
                            .positionId(account.getPositionId())
                            .positionName(positionName)
                            .build();
                })
                .toList();
        Page<AccountListDto> accountListDtoPage = new PageImpl<>(accountListDtos, pageable, accounts.getTotalElements());
        return PageResponse.fromPage(accountListDtoPage);
    }

    @Override
    public AccountInfoDto getAccountById(int id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new NotFoundException(MessageUtil.getMessage(ACCOUNT_ID_NOT_EXISTS));
        }
//        objectMapperUtils.getModelMapper().typeMap(Account.class, AccountInfoDto.class)
//                .addMappings(m -> {
//                    m.map(acc -> acc.getDepartment().getDepartmentName(), AccountInfoDto::setDepartmentName);
//                    m.map(acc -> acc.getPosition().getPositionName(), AccountInfoDto::setPositionName);
//                });
        return objectMapperUtils.map(account, AccountInfoDto.class);
    }

    @Override
    public void save(AccountCreateRequest account) {
        if (Boolean.TRUE.equals(accountRepository.existsAccountByEmail(account.getEmail()))) {
            throw new DuplicateException(MessageUtil.getMessage(ACCOUNT_EMAIL_EXISTS));
        }
        if (Boolean.TRUE.equals(accountRepository.existsAccountByUsername(account.getUsername()))) {
            throw new DuplicateException(MessageUtil.getMessage(ACCOUNT_USERNAME_EXISTS));
        }
//        if (!positionRepository.existsById(account.getPositionId())) {
//            throw new NotFoundException(MessageUtil.getMessage("position.id.not.exists"));
//        }
//        if (!departmentRepository.existsById(account.getDepartmentId())) {
//            throw new NotFoundException(MessageUtil.getMessage("department.id.not.exists"));
//        }
        Account acc = objectMapperUtils.map(account, Account.class);
//        acc.setDepartment(new Department(account.getDepartmentId()));
//        acc.setPosition(new Position(account.getPositionId()));
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
            throw new DuplicateException(MessageUtil.getMessage(ACCOUNT_EMAIL_EXISTS));
        }
//        if (!positionRepository.existsById(account.getPositionId())) {
//            throw new NotFoundException(MessageUtil.getMessage("position.id.not.exists"));
//        }
//        if (!departmentRepository.existsById(account.getDepartmentId())) {
//            throw new NotFoundException(MessageUtil.getMessage("department.id.not.exists"));
//        }
        objectMapperUtils.getModelMapper().map(account, acc);
//        acc.setPosition(new Position(account.getPositionId()));
//        acc.setDepartment(new Department(account.getDepartmentId()));
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
    public AccountDto auth(AuthenticationRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername()).orElse(null);
        if (account == null || !passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return null;
        }
        return objectMapperUtils.map(account, AccountDto.class);
    }

    @Override
    public AccountDto findByUsername(String token) {
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        if (!jwtService.isTokenValid(token)) {
            throw new BadCredentialsException("Invalid or expired token");
        }
        Account account = accountRepository.findByUsername(jwtService.extractUsername(token)).orElse(null);
        if (account == null) {
            throw new NotFoundException(MessageUtil.getMessage(ACCOUNT_USERNAME_NOT_EXISTS));
        }
        return objectMapperUtils.map(account, AccountDto.class);
    }

    @Override
    public List<AccountInfoDto> getAccountByDepartmentId(int departmentId) {
        List<Account> accounts = accountRepository.findAllByDepartmentId(departmentId);
        return objectMapperUtils.mapAll(accounts, AccountInfoDto.class);
    }
}
