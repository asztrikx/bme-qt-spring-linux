package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.stop.Stop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StopRepository : JpaRepository<Stop, Long> {
    fun findAllByName(name : String) : List<Stop>
}