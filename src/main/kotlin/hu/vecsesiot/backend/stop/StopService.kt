package hu.vecsesiot.backend.stop

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StopService {
    @Autowired
    private lateinit var repository: StopRepository

    fun findStopsByName(name: String) = repository.findAllByName(name)
}