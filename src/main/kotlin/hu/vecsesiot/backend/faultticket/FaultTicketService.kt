package hu.vecsesiot.backend.faultticket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FaultTicketService {
	@Autowired
	private lateinit var repository: FaultTicketRepository
}
