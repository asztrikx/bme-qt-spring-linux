package hu.vecsesiot.backend

import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.line.LineRepository
import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.section.SectionRepository
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.stop.Stop
import hu.vecsesiot.backend.stop.StopRepository
import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.timetable.TimetableRepository
import hu.vecsesiot.backend.user.User
import hu.vecsesiot.backend.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class SqlInitializer : ApplicationRunner {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Autowired
    private lateinit var sectionRepository: SectionRepository

    @Autowired
    private lateinit var lineRepository: LineRepository

    @Autowired
    private lateinit var timetableRepository: TimetableRepository

    override fun run(args: ApplicationArguments) {
        val stops = getStops()

        val sections = getSections(stops)

        val lines = getLines(sections)

        val timetables = getTimetables(lines)

        val user = User(username = "asd")
        userRepository.save(user)

        stops.forEach { stopRepository.save(it) }
        sections.forEach { sectionRepository.save(it) }
        lines.forEach { lineRepository.save(it) }
        timetables.forEach { timetableRepository.save(it) }
    }

    private fun getStops(): List<Stop> {
        val stops = mutableListOf<Stop>()
        stops.add(
            Stop(
                id = 1,
                coordinate = GPSCoordinate(47.419804, 19.247528),
                name = "Autóbuszállomás"
            )
        )
        stops.add(
            Stop(
                id = 2,
                coordinate = GPSCoordinate(47.39992883987876, 19.25913066972342),
                name = "Erzsébet tér"
            )
        )
        stops.add(
            Stop(
                id = 3,
                coordinate = GPSCoordinate(47.40366317296768, 19.2533053122232),
                name = "Sportpálya"
            )
        )
        stops.add(
            Stop(
                id = 4,
                coordinate = GPSCoordinate(47.40128681145784, 19.249980961251165),
                name = "Széchenyi"
            )
        )
        stops.add(
            Stop(
                id = 5,
                coordinate = GPSCoordinate(47.4173987343762, 19.241270529370745),
                name = "Logisztikai központ"
            )
        )
        stops.add(
            Stop(
                id = 6,
                coordinate = GPSCoordinate(47.41502039841608, 19.26243241570361),
                name = "Vörösmarty utca"
            )
        )
        stops.add(
            Stop(
                id = 7,
                coordinate = GPSCoordinate(47.407785632881705, 19.281104668262078),
                name = "Iskola utca"
            )
        )
        stops.add(
            Stop(
                id = 8,
                coordinate = GPSCoordinate(47.399162080014555, 19.299996594292413),
                name = "Kőolajvezeték Vállalat"
            )
        )
        stops.add(
            Stop(
                id = 9,
                coordinate = GPSCoordinate(47.416556419436255, 19.25811216892971),
                name = "OTP lakótelep"
            )
        )

        return stops
    }

    private fun getSections(stops: List<Stop>): List<Section> {
        val sections = mutableListOf<Section>()
        sections.add(
            Section(id = 1, timespan = Duration.ofSeconds(120)).apply {
                start = stops[0]
                stop = stops[1]
            }
        )
        sections.add(
            Section(id = 2, timespan = Duration.ofSeconds(100)).apply {
                start = stops[1]
                stop = stops[2]
            }
        )
        sections.add(
            Section(id = 3, timespan = Duration.ofSeconds(90)).apply {
                start = stops[1]
                stop = stops[3]
            }
        )
        sections.add(
            Section(id = 4, timespan = Duration.ofSeconds(65)).apply {
                start = stops[2]
                stop = stops[4]
            }
        )
        sections.add(
            Section(id = 5, timespan = Duration.ofSeconds(34)).apply {
                start = stops[4]
                stop = stops[5]
            }
        )
        sections.add(
            Section(id = 6, timespan = Duration.ofSeconds(123)).apply {
                start = stops[3]
                stop = stops[7]
            }
        )
        sections.add(
            Section(id = 7, timespan = Duration.ofSeconds(145)).apply {
                start = stops[7]
                stop = stops[8]
            }
        )
        sections.add(
            Section(id = 8, timespan = Duration.ofSeconds(170)).apply {
                start = stops[6]
                stop = stops[4]
            }
        )
        sections.add(
            Section(id = 9, timespan = Duration.ofSeconds(53)).apply {
                start = stops[4]
                stop = stops[2]
            }
        )
        sections.add(
            Section(id = 10, timespan = Duration.ofSeconds(102)).apply {
                start = stops[2]
                stop = stops[0]
            }
        )
        sections.add(
            Section(id = 11, timespan = Duration.ofSeconds(200)).apply {
                start = stops[8]
                stop = stops[6]
            }
        )
        return sections
    }

    private fun getLines(sections: List<Section>): List<Line> {
        val lines = mutableListOf<Line>()
        lines.add(
            Line(id = 1, name = "1 - Hosszú járat").apply {
                route = listOf(sections[0], sections[1], sections[3], sections[4])
                stops = listOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
            }
        )
        lines.add(
            Line(id = 2, name = "2 - Körjárat").apply {
                route = listOf(sections[0], sections[2], sections[5], sections[6])
                stops = listOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
            }
        )
        lines.add(
            Line(id = 3, name = "3 - Visszirányú járat").apply {
                route = listOf(sections[10], sections[7], sections[8], sections[9])
                stops = listOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
            }
        )
        return lines
    }

    private fun getTimetables(lines: List<Line>): List<Timetable> {
        val timetables = mutableListOf<Timetable>()
        timetables.add(
            Timetable(id = 1, startDate = LocalDateTime.of(2023, 3, 30, 16, 42)).apply {
                line = lines[0]
            }
        )
        timetables.add(
            Timetable(id = 2, startDate = LocalDateTime.of(2023, 3, 30, 15, 10)).apply {
                line = lines[1]
            }
        )
        timetables.add(
            Timetable(id = 3, startDate = LocalDateTime.of(2023, 3, 30, 14, 42)).apply {
                line = lines[2]
            }
        )
        timetables.add(
            Timetable(id = 4, startDate = LocalDateTime.of(2023, 3, 30, 15, 42)).apply {
                line = lines[0]
            }
        )
        timetables.add(
            Timetable(id = 5, startDate = LocalDateTime.of(2023, 3, 30, 15, 33)).apply {
                line = lines[1]
            }
        )
        timetables.add(
            Timetable(id = 6, startDate = LocalDateTime.of(2023, 3, 30, 15, 57)).apply {
                line = lines[1]
            }
        )
        timetables.add(
            Timetable(id = 7, startDate = LocalDateTime.of(2023, 3, 30, 16, 5)).apply {
                line = lines[1]
            }
        )
        timetables.add(
            Timetable(id = 8, startDate = LocalDateTime.of(2023, 3, 30, 15, 42)).apply {
                line = lines[2]
            }
        )
        timetables.add(
            Timetable(id = 9, startDate = LocalDateTime.of(2023, 3, 30, 16, 42)).apply {
                line = lines[2]
            }
        )
        return timetables
    }
}