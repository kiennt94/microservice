//package vti.accountmanagement.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.test.web.servlet.MockMvc;
//import vti.accountmanagement.request.authenticate.AuthenticationRequest;
//import vti.accountmanagement.response.authenticate.AuthenticationResponse;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class   AuthenticationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testAuthenticate() throws Exception {
//        AuthenticationRequest request = new AuthenticationRequest("testUser", "testPassword");
//        AuthenticationResponse response = new AuthenticationResponse("mocked-access-token", "Bearer", 3600L);
//
//        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);
//
//        mockMvc.perform(post("/api/auth")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value("mocked-access-token"))
//                .andExpect(jsonPath("$.tokenType").value("Bearer"))
//                .andExpect(jsonPath("$.expiresIn").value(3600));
//    }
//
//    @Test
//    void testAuthenticate_ShouldReturn401_WhenBadCredentials() throws Exception {
//        AuthenticationRequest request = new AuthenticationRequest("wrongUser", "wrongPassword");
//
//        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
//                .thenThrow(new BadCredentialsException("Invalid username or password."));
//
//        mockMvc.perform(post("/api/auth")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.message").value("Invalid username or password."));
//    }
//}
