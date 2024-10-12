package hcmute.edu.vn.watches_store_v2.controller;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.RequestAuthPassword;
import hcmute.edu.vn.watches_store_v2.dto.auth.response.ResponseCode;
import hcmute.edu.vn.watches_store_v2.dto.user.Address;
import hcmute.edu.vn.watches_store_v2.dto.user.request.ProfileRequest;
import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.service.auth.AuthService;
import hcmute.edu.vn.watches_store_v2.service.business.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/profile")
@RequiredArgsConstructor
public class ProfileController extends ControllerBase {
    
    private final ProfileService profileService;
    private final AuthService authService;

    @GetMapping()
    public ResponseEntity<?> getProfile(Principal principal) {
        try {
            return response(this.profileService.getProfileUser(findIdByUsername(principal.getName()))
                    , HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest profileRequest, Principal principal) {
        if (ObjectUtils.isEmpty(profileRequest))
            return response(null, HttpStatus.NO_CONTENT);

        try {
            return response(this.profileService.updateProfile(profileRequest, findIdByUsername(principal.getName()))
                    , HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(Principal principal, @RequestBody RequestAuthPassword requestAuthPassword) {
        User user = this.authService.checkPassword(findIdByUsername(principal.getName()), requestAuthPassword.getPassword());

        if (user != null)
            return response(new ResponseCode(user.getToken()), HttpStatus.OK);
        else
            return response(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update-address")
    public ResponseEntity<?> updateAddress(@RequestBody Address address, Principal principal) {
        if (ObjectUtils.isEmpty(address))
            return response(null, HttpStatus.NO_CONTENT);

        User user = this.profileService.updateAddress(address, findIdByUsername(principal.getName()));

        if (user == null)
            return response(null, HttpStatus.NOT_FOUND);
        else
            return response(user, HttpStatus.OK);
    }

//    @PostMapping("/update-password")
//    public ResponseEntity<?> updatePassword(Principal principal, @RequestBody RequestAuthPassword requestAuthPassword) {
//        User user= this.authService.updatePassword(findIdByUsername(principal.getName()), requestAuthPassword.getPassword());
//
//        if (user != null)
//            return response(null, HttpStatus.OK);
//        else
//            return response(null, HttpStatus.NOT_FOUND);
//    }
}
