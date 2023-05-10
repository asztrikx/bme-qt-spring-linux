package hu.vecsesiot.backend.section

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository
@RepositoryRestResource
interface SectionRepository : JpaRepository<Section, Long>
