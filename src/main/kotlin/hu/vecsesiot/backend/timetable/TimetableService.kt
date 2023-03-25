package hu.vecsesiot.backend.timetable

import hu.vecsesiot.backend.line.Line
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TimetableService {

    @Autowired
    private lateinit var repository: TimetableRepository

    @Transactional
    fun addTimetable(timetable: Timetable) = repository.save(timetable)

    fun findTimetablesByLine(line: Line) = repository.findAllByLine(line)

    fun findTimetableByLineAndAfterDate(line: Line, date: LocalDateTime) =
        repository.findAllByLineAndAfterDate(line, date)

    fun findById(id: Long) = repository.findById(id)

    @Transactional
    fun deleteLineById(id: Long) = repository.deleteById(id)
}