package hu.vecsesiot.backend.line

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LineService {
    @Autowired
    private lateinit var repository: LineRepository

    @Transactional
    fun addLine(line : Line) = repository.save(line)

    fun findLinesByName(name : String) = repository.findAllByName(name)

    fun findById(id : Long) = repository.findById(id)

    @Transactional
    fun deleteLineById(id : Long) = repository.deleteById(id)
}