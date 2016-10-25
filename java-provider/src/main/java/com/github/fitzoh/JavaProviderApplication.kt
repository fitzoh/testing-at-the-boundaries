package com.github.fitzoh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class JavaProviderApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(JavaProviderApplication::class.java, *args)
    }

}

