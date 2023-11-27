package com.spring.issue.integration

import com.spring.issue.application.service.MyService
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@SpringBootTest
class ServiceIntegrationTest {

    @Autowired
    private lateinit var service: MyService

    @Test
    fun test() {
        service
    }
}
