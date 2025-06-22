package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleUserService implements UserService {
    UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> save(User user) {
        Optional<User> result;
        try {
            result = userRepository.save(user);
        } catch (Sql2oException e) {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public boolean deleteById(int id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }
}
