package ms.study.kurly.domain.terms;

import jakarta.persistence.*;
import lombok.*;
import ms.study.kurly.domain.verification.VerificationToken;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter
@ToString
@Builder
@DynamicUpdate
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TermsAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    @Setter(AccessLevel.PUBLIC)
    private Boolean agreed;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    private Terms terms;

    @Setter(AccessLevel.PUBLIC)
    @OneToOne(cascade = CascadeType.ALL)
    private VerificationToken verificationToken;
}