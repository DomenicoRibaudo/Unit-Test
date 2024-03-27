package com.develhope.UnitTest.controllers;

import com.develhope.UnitTest.entities.User;
import com.develhope.UnitTest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<User> createStudent(@RequestBody User userToAdd){
        return ResponseEntity.ok().body(userService.addStudent(userToAdd));
    }
    @GetMapping("/getall")
    public ResponseEntity<List<User>>get(){
        return ResponseEntity.ok(userService.getAll());
    }
    @GetMapping("/getsingle/{id}")
    public ResponseEntity<User> getSingleStudent(@PathVariable Long id){
        Optional<User> studentOptional = userService.getOne(id);
        if(studentOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(studentOptional.get());
    }
    @PutMapping("/putuser/{id}")
    public ResponseEntity<User> updateEvento(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = userService.updateUser(id, user);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userOptional.get());
    }
    @DeleteMapping("/deletesingle/{id}")
    public ResponseEntity<User> deleteSingleStudent(@PathVariable Long id){
        Optional<User> studentOptional = userService.deleteById(id);
        if(studentOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(studentOptional.get());
    }
    @PatchMapping ("/active/{id}")
    public ResponseEntity<User> studentActivation(@PathVariable Long id, @RequestParam Boolean isActive) {
        Optional<User> updatedStudent = userService.userActivation(id, isActive);
        if (updatedStudent.isPresent()) {
            return ResponseEntity.ok(updatedStudent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
