package ms.study.kurly.domain.terms;

import lombok.RequiredArgsConstructor;
import ms.study.kurly.common.Response;
import ms.study.kurly.domain.terms.dto.TermsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TermsController {

    private final TermsService service;

    @GetMapping("/terms")
    public Response<List<TermsResponse>> terms() {

        return service.terms();
    }
}
