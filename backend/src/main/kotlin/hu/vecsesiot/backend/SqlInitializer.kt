package hu.vecsesiot.backend

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.bus.BusRepository
import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.faultticket.FaultTicketRepository
import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.line.LineRepository
import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.section.SectionRepository
import hu.vecsesiot.backend.security.RoleType.*
import hu.vecsesiot.backend.security.expandRoles
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
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
@Profile("!test")
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

	@Autowired
	private lateinit var busRepository: BusRepository

	@Autowired
	private lateinit var faultTicketRepository: FaultTicketRepository

	override fun run(args: ApplicationArguments) {
		val stops = getStops()
		val sections = getSections(stops)
		val lines = getLines(sections)
		val timetables = getTimetables(lines)
		val users = getUsers()
		val buses = getBuses(users, timetables)
		val tickets = getFaultTickets(buses)

		stops.forEach { stopRepository.save(it) }
		sections.forEach { sectionRepository.save(it) }
		lines.forEach { lineRepository.save(it) }
		timetables.forEach { timetableRepository.save(it) }
		users.forEach { userRepository.save(it) }
		buses.forEach { busRepository.save(it) }
		tickets.forEach { faultTicketRepository.save(it) }
	}

	private fun getStops(): List<Stop> {
		return mutableListOf<Stop>().apply {
			add(
				Stop(
					coordinate = GPSCoordinate(47.419804, 19.247528),
					name = "Autóbuszállomás"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.39992883987876, 19.25913066972342),
					name = "Erzsébet tér"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.40366317296768, 19.2533053122232),
					name = "Sportpálya"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.40081377546881, 19.2486407587372),
					name = "Széchenyi"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.4173987343762, 19.241270529370745),
					name = "Logisztika központ"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.41502039841608, 19.26243241570361),
					name = "Vörösmarty utca"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.407785632881705, 19.281104668262078),
					name = "Iskola utca"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.399162080014555, 19.299996594292413),
					name = "Kőolajvezeték vállalat"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.416556419436255, 19.25811216892971),
					name = "OTP lakótelep"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.4116, 19.2707),
					name = "Anna utca"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.4167, 19.2518),
					name = "Market Central"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.40597489716716, 19.257094448364764),
					name = "Kinizsi utca"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.40497231300258, 19.242531431424034),
					name = "Besztercei utca"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.40899928349606, 19.247786069530157),
					name = "Halmy József tér"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.413716243960444, 19.24605706617703),
					name = "Előd utca"
				)
			)
		}
	}

	private fun getSections(stops: List<Stop>): List<Section> {
		return mutableListOf(
			// 580 (Market Central)
			stops.createSection("Iskola utca", "Anna utca", mutableListOf()),
			stops.createSection("Anna utca", "Vörösmarty utca", mutableListOf()),
			stops.createSection("Vörösmarty utca", "OTP lakótelep", mutableListOf()),
			stops.createSection("OTP lakótelep", "Market Central", mutableListOf(
				GPSCoordinate(47.4172, 19.2541),
				GPSCoordinate(47.4162, 19.2534),
			)),
			// 581 (Kőolajvezeték vállalat)
			stops.createSection("OTP lakótelep", "Vörösmarty utca", mutableListOf()),
			stops.createSection("Vörösmarty utca", "Anna utca", mutableListOf()),
			stops.createSection("Anna utca", "Iskola utca", mutableListOf()),
			stops.createSection("Iskola utca", "Kőolajvezeték vállalat", mutableListOf()),
			// 576 (Logisztika központ)
			stops.createSection("Erzsébet tér", "Kinizsi utca", mutableListOf(
				GPSCoordinate(47.39984172096639, 19.259285804647785),
				GPSCoordinate(47.39998842262298, 19.259545571588877),
				GPSCoordinate(47.40024259525668, 19.259652859942417),
				GPSCoordinate(47.400502213180935, 19.259451694279527),
				GPSCoordinate(47.40051492172357, 19.25941950774823),
				GPSCoordinate(47.400571202350015, 19.258974261081036),
			)),
			stops.createSection("Kinizsi utca", "Sportpálya", mutableListOf(
				GPSCoordinate(47.40597489716716, 19.257094448364764),
				GPSCoordinate(47.40627623891681, 19.256901329310228),
			)),
			stops.createSection("Sportpálya", "Széchenyi", mutableListOf(
				GPSCoordinate(47.40047060530009, 19.249417568510694),
				GPSCoordinate(47.400550487528875, 19.249227131683153),
				GPSCoordinate(47.40053959450482, 19.249082292405873),
			)),
			stops.createSection("Széchenyi", "Besztercei utca", mutableListOf(
				GPSCoordinate(47.40081377546881, 19.2486407587372),
				GPSCoordinate(47.404632063246375, 19.24209254670556),
			)),
			stops.createSection("Besztercei utca", "Halmy József tér", mutableListOf(
				GPSCoordinate(47.40497231300258, 19.242531431424034),
			)),
			stops.createSection("Halmy József tér", "Előd utca", mutableListOf(
				GPSCoordinate(47.40899928349606, 19.247786069530157),
				GPSCoordinate(47.41106877712169, 19.250433059398958),
			)),
			stops.createSection("Előd utca", "Logisztika központ", mutableListOf(
				GPSCoordinate(47.413716243960444, 19.24605706617703),
				GPSCoordinate(47.41397543735944, 19.245610201741727),
				GPSCoordinate(47.414360806484794, 19.245041723114454),
				GPSCoordinate(47.4156069565216, 19.242989828691236),
			)),
		)
	}

	private fun getLines(sections: List<Section>): List<Line> {
		return mutableListOf(
			Line(name = "580 (Market Central)").apply {
				route = mutableListOf(sections[0], sections[1], sections[2], sections[3])
				stops = mutableListOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
			},
			Line(name = "581 (Kőolajvezeték vállalat)").apply {
				route = mutableListOf(sections[4], sections[5], sections[6], sections[7])
				stops = mutableListOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
			},
			Line(name = "576 (Logisztika központ)").apply {
				route = mutableListOf(sections[8], sections[9], sections[10], sections[11], sections[12], sections[13], sections[14])
				stops = mutableListOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
			},
		)
	}

	private fun getTimetables(lines: List<Line>): List<Timetable> {
		return mutableListOf(
			Timetable(startDate = LocalDateTime.now().plusMinutes(1)).apply {
				line = lines[0]
			},
			Timetable(startDate = LocalDateTime.now().plusMinutes(-10)).apply {
				line = lines[0]
			},
			Timetable(startDate = LocalDateTime.now().plusMinutes(3)).apply {
				line = lines[0]
			},

			Timetable(startDate = LocalDateTime.now().plusMinutes(3)).apply {
				line = lines[1]
			},

			Timetable(startDate = LocalDateTime.now().plusMinutes(3)).apply {
				line = lines[2]
			},
		)
	}

	private fun getUsers(): List<User> {
		return mutableListOf<User>().apply {
			add(
				User(
					name = "Nagy István",
					username = "user",
					roles = expandRoles(User),
					// ne spammeljünk domaineket...
					email = "d",
					// 123
					password = "\$2a\$12\$.oQRfzitpL1ACFSTjjas6e3fGNRbI52rOCH4frgE8mwxOuh3RV7o2"
				)
			)
			add(
				User(
					name = "Gipsz Jakab",
					username = "driver",
					roles = expandRoles(Driver),
					// ne spammeljünk domaineket...
					email = "c",
					// 123
					password = "\$2a\$12\$.oQRfzitpL1ACFSTjjas6e3fGNRbI52rOCH4frgE8mwxOuh3RV7o2"
				)
			)
			add(
				User(
					name = "Csetneki Péter",
					username = "driver2",
					roles = expandRoles(Driver),
					// ne spammeljünk domaineket...
					email = "b",
					// 123
					password = "\$2a\$12\$.oQRfzitpL1ACFSTjjas6e3fGNRbI52rOCH4frgE8mwxOuh3RV7o2"
				)
			)
			add(
				User(
					name = "Kis István",
					username = "driver3",
					roles = expandRoles(Driver),
					// ne spammeljünk domaineket...
					email = "a",
					// 123
					password = "\$2a\$12\$.oQRfzitpL1ACFSTjjas6e3fGNRbI52rOCH4frgE8mwxOuh3RV7o2"
				)
			)
			add(
				User(
					name = "Mekk Elek",
					username = "main",
					roles = expandRoles(Maintenance),
					// ne spammeljünk domaineket...
					email = "e",
					// 123
					password = "\$2a\$12\$.oQRfzitpL1ACFSTjjas6e3fGNRbI52rOCH4frgE8mwxOuh3RV7o2"
				)
			)
			add(
				User(
					name = "Developer",
					username = "dev",
					roles = expandRoles(Developer),
					// ne spammeljünk domaineket...
					email = "e",
					// 123
					password = "\$2a\$12\$.oQRfzitpL1ACFSTjjas6e3fGNRbI52rOCH4frgE8mwxOuh3RV7o2"
				)
			)
			add(
				User(
					name = "BusDisplay",
					username = "disp",
					roles = expandRoles(Display),
					email = "f",
					// 123
					password = "\$2a\$12\$.oQRfzitpL1ACFSTjjas6e3fGNRbI52rOCH4frgE8mwxOuh3RV7o2"
				)
			)
		}
	}

	private fun getBuses(users: List<User>, timetables: List<Timetable>): List<Bus> {
		return mutableListOf<Bus>().apply {
			add(
				Bus(serialnumber = "AAA111").apply {
					user = users[3]
				}
			)
			add(
				Bus(serialnumber = "BBB222").apply {
					user = users[2]
				}
			)
			add(
				Bus(serialnumber = "CCC223").apply {
					user = users[1]
				}
			)
		}
	}

	private fun getFaultTickets(buses: List<Bus>): List<FaultTicket> {
		val now = LocalDateTime.now()
		return mutableListOf<FaultTicket>().apply {
			add(
				FaultTicket(
					startDate = now.minusDays(1),
					description = "The engine is fucked up.",
					coordinate = GPSCoordinate(47.416674, 19.254866),
					state = FaultTicket.State.Created
				).apply {
					bus = buses[0]
					user = bus.user!!
				}
			)
			add(
				FaultTicket(
					startDate = now.minusHours(5),
					description = "The engine is fucked up and first door is locked.",
					coordinate = GPSCoordinate(47.416674, 19.254866),
					state = FaultTicket.State.InProgress
				).apply {
					bus = buses[0]
					user = bus.user!!

				}
			)
			add(
				FaultTicket(
					startDate = now.minusHours(1),
					description = "The engine is broken.",
					coordinate = GPSCoordinate(47.409671, 19.270102),
					resolveDate = now.minusSeconds(1),
					state = FaultTicket.State.Resolved
				).apply {
					bus = buses[0]
					user = bus.user!!

				}
			)
			add(
				FaultTicket(
					startDate = now.minusMinutes(2),
					description = "The doors are locked.",
					coordinate = GPSCoordinate(47.41458957260982, 19.25814141217378),
					resolveDate = now.plusMinutes(5),
					state = FaultTicket.State.Resolved
				).apply {
					bus = buses[0]
					user = bus.user!!
				}
			)
		}
	}

	operator fun List<Stop>.get(name: String) = this.first { it.name == name }

	fun List<Stop>.createSection(startStopName: String, endStopName: String, coos: MutableList<GPSCoordinate>) =
		Section(timespan = Duration.ofSeconds(200)).apply {
			start = this@createSection[startStopName]
			stop = this@createSection[endStopName]
			sectionPoints = (
				mutableListOf(this@createSection[startStopName].coordinate) + coos + mutableListOf(this@createSection[endStopName].coordinate)
				).toMutableList()
		}
}