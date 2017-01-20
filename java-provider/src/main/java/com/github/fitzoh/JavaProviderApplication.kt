package com.github.fitzoh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@SpringBootApplication
open class JavaProviderApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(JavaProviderApplication::class.java, *args)
    }


}


data class User(val id: Int, val username: String, val password: String, val nickname: String)

@Repository
class UserRepository : HashMap<String, User>();


data class Credentials(val username : String, val password : String)
@Controller
class LoginController(val userRepository: UserRepository) {
    @PostMapping("/login")
    fun login(@RequestBody credentials : Credentials): ResponseEntity<Map<String, Any>> {
        val user = userRepository[credentials.username]
        if (user != null && user.password == credentials.password){
            val body = mapOf("user" to user, "token" to UUID.randomUUID().toString())
            return ResponseEntity.ok(body)
        }
        return ResponseEntity.status(401).body(mapOf("error" to "invalid_credentials"))
    }
}
