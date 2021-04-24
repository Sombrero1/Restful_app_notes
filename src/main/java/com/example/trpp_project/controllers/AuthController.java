package com.example.trpp_project.controllers;


import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthController {



//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
//        System.out.println("hello");
//        try {
//
//            String username = requestDto.getUsername();
//
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
//            User user = userService.findByUsername(username);
//
//
//            if (user == null) {
//                throw new UsernameNotFoundException("User with username: " + username + " not found");
//            }
//
//            String token = jwtTokenProvider.createToken(username, user.getRoles());
//
//            Map<Object, Object> response = new HashMap<>();
//            response.put("username", username);
//            response.put("token", token);
//
//            return ResponseEntity.ok(response);
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//    }
}
