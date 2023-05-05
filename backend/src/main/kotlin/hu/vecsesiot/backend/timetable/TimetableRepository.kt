package hu.vecsesiot.backend.timetable

import hu.vecsesiot.backend.line.Line
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TimetableRepository : JpaRepository<Timetable, Long> {
	fun findAllByLine(line: Line): List<Timetable>

	// TODO check if param is necessary
	@Query("SELECT t FROM Timetable t WHERE t.line = :line AND t.startDate > :date ORDER BY t.startDate ASC")
	fun findAllByLineAndAfterDate(@Param("line") line: Line, @Param("date") date: LocalDateTime): List<Timetable>
}