package vti.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vti.accountservice.request.account.AccountCreateRequest;
import vti.accountservice.request.account.AccountUpdateRequest;
import vti.accountservice.response.dto.account.AccountInfoDto;
import vti.accountservice.response.dto.account.AccountListDto;
import vti.accountservice.service.AccountService;
import vti.common.enums.PositionName;
import vti.common.exception_handler.NotFoundException;
import vti.common.payload.PageResponse;
import vti.common.utils.ConstantUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/account - Success")
    void getAll_success() throws Exception {
        List<AccountListDto> accounts = List.of(new AccountListDto());
        PageResponse<AccountListDto> response = new PageResponse<>(accounts, 1, 1, 0, 10);

        when(accountService.getAll(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/account")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("GET /api/account/{id} - Success")
    void getById_success() throws Exception {
        AccountInfoDto account = new AccountInfoDto(
                1,
                "username",
                "user@example.com",
                "Full Name",
                LocalDateTime.now(),
                1,
                "IT",
                2,
                PositionName.DEV
        );

        when(accountService.getAccountById(1)).thenReturn(account);

        mockMvc.perform(get("/api/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @DisplayName("GET /api/account/{id} - Not Found")
    void getById_notFound() throws Exception {
        when(accountService.getAccountById(999)).thenThrow(new NotFoundException("Account not found"));

        mockMvc.perform(get("/api/account/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Account not found"));
    }

    @Test
    @DisplayName("POST /api/account/create - Success")
    void create_success() throws Exception {
        AccountCreateRequest req = new AccountCreateRequest();
        req.setEmail("email@test.com");
        req.setUsername("username");
        req.setFullName("Full Name");
        req.setPassword("pass");
        req.setRole("USER");
        req.setPassword("pass1234");
        req.setDepartmentId(1);
        req.setPositionId(1);

        String json = objectMapper.writeValueAsString(req);

        doNothing().when(accountService).save(any());

        mockMvc.perform(post("/api/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(ConstantUtils.CREATE_SUCCESSFULLY));
    }

    @Test
    @DisplayName("POST /api/account/update - Success")
    void update_success() throws Exception {
        AccountUpdateRequest req = new AccountUpdateRequest(1, "email@test.com", "Full Name", 1, 1);

        String json = objectMapper.writeValueAsString(req);

        doNothing().when(accountService).update(any());

        mockMvc.perform(post("/api/account/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(ConstantUtils.UPDATE_SUCCESSFULLY));
    }

    @Test
    @DisplayName("DELETE /api/account/{id} - Success")
    void delete_success() throws Exception {
        doNothing().when(accountService).delete(1);

        mockMvc.perform(delete("/api/account/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(ConstantUtils.DELETE_SUCCESSFULLY));
    }

    @Test
    @DisplayName("GET /api/account/roles - Success")
    void getRoles_success() throws Exception {
        when(accountService.getAllRoles()).thenReturn(List.of("ADMIN", "USER"));

        mockMvc.perform(get("/api/account/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("ADMIN"))
                .andExpect(jsonPath("$[1]").value("USER"));
    }

    @Test
    @DisplayName("GET /api/account/department - Success")
    void getAccountsByDepartment_success() throws Exception {
        AccountInfoDto account = new AccountInfoDto(
                1,
                "username",
                "user@example.com",
                "Full Name",
                LocalDateTime.now(),
                1,
                "IT",
                2,
                PositionName.DEV
        );
        List<AccountInfoDto> list = List.of(account);
        when(accountService.getAccountByDepartmentId(1)).thenReturn(list);

        mockMvc.perform(get("/api/account/department")
                        .param("departmentId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"));
    }
}
