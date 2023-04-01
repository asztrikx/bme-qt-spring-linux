package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BusRepository : JpaRepository<Bus, Long> {
	fun findBySerialnumber(serialnumber: String): Bus?
	fun findByUser(user: User): Bus?

	@Query("SELECT b FROM Bus b WHERE b.timetable.line = :line")
	fun findByLine(@Param("line") line: Line): List<Bus>
}
