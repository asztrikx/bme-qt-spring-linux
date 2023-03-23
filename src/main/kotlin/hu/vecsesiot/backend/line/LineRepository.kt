package hu.vecsesiot.backend.line

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LineRepository : JpaRepository<Line, Long> {
    fun findAllByName(name : String) : Line
}