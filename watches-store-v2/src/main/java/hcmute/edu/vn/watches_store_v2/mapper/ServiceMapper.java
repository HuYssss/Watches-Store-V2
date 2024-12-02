package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.service.request.ServiceRequest;
import hcmute.edu.vn.watches_store_v2.dto.service.response.ServiceResponse;
import hcmute.edu.vn.watches_store_v2.entity.Service;
import org.bson.types.ObjectId;

public class ServiceMapper {
    public static ServiceResponse mapServiceResponse(Service service) {
        return new ServiceResponse(
                service.getId().toHexString(),
                service.getName(),
                service.getPhone(),
                service.getEmail(),
                service.getMessage(),
                service.getType(),
                service.getImg(),
                service.getState()
        );
    }

    public static Service mapService(ServiceRequest serviceRequest) {
        return new Service(
                new ObjectId(),
                serviceRequest.getName(),
                serviceRequest.getPhone(),
                serviceRequest.getEmail(),
                serviceRequest.getMessage(),
                serviceRequest.getType(),
                serviceRequest.getImg(),
                "active"
        );
    }
}
