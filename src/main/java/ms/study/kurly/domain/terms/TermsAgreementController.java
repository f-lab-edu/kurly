package ms.study.kurly.domain.terms;

import lombok.RequiredArgsConstructor;
import ms.study.kurly.common.Response;
import ms.study.kurly.domain.terms.dto.TermsAgreementRequest;
import ms.study.kurly.domain.terms.dto.TermsAgreementResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TermsAgreementController {

    private final TermsAgreementService service;

    @PostMapping("/terms/agreement")
    public Response<TermsAgreementResponse> agreement(@RequestBody TermsAgreementRequest request) throws Exception {

        return service.agreement(request);
    }
}
