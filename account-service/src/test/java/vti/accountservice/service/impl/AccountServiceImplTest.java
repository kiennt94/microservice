package vti.accountservice.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
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
import vti.common.enums.PositionName;
import vti.common.payload.PageResponse;
import vti.common.utils.MessageUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private DepartmentServiceClient departmentServiceClient;

    @Mock
    private PositionServiceClient positionServiceClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private KeycloakAdminService keycloakAdminService;

    @Mock
    private MessageUtil messageUtil;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(
                accountRepository,
                passwordEncoder,
                positionServiceClient,
                departmentServiceClient,
                keycloakAdminService,
                messageUtil
        );
    }

    @Test
    @DisplayName("Get all accounts with paging and mapping department/position names")
    void testGetAllAccounts() {
        Account account = Account.builder()
                .accountId(1)
                .email("john.doe@example.com")
                .fullName("John Doe")
                .createDate(LocalDateTime.now())
                .departmentId(100)
                .positionId(200)
                .build();
        Page<Account> mockAccountPage = new PageImpl<>(List.of(account));
        Pageable pageable = PageRequest.of(0, 10);

        DepartmentInfoDto departmentDto = new DepartmentInfoDto(100, "IT");
        PositionInfoDto positionDto = new PositionInfoDto(200, PositionName.DEV);

        when(accountRepository.findAll(pageable, null)).thenReturn(mockAccountPage);
        when(departmentServiceClient.getDepartmentsByIds(List.of(100))).thenReturn(List.of(departmentDto));
        when(positionServiceClient.getPositionsByIds(List.of(200))).thenReturn(List.of(positionDto));

        PageResponse<AccountListDto> result = accountService.getAll(pageable, null);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        AccountListDto dto = result.getContent().get(0);
        assertThat(dto.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(dto.getDepartmentName()).isEqualTo("IT");
        assertThat(dto.getPositionName()).isEqualTo(PositionName.DEV);
    }

    @Test
    @DisplayName("Get account by ID - Success")
    void testGetAccountById() {
        Account account = Account.builder()
                .accountId(1)
                .email("john@example.com")
                .fullName("John Doe")
                .positionId(1)
                .departmentId(2)
                .build();

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(positionServiceClient.getPositionById(1)).thenReturn(new PositionInfoDto(1, PositionName.DEV));
        when(departmentServiceClient.getDepartmentById(2)).thenReturn(new DepartmentInfoDto(2, "IT"));

        AccountInfoDto result = accountService.getAccountById(1);

        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getPositionName()).isEqualTo(PositionName.DEV);
        assertThat(result.getDepartmentName()).isEqualTo("IT");
    }

    @Test
    @DisplayName("Save new account - Success")
    void testSaveAccount() {
        AccountCreateRequest request = AccountCreateRequest.builder()
                .email("test@example.com")
                .username("testuser")
                .password("123456")
                .role("admin")
                .positionId(1)
                .departmentId(2)
                .build();

        when(keycloakAdminService.findUserByUsername("testuser")).thenReturn(Mono.empty());
        when(keycloakAdminService.getAllRealmRoles()).thenReturn(Mono.just(List.of("admin", "user")));
        when(positionServiceClient.getPositionById(1)).thenReturn(new PositionInfoDto(1, PositionName.DEV));
        when(departmentServiceClient.getDepartmentById(2)).thenReturn(new DepartmentInfoDto(2, "IT"));
        when(keycloakAdminService.createUserOnKeycloak(any(), any(), any())).thenReturn(Mono.just("keycloak-id-123"));

        accountService.save(request);

        verify(accountRepository, timeout(1000)).save(any(Account.class));
    }

    @Test
    @DisplayName("Update account - Success")
    void testUpdateAccount() {
        AccountUpdateRequest request = AccountUpdateRequest.builder()
                .accountId(1)
                .email("updated@example.com")
                .fullName("Updated Name")
                .positionId(1)
                .departmentId(2)
                .build();

        Account existing = Account.builder()
                .accountId(1)
                .email("old@example.com")
                .fullName("Old Name")
                .positionId(1)
                .departmentId(2)
                .build();

        when(accountRepository.findById(1)).thenReturn(Optional.of(existing));
        when(accountRepository.existsAccountByEmailAndAccountIdNot("updated@example.com", 1)).thenReturn(false);
        when(positionServiceClient.getPositionById(1)).thenReturn(new PositionInfoDto(1, PositionName.DEV));
        when(departmentServiceClient.getDepartmentById(2)).thenReturn(new DepartmentInfoDto(2, "IT"));

        accountService.update(request);

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Delete account - Success")
    void testDeleteAccount() {
        Account existing = Account.builder()
                .accountId(1)
                .build();

        when(accountRepository.findById(1)).thenReturn(Optional.of(existing));

        accountService.delete(1);

        verify(accountRepository).delete(existing);
    }
}
