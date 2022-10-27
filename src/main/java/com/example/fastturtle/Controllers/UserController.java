package com.example.fastturtle.Controllers;

import com.example.fastturtle.Dtos.CreateUserDto;
import com.example.fastturtle.Dtos.ReturnUserDto;
import com.example.fastturtle.Dtos.UpdateUserDto;
import com.example.fastturtle.Exceptions.EmailTakenException;
import com.example.fastturtle.Exceptions.UserNotFoundException;
import com.example.fastturtle.Models.User;
import com.example.fastturtle.Repositories.UserRepository;
import com.example.fastturtle.Services.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/user/")
public class UserController {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserController(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ReturnUserDto getUserById(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return mapper.toReturnUserDto(user);
    }

    @GetMapping("following/{id}")
    public List<ReturnUserDto> getUserFollowing(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return mapper.toReturnUserDto(user.getFollowing());
    }

    @PostMapping
    public ReturnUserDto createUser(@RequestBody CreateUserDto createUserDto){
        if(userRepository.existsUserByEmail(createUserDto.email())){
            throw new EmailTakenException(createUserDto.email());
        }

        User user = mapper.toUser(createUserDto);
        userRepository.save(user);

        return mapper.toReturnUserDto(user);
    }

    @PutMapping("{id}")
    public ReturnUserDto updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(userRepository.existsUserByEmail(updateUserDto.email())){
            throw new EmailTakenException(updateUserDto.email());
        }

        user.setEmail(updateUserDto.email());
        user.setPassword(updateUserDto.password());
        userRepository.save(user);

        return mapper.toReturnUserDto(user);
    }

    @PutMapping("{id}/follow/{followId}")
    public void followUser(@PathVariable Long id, @PathVariable Long followId){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(!userRepository.existsById(followId)){
            throw new UserNotFoundException(followId);
        }

        user.follow(new User(followId));
        userRepository.save(user);
    }

    @PutMapping("{id}/unfollow/{followId}")
    public void unfollowUser(@PathVariable Long id, @PathVariable Long followId){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(!userRepository.existsById(followId)){
            throw new UserNotFoundException(followId);
        }

        user.unfollow(new User(followId));
        userRepository.save(user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.deleteById(id);
    }
}
