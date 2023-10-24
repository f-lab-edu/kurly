package ms.study.kurly.domain.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ms.study.kurly.common.Error;
import ms.study.kurly.domain.user.dto.LoginRequest;
import ms.study.kurly.domain.user.dto.SignupRequest;
import ms.study.kurly.common.exception.KurlyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            Error error = Error.PASSWORD_NOT_MATCH;
            Map<Object, Object> data = Map.of("request", request);

            throw new KurlyException(error, data);
        }

        // TODO: 서비스에도 있으니 중복 제거
        userService.isExistEmail(request.getEmail());
        userService.signup(request);
    }

    @GetMapping("/email-exists")
    public void isExistEmail(@RequestParam String email) {

         userService.isExistEmail(email);
    }

    // TODO: 리턴 타입 void로 변경하고 http status code 활용
    @PostMapping("/login")
    public Boolean login(@Valid @RequestBody LoginRequest dto) {

        return userService.login(dto);
    }
}
