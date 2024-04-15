package com.gmail.service;

import com.gmail.dto.request.AuthenticationRequest;
import com.gmail.model.User;
import com.gmail.repository.projection.AuthUserProjection;
import com.gmail.repository.projection.UserPrincipalProjection;
import org.springframework.validation.BindingResult;

import java.util.Map;

public interface AuthenticationService {

    Long getAuthenticatedUserId();

    User getAuthenticatedUser();

    UserPrincipalProjection getUserPrincipalByEmail(String email);

    Map<String, Object> login(AuthenticationRequest request, BindingResult bindingResult);

    Map<String, Object> getUserByToken();

    String getExistingEmail(String email, BindingResult bindingResult);

    String sendPasswordResetCode(String email, BindingResult bindingResult);

    AuthUserProjection getUserByPasswordResetCode(String code);

    String passwordReset(String email, String password, String password2, BindingResult bindingResult);

    String currentPasswordReset(String currentPassword, String password, String password2, BindingResult bindingResult);
}
