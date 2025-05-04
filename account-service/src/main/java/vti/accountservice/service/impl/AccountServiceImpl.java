package vti.accountservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vti.accountservice.client.DepartmentServiceClient;
import vti.accountservice.client.PositionServiceClient;
import vti.accountservice.model.Account;
import vti.accountservice.repository.AccountRepository;
import vti.accountservice.request.account.AccountCreateRequest;
import vti.accountservice.request.account.AccountUpdateRequest;
import vti.accountservice.response.dto.account.AccountInfoDto;
import vti.accountservice.response.dto.account.AccountListDto;
import vti.accountservice.response.dto.department.DepartmentInfoDto;
import vti.accountservice.response.dto.position.PositionInfoDto;
import vti.accountservice.service.AccountService;
import vti.common.enums.PositionName;
import vti.common.exception_handler.DuplicateException;
import vti.common.exception_handler.NotFoundException;
import vti.common.payload.PageResponse;
import vti.common.utils.ConstantUtils;
import vti.common.utils.FilterRole;
import vti.common.utils.MessageUtil;
import vti.common.utils.ObjectMapperUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();
    private final PasswordEncoder passwordEncoder;
    private final PositionServiceClient positionServiceClient;
    private final DepartmentServiceClient departmentServiceClient;
    private final KeycloakAdminService keycloakAdminService;

    @Value("${keycloak.realm}")
    private String realm;

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
        List<DepartmentInfoDto> departments = departmentServiceClient.getDepartmentsByIds(departmentIds);
        if (departments == null) {
            throw new NotFoundException("Not Found Department");
        }

        Map<Integer, String> departmentMap = departments.stream()
                .collect(Collectors.toMap(
                        DepartmentInfoDto::getDepartmentId,
                        DepartmentInfoDto::getDepartmentName
                ));

        List<PositionInfoDto> positions = positionServiceClient.getPositionsByIds(positionIds);
        if (positions == null) {
            throw new NotFoundException("Not Found Position");
        }

        Map<Integer, PositionName> positionMap = positions.stream()
                .collect(Collectors.toMap(
                        PositionInfoDto::getPositionId,
                        PositionInfoDto::getPositionName
                ));
        List<AccountListDto> accountListDtos = accounts.getContent().stream()
                .map(account -> {
                    String departmentName = departmentMap.get(account.getDepartmentId());
                    PositionName positionName = positionMap.get(account.getPositionId());
                    return AccountListDto.builder()
                            .accountId(account.getAccountId())
                            .email(account.getEmail())
                            .fullName(account.getFullName())
                            .createDate(account.getCreateDate())
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
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.ACCOUNT_ID_NOT_EXISTS));
        }
        PositionInfoDto positionInfoDto = positionServiceClient.getPositionById(account.getPositionId());
        DepartmentInfoDto departmentInfoDto = departmentServiceClient.getDepartmentById(account.getDepartmentId());

        AccountInfoDto accountDto = objectMapperUtils.map(account, AccountInfoDto.class);
        accountDto.setPositionName(positionInfoDto.getPositionName());
        accountDto.setDepartmentName(departmentInfoDto.getDepartmentName());

        return accountDto;
    }

    @Override
    public void save(AccountCreateRequest account) {
        JsonNode existingUser = keycloakAdminService.findUserByUsername(account.getUsername()).block();
        List<String> roles = getAllRoles();
        Optional<String> roleName = roles.stream().filter(role -> role.equals(account.getRole().toLowerCase())).findFirst();

        if (roleName.isEmpty()) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.ROLE_NOT_EXISTS));
        }

        if (existingUser != null) {
            throw new DuplicateException(MessageUtil.getMessage(ConstantUtils.ACCOUNT_USERNAME_EXISTS));
        }

        if (Boolean.TRUE.equals(accountRepository.existsAccountByEmail(account.getEmail()))) {
            throw new DuplicateException(MessageUtil.getMessage(ConstantUtils.ACCOUNT_EMAIL_EXISTS));
        }

        if (positionServiceClient.getPositionById(account.getPositionId()) == null) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.POSITION_ID_NOT_EXISTS));
        }
        if (departmentServiceClient.getDepartmentById(account.getDepartmentId()) == null) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.DEPARTMENT_ID_NOT_EXISTS));
        }
        Account acc = objectMapperUtils.map(account, Account.class);

        keycloakAdminService.createUserOnKeycloak(account.getUsername(), account.getPassword(), account.getRole().toLowerCase())
                .subscribe(keycloakUserId -> {
                    acc.setKeycloakUserId(keycloakUserId);
                    accountRepository.save(acc);
                });

    }

    @Override
    public void update(AccountUpdateRequest account) {
        Account acc = accountRepository.findById(account.getAccountId()).orElse(null);
        if (acc == null) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.ACCOUNT_ID_NOT_EXISTS));
        }
        if (Boolean.TRUE.equals(accountRepository.existsAccountByEmailAndAccountIdNot(account.getEmail(), account.getAccountId()))) {
            throw new DuplicateException(MessageUtil.getMessage(ConstantUtils.ACCOUNT_EMAIL_EXISTS));
        }
        if (positionServiceClient.getPositionById(account.getPositionId()) == null) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.POSITION_ID_NOT_EXISTS));
        }
        if (departmentServiceClient.getDepartmentById(account.getDepartmentId()) == null) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.DEPARTMENT_ID_NOT_EXISTS));
        }

        objectMapperUtils.getModelMapper().map(account, acc);
        accountRepository.save(acc);
    }

    @Override
    public void delete(Integer id) {
        Account acc = accountRepository.findById(id).orElse(null);
        if (acc == null) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.ACCOUNT_ID_NOT_EXISTS));
        }
        accountRepository.delete(acc);
    }

    @Override
    public List<AccountInfoDto> getAccountByDepartmentId(int departmentId) {
        List<Account> accounts = accountRepository.findAllByDepartmentId(departmentId);
        return objectMapperUtils.mapAll(accounts, AccountInfoDto.class);
    }

    @Override
    public List<String> getAllRoles() {
        List<String> roles = keycloakAdminService.getAllRealmRoles().block();
        if (roles == null || roles.isEmpty()) {
            throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.ROLES_EMPTY));
        }
        return FilterRole.getRootRole(roles);
    }
}
