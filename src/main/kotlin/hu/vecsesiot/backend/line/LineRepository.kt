package hu.vecsesiot.backend.line

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LineRepository : JpaRepository<Line, Long> {
	fun findByName(name: String): Line?
}