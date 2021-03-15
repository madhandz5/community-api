package ryan.community.module.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "TB_BOARD_REPLY")
@SequenceGenerator(name = "TB_BOARD_REPLY_SEQ", sequenceName = "TB_BOARD_REPLY_SEQ", allocationSize = 30, initialValue = 1)
public class BoardReply {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_BOARD_REPLY_SEQ")
    @Column(name = "reply_id")
    private Long id;

    private String contents;

    private LocalDateTime createAt;
    private boolean isActive;
    private LocalDateTime removedAt;

    private boolean isModified;
    private LocalDateTime modifiedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
