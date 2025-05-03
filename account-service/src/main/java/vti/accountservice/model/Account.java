package vti.accountservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    Integer accountId;

    @Column(nullable = false, unique = true, name = "PositionId")
    Integer positionId;

    @Column(nullable = false, unique = true, name = "DepartmentId")
    Integer departmentId;

    @Column(nullable = false, unique = true, name = "email")
    String email;

    @Column(nullable = false, unique = true, name = "keycloakUserId")
    String keycloakUserId;

    @Column(name = "FullName")
    String fullName;

    @CreatedDate
    @Column(name = "CreateDate")
    LocalDateTime createDate;

}
