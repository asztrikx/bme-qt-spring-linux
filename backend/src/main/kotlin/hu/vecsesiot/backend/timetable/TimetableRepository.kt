package hu.vecsesiot.backend.timetable

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
@RepositoryRestResource
interface TimetableRepository : JpaRepository<Timetable, Long> {
	fun findAllByBusIsNullAndStartDateIsAfterOrderByStartDateAsc(date: LocalDateTime) : List<Timetable>

	fun findTimetableByBusIsNotNullAndBusUserId(user_id: Long) : Timetable?
	fun findTimetablesByLineId(line_id: Long) : List<Timetable>
	// TODO check if param is necessary
	@Query("SELECT t FROM Timetable t WHERE t.line.id = :line_id AND t.startDate > :date ORDER BY t.startDate ASC")
	fun findAllByLineAndAfterDate(@Param("line_id") line_id: Long, @Param("date") date: LocalDateTime): List<Timetable>
}