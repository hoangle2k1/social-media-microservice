package com.gmail.controller.rest;

import com.gmail.dto.request.EndRegistrationRequest;
import com.gmail.dto.request.ProcessEmailRequest;
import com.gmail.dto.request.RegistrationRequest;
import com.gmail.dto.response.AuthenticationResponse;
import com.gmail.mapper.RegistrationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.gmail.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UI_V1_AUTH)
public class RegistrationController {

    private final RegistrationMapper registrationMapper;

    @PostMapping(REGISTRATION_CHECK)
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationRequest request, BindingResult bindingResult) {
        return ResponseEntity.ok(registrationMapper.registration(request, bindingResult));
    }

    @PostMapping(REGISTRATION_CODE)
    public ResponseEntity<String> sendRegistrationCode(@Valid @RequestBody ProcessEmailRequest request, BindingResult bindingResult) {
        return ResponseEntity.ok(registrationMapper.sendRegistrationCode(request.getEmail(), bindingResult));
    }

    @GetMapping(REGISTRATION_ACTIVATE_CODE)
    public ResponseEntity<String> checkRegistrationCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(registrationMapper.checkRegistrationCode(code));
    }

    @PostMapping(REGISTRATION_CONFIRM)
    public ResponseEntity<AuthenticationResponse> endRegistration(@Valid @RequestBody EndRegistrationRequest request, BindingResult bindingResult) {
        return ResponseEntity.ok(registrationMapper.endRegistration(request, bindingResult));
    }
}
