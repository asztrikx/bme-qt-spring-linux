package hu.vecsesiot.backend.timetable

import hu.vecsesiot.backend.line.Line
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/timetables")
class TimetableController {
    @Autowired
    private lateinit var service: TimetableService


    @GetMapping("/byLine")
    fun getTimetablesByLine(@RequestBody line: Line) = service.findTimetablesByLine(line)

    @GetMapping("/byLine/{:date}")
    fun getTimetablesByLineAfterDate(@RequestBody line: Line, @PathVariable date: LocalDateTime) =
        service.findTimetableByLineAndAfterDate(line, date)
    
}