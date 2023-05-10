package hu.vecsesiot.backend.line

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository
@RepositoryRestResource
interface LineRepository : JpaRepository<Line, Long> {
	fun findByName(name: String): Line?
}