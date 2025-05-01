package vti.positionservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vti.common.enums.PositionName;
import vti.common.utils.PositionNameConverter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "Position")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Position implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PositionID")
    Integer positionId;

    @Column(nullable = false, unique = true, name = "PositionName")
    @Convert(converter = PositionNameConverter.class)
    PositionName positionName;

    public Position(Integer positionId) {
        this.positionId = positionId;
    }

}
