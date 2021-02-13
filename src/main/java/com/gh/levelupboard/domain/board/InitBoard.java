package com.gh.levelupboard.domain.board;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitBoard {

    private final InitBoardService initBoardService;

    @PostConstruct
    public void init() {
        initBoardService.init();
    }

    @Component
    static class InitBoardService {

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            em.persist(new Board("자유게시판"));
            em.persist(new Board("공지사항"));
        }

    }

}
