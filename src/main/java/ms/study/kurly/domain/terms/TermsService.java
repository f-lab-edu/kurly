package ms.study.kurly.domain.terms;

import lombok.RequiredArgsConstructor;
import ms.study.kurly.common.Response;
import ms.study.kurly.domain.terms.dto.TermsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    public Response<List<TermsResponse>> terms() {

         List<TermsResponse> list = termsRepository.findAll().stream()
                 .map(Terms::toResponse)
                 .toList();

        return Response.<List<TermsResponse>>builder()
                .data(list)
                .build();
    }
}
