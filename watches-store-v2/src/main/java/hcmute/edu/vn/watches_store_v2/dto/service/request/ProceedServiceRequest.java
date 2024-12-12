package hcmute.edu.vn.watches_store_v2.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProceedServiceRequest {
    private String note;
    private String type;
}
