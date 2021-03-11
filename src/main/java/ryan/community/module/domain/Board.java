package ryan.community.module.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "TB_BOARD")
@SequenceGenerator(name = "TB_BOARD_SEQ", sequenceName = "TB_BOARD_SEQ", allocationSize = 30, initialValue = 1001)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_BOARD_SEQ")
    @Column(name = "board_id")
    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    private boolean isModified;
    private LocalDateTime modifiedAt;
}
