package BancoMaster.Bank.controller;

import BancoMaster.Bank.dto.signIn.LoginRequest;
import BancoMaster.Bank.dto.signIn.LoginResponse;
import BancoMaster.Bank.dto.signup.ConfirmationRequest;
import BancoMaster.Bank.dto.signup.UserRequest;
import BancoMaster.Bank.dto.signup.signupResponse;
import BancoMaster.Bank.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRequest userRequest) throws MessagingException {
        userService.register(userRequest);
        return ResponseEntity.ok("Confirmation Code has been sent.");
    }
    @PostMapping("/confirmation")
    public ResponseEntity<signupResponse> confirmCode(
            @RequestBody ConfirmationRequest request) {

        signupResponse response =
                userService.signUp(request.email(), request.code());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequestDTO) {
       LoginResponse response = userService.login(loginRequestDTO);
      return ResponseEntity.ok(response);
   }
}
