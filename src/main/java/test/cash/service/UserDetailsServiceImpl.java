package test.cash.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.cash.model.User;
import test.cash.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public User getLoggedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (User) authentication.getPrincipal();
        return repository.findByUsername(userDetails.getUsername());
    }
}