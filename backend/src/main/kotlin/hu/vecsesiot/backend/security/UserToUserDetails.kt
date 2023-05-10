package hu.vecsesiot.backend.security

import hu.vecsesiot.backend.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserToUserDetails(val user: User) : UserDetails {
	// GrantedAuthority is refined to read, write access
	override fun getAuthorities(): Collection<GrantedAuthority> {
		// sends it to ctor
		return user.roles.map(::SimpleGrantedAuthority)
	}

	override fun getPassword() = user.password

	override fun getUsername() = user.username

	override fun isAccountNonExpired() = true

	override fun isAccountNonLocked() = true

	override fun isCredentialsNonExpired() = true

	override fun isEnabled() = true

	val id = user.id!!
}