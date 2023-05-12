@file:OptIn(ExperimentalStdlibApi::class)

package hu.vecsesiot.backend.bus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusService {
	@Autowired
	private lateinit var busRepository: BusRepository
}