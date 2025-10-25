package br.dev.norn.stigma.user.controller;

import br.dev.norn.stigma.user.dto.UserDetailDataObject;
import br.dev.norn.stigma.user.dto.UserRegisterDataObject;
import br.dev.norn.stigma.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDetailDataObject>> index() {
        var users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetailDataObject> create(@RequestBody UserRegisterDataObject userRegisterDTO, UriComponentsBuilder uriBuilder) {
        var user = userService.store(userRegisterDTO);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDetailDataObject(user));
    }
}
