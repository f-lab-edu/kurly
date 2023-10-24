package ms.study.kurly.domain.verification;

import lombok.RequiredArgsConstructor;
import ms.study.kurly.common.Error;
import ms.study.kurly.common.Response;
import ms.study.kurly.common.exception.KurlyException;
import ms.study.kurly.domain.verification.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MobileVerificationService {

    private final MobileVerificationRepository mobileVerificationRepository;
    private final VerificationTokenService verificationTokenService;

    public void sendVerificationCode(MobileCodeRequest dto) {

        List<MobileVerification> verifications = mobileVerificationRepository
                .findTop3ByMobileNumberOrderByCreatedAtDesc(dto.getMobileNumber());

        if (verifications.size() == 3) {
            MobileVerification verification = verifications.get(2);
            if (verification.getCreatedAt().plusMinutes(1).isAfter(LocalDateTime.now())) {
                Error error = Error.EXCEEDED_VERIFICATION_CODE_REQUEST_LIMIT;
                Map<Object, Object> data = Map.of("request", dto);

                throw new KurlyException(error, data);
            }
        }

        String verificationCode = String.valueOf(100000 + (int)(Math.random() * 900000));

        MobileVerification mobileVerification = MobileVerification.builder()
                .mobileNumber(dto.getMobileNumber())
                .verificationCode(verificationCode)
                .build();

        mobileVerificationRepository.save(mobileVerification);
    }

    public Response<MobileCodeVerifyResponse> verifyMobileVerificationCode(MobileCodeVerifyRequest dto) throws Exception {

        MobileVerification verification = mobileVerificationRepository
                .findFirstByMobileNumberOrderByCreatedAtDesc(dto.getMobileNumber())
                .orElseThrow(() -> {
                    Error error = Error.MOBILE_VERIFICATION_CODE_NOT_MATCH;
                    Map<Object, Object> data = Map.of("request", dto);

                    return new KurlyException(error, data);
                });

        if (!verification.getVerificationCode().equals(dto.getVerificationCode())) {
            Error error = Error.MOBILE_VERIFICATION_CODE_NOT_MATCH;
            Map<Object, Object> data = Map.of("request", dto);

            throw new KurlyException(error, data);
        }

        if (verification.getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            Error error = Error.EXPIRED_MOBILE_VERIFICATION_CODE;
            Map<Object, Object> data = Map.of("request", dto);

            throw new KurlyException(error, data);
        }

        VerificationToken token = verificationTokenService.generate(VerificationTokenRequest.builder()
                .verificationId(verification.getId())
                .type(VerificationToken.Type.MOBILE)
                .build());

        verification.setVerificationToken(token);

        return Response.<MobileCodeVerifyResponse>builder()
                .data(new MobileCodeVerifyResponse(token.getToken()))
                .build();
    }
}
