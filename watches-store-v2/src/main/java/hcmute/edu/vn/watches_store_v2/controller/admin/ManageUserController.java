package hcmute.edu.vn.watches_store_v2.controller.admin;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileResponse;
import hcmute.edu.vn.watches_store_v2.service.component.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ManageUserController extends ControllerBase {

    private final UserService userService;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/get-all-user")
    public ResponseEntity<?> getAllUser(@RequestParam(defaultValue = "none") String q) {
        List<ProfileResponse> users = userService.getAllUser();

        if (users != null) {
            return response(users.stream()
                            .filter(u -> q.equals("none")
                                    || (u.getFullName() != null && u.getFullName().toLowerCase(new Locale("vi", "VN")).contains(q.toLowerCase(new Locale("vi", "VN"))))
                                    || (u.getPhone() != null && u.getPhone().contains(q))
                                    || u.getEmail().contains(q))
                    , HttpStatus.OK);
        }
        else
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/block-user")
    public ResponseEntity<?> blockUser(@RequestParam ObjectId userId,
                                       @RequestParam (defaultValue = "Vi phạm chính sách") String message) {
        try {
            return response(
                    this.userService.blockUser(userId, message),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/unblock-user")
    public ResponseEntity<?> unblockUser(@RequestParam ObjectId userId) {
        try {
            return response(
                    this.userService.unblockUser(userId),
                    HttpStatus.OK
            );
        } catch (MongoException e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
