package hcmute.edu.vn.watches_store_v2.dto.user.response;

import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.dto.user.Address;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String id;

    private String email;

    private String phone;

    private String username;

    private String fullName;

    private String avatarImg;

    private Address address;

    private String reasonBlock;

    private Date blockAt;

    private String state;

    private boolean verified;

    private boolean isAdmin;

    private List<OrderResponse> orders = new ArrayList<>();

    public void setOrder(OrderResponse order) {
        orders.add(order);
    }
}
