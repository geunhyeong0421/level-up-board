package com.gh.levelupboard.domain.board;

import com.gh.levelupboard.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

    @Column(nullable = false)
    private String name;

    public Board(String name) {
        this.name = name;
    }


    // 게시판 이름 변경
    public void changeName(String name) {
        this.name = name;
    }

}
