package dsergeyev.example.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserRepository;
import dsergeyev.example.resources.authentication.LoginAttemptService;
import dsergeyev.example.resources.errorhanding.exception.UserBlockedExeption;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private LoginAttemptService loginAttemptService;

	@Autowired
	private HttpServletRequest request;

	private String getClientIP() {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}

		return xfHeader.split(",")[0];
	}

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		String ip = getClientIP();
		if (loginAttemptService.isBlocked(ip)) {
			throw new UserBlockedExeption("Access denied. You have exceeded the number of login attempts. Your IP address was bloked during next "
					+ LoginAttemptService.DURATION_HOURS + " hours");
		}

		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("User with the email '%s' doesn't exist", email));
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.isEnabled(), true, true, true, getAuthorities(user.getRole().getName()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));

		return authorities;
	}
}
