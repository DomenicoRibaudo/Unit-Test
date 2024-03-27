package com.develhope.UnitTest.services;

import com.develhope.UnitTest.entities.User;
import com.develhope.UnitTest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User addStudent(User user){
        return userRepository.saveAndFlush(user);
    }
    public List<User> getAll(){
        return userRepository.findAll();
    }
    public Optional<User> getOne(Long id){
        return userRepository.findById(id);
    }
    public Optional<User>updateUser(Long id,User user){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            userOptional.get().setName(user.getName());
            userOptional.get().setLastName(user.getLastName());
            User userUpdated = userRepository.save(userOptional.get());
            return Optional.of(userUpdated);
        }
        return Optional.empty();
    }
    public Optional<User>deleteById(Long id){
        Optional<User> studentOptional= userRepository.findById(id);
        if(studentOptional.isPresent()){
            userRepository.delete(studentOptional.get());
        }else{
            return Optional.empty();
        }
        return studentOptional;
    }
    public Optional<User> userActivation(Long id, Boolean isActive){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            userOptional.get().setActive(isActive);
            User studentUpdated = userRepository.save(userOptional.get());
            return Optional.of(studentUpdated);
        }else{
            return Optional.empty();
        }

    }
}
