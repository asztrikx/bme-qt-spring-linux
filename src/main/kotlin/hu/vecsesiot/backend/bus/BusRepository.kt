package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource


@RepositoryRestResource(collectionResourceRel = "buses", path = "buses")
interface BusRepository : JpaRepository<Bus, Long> {
	// TODO empty is permitted
	fun findBySerialnumber(serialnumber: String): Bus?
	fun findByUser(user: User): Bus?

	@Query("SELECT b FROM Bus b WHERE b.timetable.line = :line")
	fun findByLine(@Param("line") line: Line): List<Bus>
}
