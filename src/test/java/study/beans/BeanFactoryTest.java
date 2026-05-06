package study.beans;

import com.diy.app.LectureController;
import com.diy.app.LectureRepository;
import com.diy.app.LectureRepositoryImpl;
import com.diy.framework.web.beans.factory.BeanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BeanFactoryTest {

    @Test
    @DisplayName("BeanFactory가 @Autowired 생성자에 의존성을 주입한다")
    void createBeanWithDependency() {
        BeanFactory beanFactory = new BeanFactory("com.diy.app");

        Object controller = beanFactory.getBean(LectureController.class);
        System.out.println("LectureController: " + controller);

        Object repository = beanFactory.getBean(LectureRepository.class);
        System.out.println("LectureRepository: " + repository);

        System.out.println("controller 타입: " + controller.getClass().getSimpleName());
        System.out.println("repository 타입: " + repository.getClass().getSimpleName());
    }
}
