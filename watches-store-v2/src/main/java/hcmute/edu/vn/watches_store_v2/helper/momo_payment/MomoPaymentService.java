package hcmute.edu.vn.watches_store_v2.helper.momo_payment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class MomoPaymentService {
    public static String createPayment(Order order) {

        long totalPrice = (long) order.getTotalPrice();

        try {
            // Configurations
            String accessKey = "F8BBA842ECF85";
            String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
            String orderInfo = order.getId().toHexString();
            String partnerCode = "MOMO";
            String redirectUrl = "http://localhost:8080/payment-success";
            String ipnUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
            String requestType = "payWithMethod";
            String amount = String.valueOf(totalPrice);
            String orderId = partnerCode + System.currentTimeMillis();
            String requestId = orderId;
            String extraData = "";
            String orderGroupId = "";
            boolean autoCapture = true;
            String lang = "vi";

            // Raw signature string
            String rawSignature = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + redirectUrl +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            // Generate HMAC SHA256 signature
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] hmacBytes = hmacSHA256.doFinal(rawSignature.getBytes(StandardCharsets.UTF_8));
            StringBuilder signature = new StringBuilder();
            for (byte b : hmacBytes) {
                signature.append(String.format("%02x", b));
            }

            // Prepare JSON body
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("partnerCode", partnerCode);
            requestBody.addProperty("partnerName", "Test");
            requestBody.addProperty("storeId", "MomoTestStore");
            requestBody.addProperty("requestId", requestId);
            requestBody.addProperty("amount", amount);
            requestBody.addProperty("orderId", orderId);
            requestBody.addProperty("orderInfo", orderInfo);
            requestBody.addProperty("redirectUrl", redirectUrl);
            requestBody.addProperty("ipnUrl", ipnUrl);
            requestBody.addProperty("lang", lang);
            requestBody.addProperty("requestType", requestType);
            requestBody.addProperty("autoCapture", autoCapture);
            requestBody.addProperty("extraData", extraData);
            requestBody.addProperty("orderGroupId", orderGroupId);
            requestBody.addProperty("signature", signature.toString());

            // Send HTTP POST request
            URL url = new URL("https://test-payment.momo.vn/v2/gateway/api/create");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonBody = requestBody.toString();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read response
            int status = connection.getResponseCode();
            if (status == 200 || status == 201) {
                StringBuilder response = new StringBuilder();
                try (java.util.Scanner scanner = new java.util.Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                }

                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                return jsonResponse.get("payUrl").getAsString();

            } else {
                StringBuilder errorResponse = new StringBuilder();
                try (java.util.Scanner scanner = new java.util.Scanner(connection.getErrorStream(), StandardCharsets.UTF_8)) {
                    while (scanner.hasNextLine()) {
                        errorResponse.append(scanner.nextLine());
                    }
                }
                return "Error response: " + errorResponse.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
