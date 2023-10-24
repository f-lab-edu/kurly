package ms.study.kurly.domain.verification;

import lombok.RequiredArgsConstructor;
import ms.study.kurly.common.encryption.EncryptionUtils;
import ms.study.kurly.domain.verification.dto.VerificationTokenRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken generate(VerificationTokenRequest request) throws Exception {

        String token = EncryptionUtils.encrypt(request.getVerificationId());

        return VerificationToken.builder()
                .type(request.getType())
                .token(token)
                .build();

//        return verificationTokenRepository.save(verificationToken)
//                .toResponse();
    }
}
