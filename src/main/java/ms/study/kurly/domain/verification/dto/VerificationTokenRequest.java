package ms.study.kurly.domain.verification.dto;

import lombok.*;
import ms.study.kurly.domain.verification.VerificationToken;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class VerificationTokenRequest {

    private Long verificationId;

    private VerificationToken.Type type;
}
