package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.entity.Order;

public interface MailService {
    String sendResetToken(String email, String token, String username);
    String blockUser(String email, String username, String message);
    String deleteUser(String email, String username);
    String unBlockUser(String email, String username);
    String welcome(String email, String username, String token);
    String resetTokenSuccess(String email, String username);
    String verifiedUser(String email, String username);

    String orderSuccess(Order order);
}
