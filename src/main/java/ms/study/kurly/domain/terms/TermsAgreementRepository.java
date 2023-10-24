package ms.study.kurly.domain.terms;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsAgreementRepository extends JpaRepository<TermsAgreement, Long> {

    TermsAgreement findByTermsId(Long termsId);
}
