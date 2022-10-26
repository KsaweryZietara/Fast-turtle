package com.example.fastturtle.Controllers;

import com.example.fastturtle.Dtos.CreateUserDto;
import com.example.fastturtle.Dtos.UpdateUserDto;
import com.example.fastturtle.Exceptions.EmailTakenException;
import com.example.fastturtle.Exceptions.UserNotFoundException;
import com.example.fastturtle.Models.User;
import com.example.fastturtle.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/user/")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    Iterable<User> getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    User getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping
    User createUser(@RequestBody CreateUserDto createUserDto){
        if(userRepository.existsUserByEmail(createUserDto.email())){
            throw new EmailTakenException(createUserDto.email());
        }

        User user = new User(createUserDto.firstName(),
                createUserDto.lastName(),
                createUserDto.email(),
                createUserDto.password());

        return userRepository.save(user);
    }

    @PutMapping("{id}")
    User updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(userRepository.existsUserByEmail(updateUserDto.email())){
            throw new EmailTakenException(updateUserDto.email());
        }

        user.setEmail(updateUserDto.email());
        user.setPassword(updateUserDto.password());

        return userRepository.save(user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.deleteById(id);
    }
}
