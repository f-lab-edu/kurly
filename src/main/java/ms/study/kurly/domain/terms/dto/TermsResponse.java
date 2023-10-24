package ms.study.kurly.domain.terms.dto;

import lombok.*;

@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class TermsResponse {

    private Long id;

    private String title;

    private String content;

    private Boolean required;
}
