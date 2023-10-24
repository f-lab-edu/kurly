package ms.study.kurly.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.study.kurly.common.Error;
import ms.study.kurly.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class KurlyExceptionHandler {

    private final ObjectMapper objectMapper;

    // TODO: KulryException 아니고 Exception으로 변경 (파라미터 오류 등 공통적인 부분을 처리할 수 있도록)
    @ExceptionHandler(KurlyException.class)
    public ResponseEntity<Response<Void>> handleUserException(KurlyException e) throws JsonProcessingException {

        String data = objectMapper.writeValueAsString(e.getData());
        // TODO: 로그 레벨 설정 고민
        log.error("code: {}, message: {}, data: {}", e.getError().getCode(), e.getError().getDetailMessage(), data, e);

        // TODO: enum으로 스테이터스 설정해서 활용하도록
        HttpStatus status = switch (e.getError()) {
            case EXCEEDED_VERIFICATION_CODE_REQUEST_LIMIT -> HttpStatus.TOO_MANY_REQUESTS;
            case MOBILE_VERIFICATION_CODE_NOT_MATCH, EXPIRED_MOBILE_VERIFICATION_CODE, PASSWORD_NOT_MATCH -> HttpStatus.BAD_REQUEST;
            case EMAIL_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(status).body(Response.<Void>builder()
                .code(e.getError().getCode())
                .message(e.getError().getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bi = e.getBindingResult();
        Error error = getErrorCode(bi);

        log.error("code: {}, message: {}", error.getCode(), error.getDetailMessage(), e);

        return Response.<Void>builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build();
    }

    private static Error getErrorCode(BindingResult bi) {

        String code = bi.getFieldError() != null ? bi.getFieldError().getCode() : null;
        Error error;

        if (code == null) return Error.UNKNOWN;

        error = switch (code) {
            case "Email" -> Error.INVALID_EMAIL;
            case "Password" -> Error.INVALID_PASSWORD;
            case "MobileNumber" -> Error.INVALID_MOBILE_NUMBER;
            default -> Error.UNKNOWN;
        };

        return error;
    }
}
