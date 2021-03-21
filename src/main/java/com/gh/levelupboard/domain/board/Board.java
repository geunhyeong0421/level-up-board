package com.gh.levelupboard.domain.board;

import com.gh.levelupboard.domain.BaseTimeEntity;
import com.gh.levelupboard.domain.user.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role createPermission; // 작성 권한


    public Board(String name) {
        this(name, null);
    }

    public Board(String name, Role createPermission) {
        this.name = name;
        this.createPermission = createPermission;
    }

}
