package com.diy.framework.web.beans.factory;

import com.diy.app.LectureRepository;
import com.diy.framework.web.context.ApplicationContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextTest {

    @Test
    @DisplayName("ApplicationContext에서 빈 조회")
    void getBean() throws Exception {
        ApplicationContext context = new ApplicationContext("com.diy.app");

        LectureRepository repo = context.getBean(LectureRepository.class);

        assertThat(repo).isNotNull();
        assertThat(repo).isInstanceOf(LectureRepository.class);
    }
}
