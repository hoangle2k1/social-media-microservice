package com.gmail.mapper;

import com.gmail.UserServiceTestHelper;
import com.gmail.dto.request.AuthenticationRequest;
import com.gmail.dto.request.CurrentPasswordResetRequest;
import com.gmail.dto.request.PasswordResetRequest;
import com.gmail.repository.projection.AuthUserProjection;
import com.gmail.service.AuthenticationService;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.BindingResult;

import java.util.Map;

import static org.mockito.Mockito.*;

public class AuthenticationMapperTest extends AbstractAuthTest {

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @MockBean
    private AuthenticationService authenticationService;

    BindingResult bindingResult = mock(BindingResult.class);

    @Test
    public void login() {
        AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        Map<String, Object> credentials = Map.of(
                "user", authUserProjection,
                "token", TestConstants.AUTH_TOKEN);
        when(authenticationService.login(request, bindingResult)).thenReturn(credentials);
        authenticationMapper.login(request, bindingResult);
        verify(authenticationService, times(1)).login(request, bindingResult);
    }

    @Test
    public void getUserByToken() {
        AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();
        Map<String, Object> credentials = Map.of(
                "user", authUserProjection,
                "token", TestConstants.AUTH_TOKEN);
        when(authenticationService.getUserByToken()).thenReturn(credentials);
        authenticationMapper.getUserByToken();
        verify(authenticationService, times(1)).getUserByToken();
    }

    @Test
    public void getExistingEmail() {
        when(authenticationService.getExistingEmail(TestConstants.USER_EMAIL, bindingResult))
                .thenReturn("Reset password code is send to your E-mail");
        authenticationMapper.getExistingEmail(TestConstants.USER_EMAIL, bindingResult);
        verify(authenticationService, times(1)).getExistingEmail(TestConstants.USER_EMAIL, bindingResult);
    }

    @Test
    public void sendPasswordResetCode() {
        when(authenticationService.sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult))
                .thenReturn("Reset password code is send to your E-mail");
        authenticationMapper.sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult);
        verify(authenticationService, times(1)).sendPasswordResetCode(TestConstants.USER_EMAIL, bindingResult);
    }

    @Test
    public void getUserByPasswordResetCode() {
        AuthUserProjection authUserProjection = UserServiceTestHelper.createAuthUserProjection();
        when(authenticationService.getUserByPasswordResetCode(TestConstants.ACTIVATION_CODE)).thenReturn(authUserProjection);
        authenticationMapper.getUserByPasswordResetCode(TestConstants.ACTIVATION_CODE);
        verify(authenticationService, times(1)).getUserByPasswordResetCode(TestConstants.ACTIVATION_CODE);
    }

    @Test
    public void passwordReset() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail(TestConstants.USER_EMAIL);
        request.setPassword(TestConstants.PASSWORD);
        request.setPassword2(TestConstants.PASSWORD);
        when(authenticationService.passwordReset(
                TestConstants.USER_EMAIL,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        )).thenReturn("Password successfully changed!");
        authenticationMapper.passwordReset(request, bindingResult);
        verify(authenticationService, times(1)).passwordReset(
                TestConstants.USER_EMAIL,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        );
    }

    @Test
    public void currentPasswordReset() {
        CurrentPasswordResetRequest request = new CurrentPasswordResetRequest();
        request.setCurrentPassword(TestConstants.PASSWORD);
        request.setPassword(TestConstants.PASSWORD);
        request.setPassword2(TestConstants.PASSWORD);
        when(authenticationService.currentPasswordReset(
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        )).thenReturn("Your password has been successfully updated.");
        authenticationMapper.currentPasswordReset(request, bindingResult);
        verify(authenticationService, times(1)).currentPasswordReset(
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                TestConstants.PASSWORD,
                bindingResult
        );
    }
}
