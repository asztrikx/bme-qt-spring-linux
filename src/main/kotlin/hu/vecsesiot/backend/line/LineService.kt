package hu.vecsesiot.backend.line

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LineService {
    @Autowired
    private lateinit var repository: LineRepository

    fun findLinesByName(name: String) = repository.findAllByName(name)

}