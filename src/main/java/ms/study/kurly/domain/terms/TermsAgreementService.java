package ms.study.kurly.domain.terms;

import lombok.RequiredArgsConstructor;
import ms.study.kurly.common.Error;
import ms.study.kurly.common.Response;
import ms.study.kurly.common.encryption.EncryptionUtils;
import ms.study.kurly.common.exception.KurlyException;
import ms.study.kurly.domain.terms.dto.TermsAgreementRequest;
import ms.study.kurly.domain.terms.dto.TermsAgreementResponse;
import ms.study.kurly.domain.verification.MobileVerification;
import ms.study.kurly.domain.verification.MobileVerificationRepository;
import ms.study.kurly.domain.verification.VerificationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService {

    private final TermsRepository termsRepository;
    private final TermsAgreementRepository termsAgreementRepository;
    private final MobileVerificationRepository mobileVerificationRepository;

    public Response<TermsAgreementResponse> agreement(TermsAgreementRequest request) throws Exception {

        Long mobileVerificationId = EncryptionUtils.decrypt(request.getMobileVerificationToken());
        MobileVerification verification = mobileVerificationRepository.findById(mobileVerificationId)
                .orElseThrow(() -> {
                    Error error = Error.MOBILE_VERIFICATION_TOKEN_NOT_FOUND;
                    Map<Object, Object> data = Map.of("request", request);

                    return new KurlyException(error, data);
                });

        if (!verification.getVerificationToken().getToken().equals(request.getMobileVerificationToken())) {
            Error error = Error.MOBILE_VERIFICATION_CODE_NOT_MATCH;
            Map<Object, Object> data = Map.of("request", request);

            throw new KurlyException(error, data);
        }

        List<Terms> termsList = termsRepository.findAll();

        termsList.forEach(terms -> {
            TermsAgreementRequest.Agreement requestAgreement = Arrays.stream(request.getAgreements())
                    .filter(a -> a.getId().equals(terms.getId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        Error error = Error.TERMS_NOT_FOUND;
                        Map<Object, Object> data = Map.of("request", request);

                        return new KurlyException(error, data);
                    });

            if (terms.getRequired() && !requestAgreement.getAgreed()) {
                Error error = Error.REQUIRED_TERMS_NOT_AGREED;
                Map<Object, Object> data = Map.of("request", request);

                throw new KurlyException(error, data);
            }

            TermsAgreement agreement = termsAgreementRepository.save(TermsAgreement.builder()
                    .agreed(requestAgreement.getAgreed())
                    .terms(terms)
                    .build());

            try {
                String token = EncryptionUtils.encrypt(agreement.getId());
                agreement.setVerificationToken(VerificationToken.builder()
                        .type(VerificationToken.Type.TERMS)
                        .token(token)
                        .build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        TermsAgreementRequest.Agreement agreement =  Arrays.stream(request.getAgreements())
                .max(Comparator.comparing(TermsAgreementRequest.Agreement::getId))
                .orElse(null);

        String token = null;
        if (agreement != null) {
             token = termsAgreementRepository.findByTermsId(agreement.getId())
                     .getVerificationToken()
                     .getToken();
        }

        return Response.<TermsAgreementResponse>builder()
                .data(new TermsAgreementResponse(token))
                .build();
    }
}
