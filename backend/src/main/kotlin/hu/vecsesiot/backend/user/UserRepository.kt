package hu.vecsesiot.backend.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
	fun findUserByUsername(username: String): User?
}