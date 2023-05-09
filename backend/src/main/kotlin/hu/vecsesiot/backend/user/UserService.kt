package hu.vecsesiot.backend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserService{
	@Autowired
	private lateinit var repository: UserRepository
}