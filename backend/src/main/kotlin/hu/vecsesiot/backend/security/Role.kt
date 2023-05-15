package hu.vecsesiot.backend.security

import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.annotation.web.AuthorizeHttpRequestsDsl
import org.springframework.security.web.access.intercept.RequestAuthorizationContext

enum class RoleType {
	User {
		override val iherits: List<RoleType>
			get() = listOf()
	},
	Developer {
		override val iherits: List<RoleType>
			get() = listOf(Maintenance, Driver, User)
	},
	Maintenance {
		override val iherits: List<RoleType>
			get() = listOf(User)
	},
	Driver {
		override val iherits: List<RoleType>
			get() = listOf(User)
	},
	Display {
		override val iherits: List<RoleType>
			get() = listOf(User)
	};

	abstract val iherits: List<RoleType>
	val roles: Collection<RoleType>
		get() = (listOf(this) + iherits + iherits.map { it.roles }.flatten()).toHashSet()
}

fun expandRoles(vararg highRoles: RoleType) = highRoles
	.map { it.roles }
	.flatten()
	.toHashSet()
	.map { it.name }
	.toMutableList()

fun AuthorizeHttpRequestsDsl.hasAnyAuthority(vararg authorities: RoleType): AuthorizationManager<RequestAuthorizationContext> {
	val authorityNames = Array(authorities.size) { authorities[it].name }
	return hasAnyAuthority(*authorityNames)
}
