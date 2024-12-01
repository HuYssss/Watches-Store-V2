package hcmute.edu.vn.watches_store_v2.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    private String name;
    private String phone;
    private String email;
    private String message;
    private String type;
    private List<String> img;
}
