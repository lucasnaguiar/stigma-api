package br.dev.norn.stigma.user.service;

import br.dev.norn.stigma.user.dto.UserDetailDataObject;
import br.dev.norn.stigma.user.dto.UserRegisterDataObject;
import br.dev.norn.stigma.user.model.User;
import br.dev.norn.stigma.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Long STUDIO_ID = 1L;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndStudioId(email, STUDIO_ID)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    @Transactional(readOnly = true)
    public List<UserDetailDataObject> getUsers() {
        return userRepository.findByStudioId(STUDIO_ID)
                .stream()
                .map(UserDetailDataObject::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDetailDataObject getUserById(Long id) {
        User user = userRepository.findByIdAndStudioId(id, STUDIO_ID)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        return new UserDetailDataObject(user);
    }

    @Transactional
    public User store(UserRegisterDataObject userDTO) {
        if (userRepository.existsByEmailAndStudioId(userDTO.email(), STUDIO_ID)) {
            throw new RuntimeException("Já existe um usuário com o email: " + userDTO.email());
        }

        var userEntity = new User(userDTO);
        userEntity.setStudioId(STUDIO_ID);
        userEntity.setPassword(passwordEncoder.encode(userDTO.password()));
        userEntity.setIsActive(true);
        return userRepository.save(userEntity);
    }

    @Transactional
    public UserDetailDataObject update(Long id, UserRegisterDataObject userDTO) {
        User user = userRepository.findByIdAndStudioId(id, STUDIO_ID)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        if (userRepository.existsByEmailAndStudioIdAndIdNot(userDTO.email(), STUDIO_ID, id)) {
            throw new RuntimeException("Já existe outro usuário com o email: " + userDTO.email());
        }

        user.update(userDTO);
        User updatedUser = userRepository.save(user);
        return new UserDetailDataObject(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findByIdAndStudioId(id, STUDIO_ID)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        userRepository.delete(user);
    }
}
