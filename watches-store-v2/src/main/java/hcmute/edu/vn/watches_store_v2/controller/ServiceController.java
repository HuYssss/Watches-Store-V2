package hcmute.edu.vn.watches_store_v2.controller;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.entity.Service;
import hcmute.edu.vn.watches_store_v2.repository.ServiceRepository;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/service")
@RequiredArgsConstructor
public class ServiceController extends ControllerBase {

    private final ServiceRepository serviceRepository;

    @PostMapping("/create-service")
    public ResponseEntity<?> createService(@RequestBody Service service, Principal principal) {
        service.setId(new ObjectId());

        try {
            serviceRepository.save(service);
            return response(service, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllServices() {
        try {
            List<Service> services = serviceRepository.findAll();
            return response(services, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
