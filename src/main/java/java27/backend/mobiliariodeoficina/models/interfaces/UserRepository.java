package java27.backend.mobiliariodeoficina.models.interfaces;

import java.util.Optional;

import java27.backend.mobiliariodeoficina.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
