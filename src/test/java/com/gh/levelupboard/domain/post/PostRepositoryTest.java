package com.gh.levelupboard.domain.post;

import com.gh.levelupboard.config.QuerydslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.*;

@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private PostRepository postRepository;


    @Test
    public void postsPagination() {
        //given

        //when

        //then
    }


}
