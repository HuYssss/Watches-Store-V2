package hcmute.edu.vn.watches_store_v2.controller;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashBoardController extends ControllerBase {

    @GetMapping("/welcome-message")
    public ResponseEntity<?> getFirstWelcomeMessage(Authentication authentication){
        return response("Welcome to the Watches Store, username: " + authentication.getName() + " with scope:" + authentication.getAuthorities(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/manager-message")
    public ResponseEntity<?> getManagerData(Principal principal){
        return response("Manager::" + principal.getName(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/admin-message")
    public ResponseEntity<?> getAdminData(@RequestParam("message") String message, Principal principal){
        return response("Admin::"+principal.getName() + " has this message:" + message, HttpStatus.OK);

    }

    @PostMapping("/user")
    public ResponseEntity<?> getUserData(@RequestParam("message") String message, Principal principal){
        return response("User::" + principal.getName() + " has this message:" + message, HttpStatus.OK);
    }
}
