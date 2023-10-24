package ms.study.kurly.domain.terms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class TermsAgreementRequest {

    @NotBlank
    private Agreement[] agreements;

    @NotBlank
    private String mobileVerificationToken;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter(AccessLevel.PRIVATE)
    public static class Agreement {

        private Long id;
        private Boolean agreed;
    }
}
