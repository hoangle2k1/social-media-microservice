package com.gmail.mapper;

import com.gmail.dto.request.AuthenticationRequest;
import com.gmail.dto.request.CurrentPasswordResetRequest;
import com.gmail.dto.request.PasswordResetRequest;
import com.gmail.dto.request.*;
import com.gmail.dto.response.AuthUserResponse;
import com.gmail.dto.response.AuthenticationResponse;
import com.gmail.repository.projection.AuthUserProjection;
import com.gmail.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {

    private final ModelMapper modelMapper;
    private final AuthenticationService authenticationService;

    public AuthenticationResponse login(AuthenticationRequest request, BindingResult bindingResult) {
        return getAuthenticationResponse(authenticationService.login(request, bindingResult));
    }

    public AuthenticationResponse getUserByToken() {
        return getAuthenticationResponse(authenticationService.getUserByToken());
    }

    public String getExistingEmail(String email, BindingResult bindingResult) {
        return authenticationService.getExistingEmail(email, bindingResult);
    }

    public String sendPasswordResetCode(String email, BindingResult bindingResult) {
        return authenticationService.sendPasswordResetCode(email, bindingResult);
    }

    public AuthUserResponse getUserByPasswordResetCode(String code) {
        AuthUserProjection user = authenticationService.getUserByPasswordResetCode(code);
        return modelMapper.map(user, AuthUserResponse.class);
    }

    public String passwordReset(PasswordResetRequest request, BindingResult bindingResult) {
        return authenticationService.passwordReset(request.getEmail(), request.getPassword(), request.getPassword2(), bindingResult);
    }

    public String currentPasswordReset(CurrentPasswordResetRequest request, BindingResult bindingResult) {
        return authenticationService.currentPasswordReset(request.getCurrentPassword(), request.getPassword(),
                request.getPassword2(), bindingResult);
    }

    AuthenticationResponse getAuthenticationResponse(Map<String, Object> credentials) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setUser(modelMapper.map(credentials.get("user"), AuthUserResponse.class));
        response.setToken((String) credentials.get("token"));
        return response;
    }
}
