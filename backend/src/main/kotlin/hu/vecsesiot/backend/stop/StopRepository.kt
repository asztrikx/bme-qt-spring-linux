package hu.vecsesiot.backend.stop

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository
@RepositoryRestResource
interface StopRepository : JpaRepository<Stop, Long> {
	fun findAllByName(name: String): List<Stop>
}