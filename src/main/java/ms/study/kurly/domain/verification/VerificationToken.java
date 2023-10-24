package ms.study.kurly.domain.verification;

import jakarta.persistence.*;
import lombok.*;
import ms.study.kurly.domain.verification.dto.VerificationTokenResponse;
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
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, columnDefinition = "ENUM('UNKNOWN', 'MOBILE', 'TERMS') DEFAULT 'UNKNOWN'")
    private Type type = Type.UNKNOWN;

    @Column(nullable = false)
    private String token;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum Type {
        MOBILE,
        TERMS,
        UNKNOWN
    }

    public VerificationTokenResponse toResponse() {
        return new VerificationTokenResponse(id);
    }
}
