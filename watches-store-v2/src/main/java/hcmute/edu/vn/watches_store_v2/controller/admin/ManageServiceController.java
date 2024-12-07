package hcmute.edu.vn.watches_store_v2.controller.admin;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.service.request.ProceedServiceRequest;
import hcmute.edu.vn.watches_store_v2.dto.service.response.ServiceResponse;
import hcmute.edu.vn.watches_store_v2.entity.Service;
import hcmute.edu.vn.watches_store_v2.mapper.ServiceMapper;
import hcmute.edu.vn.watches_store_v2.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service")
public class ManageServiceController extends ControllerBase {

    private final ServiceRepository serviceRepository;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/process-service")
    public ResponseEntity<?> processService(@RequestParam ObjectId serviceId,
                                            @RequestBody ProceedServiceRequest proceedServiceRequest) {
        try {
            Service service = this.serviceRepository.findById(serviceId).orElse(null);

            if (service == null) {
                return response(null, HttpStatus.NOT_FOUND);
            }

            service.setState("proceed");



            this.serviceRepository.save(service);

            return response(service, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllServices(
            @RequestParam(defaultValue = "none") String q,
            @RequestParam(defaultValue = "none") String state) {
        try {
            List<ServiceResponse> services = serviceRepository.findAll()
                    .stream()
                    .map(ServiceMapper::mapServiceResponse)
                    .filter(s -> q.equals("none")
                            || s.getPhone().equals(q.trim())
                            || s.getName().toLowerCase(new Locale("vi", "VN")).contains(q.toLowerCase(new Locale("vi", "VN")))
                            || s.getState().equals(state))
                    .collect(Collectors.toList());

            return response(services, HttpStatus.OK);
        } catch (Exception e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
