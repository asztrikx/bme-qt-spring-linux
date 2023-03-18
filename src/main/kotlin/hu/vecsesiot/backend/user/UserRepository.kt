package hu.vecsesiot.backend.user
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
	@PersistenceContext
	private lateinit var em: EntityManager

	@Transactional
	fun save(user: User): User {
		return em.merge(user)
	}

	fun findAll(): List<User> {
		return em.createQuery("SELECT t FROM User t", User::class.java).resultList
	}

	fun findById(id: Long): User {
		return em.find(User::class.java, id)
	}

	@Transactional
	fun deleteById(id: Long) {
		val user = findById(id)
		em.remove(user)
	}
}
