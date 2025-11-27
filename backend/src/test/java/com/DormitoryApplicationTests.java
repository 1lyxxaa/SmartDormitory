package com;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = DormitoryApplication.class)
@ActiveProfiles("test")
class DormitoryApplicationTests {

    @Test
    void contextLoads() {
        // пустой тест, проверяем что контекст Spring Boot поднимается
    }
}

