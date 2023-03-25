package hu.vecsesiot.backend.stop

import jakarta.transaction.Transactional
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StopService {
    @Autowired
    private lateinit var repository: StopRepository

    @Transactional
    fun addStop(stop : Stop) = repository.save(stop)

    fun findStopsByName(name : String) = repository.findAllByName(name)

    fun findById(id : Long) = repository.findById(id)

 /*   @Transactional
    fun updateStop(stop : Stop)
    {
        var existing = repository.read(stop.getId())
        BeanUtils.copyProperties(stop, existing, getNullPropertyNames(stop))
        repository.save(existing)
    }*/

    @Transactional
    fun deleteStopById(id : Long) = repository.deleteById(id)
}