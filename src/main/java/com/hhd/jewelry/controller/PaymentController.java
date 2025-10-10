package com.hhd.jewelry.controller;

import com.hhd.jewelry.config.MomoConfig;
import com.hhd.jewelry.entity.Order;
import com.hhd.jewelry.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class PaymentController {
    private final OrderRepository orderRepository;
    private final MomoConfig momoConfig;
    @GetMapping("/vnpay_return")
    public String vnpayReturn(HttpServletRequest request, Model model) {
        String status = request.getParameter("vnp_ResponseCode");
        String orderIdStr = request.getParameter("vnp_TxnRef");
        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            try {
                Integer orderId = Integer.parseInt(orderIdStr);
                Order order = orderRepository.findById(orderId).orElse(null);

                if (order != null) {
                    if ("00".equals(status)) {
                        order.setStatus(Order.Status.PAID);
                        orderRepository.save(order);
                        model.addAttribute("status", "success");
                        model.addAttribute("message", "Thanh toán thành công!");
                    } else {
                        order.setStatus(Order.Status.CANCELLED);
                        orderRepository.save(order);
                        model.addAttribute("status", "fail");
                        model.addAttribute("message", "Thanh toán thất bại. Vui lòng thử lại.");
                    }
                } else {
                    model.addAttribute("status", "fail");
                    model.addAttribute("message", "Không tìm thấy đơn hàng của bạn.");
                }
            } catch (NumberFormatException e) {
                model.addAttribute("status", "fail");
                model.addAttribute("message", "Mã đơn hàng không hợp lệ.");
            }
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "Giao dịch không hợp lệ.");
        }

        // Trả về một view để hiển thị thông báo
        return "client/product/payment_result";
    }

    @GetMapping("/momo_return")
    public String momoReturn(
            HttpServletRequest request,
            Model model
    ) {
        // Lấy các tham số cần thiết từ request theo đúng tên MoMo trả về
        String partnerCode = request.getParameter("partnerCode");
        String orderId = request.getParameter("orderId");
        String requestId = request.getParameter("requestId");
        String amount = request.getParameter("amount");
        String orderInfo = request.getParameter("orderInfo");
        String orderType = request.getParameter("orderType");
        String transId = request.getParameter("transId");
        String resultCode = request.getParameter("resultCode");
        String message = request.getParameter("message");
        String payType = request.getParameter("payType");
        String responseTime = request.getParameter("responseTime");
        String extraData = request.getParameter("extraData");
        String momoSignature = request.getParameter("signature");
        String accessKey = momoConfig.getAccessKey(); // Lấy accessKey từ config

        // Tạo chuỗi raw data để xác thực theo đúng thứ tự alphabet MoMo yêu cầu
        String rawData = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&message=" + message +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&orderType=" + orderType +
                "&partnerCode=" + partnerCode +
                "&payType=" + payType +
                "&requestId=" + requestId +
                "&responseTime=" + responseTime +
                "&resultCode=" + resultCode +
                "&transId=" + transId;

        // Hash chuỗi raw data bằng secretKey
        String calculatedSignature = MomoConfig.hmacSHA256(momoConfig.getSecretKey(), rawData);

        // 1. So sánh chữ ký
        if (!calculatedSignature.equals(momoSignature)) {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "Lỗi: Chữ ký không hợp lệ.");
            return "client/product/payment_result";
        }

        // 2. Kiểm tra kết quả giao dịch
        try {
            String orderIdDB = orderId.split("-")[0];
            Order order = orderRepository.findById(Integer.parseInt(orderIdDB)).orElse(null);

            if (order == null) {
                model.addAttribute("status", "fail");
                model.addAttribute("message", "Lỗi: Không tìm thấy đơn hàng của bạn.");
                return "client/product/payment_result";
            }

            if ("0".equals(resultCode)) {
                order.setStatus(Order.Status.PAID);
                order.setMethodPay("MOMO");
                orderRepository.save(order);
                model.addAttribute("status", "success");
                model.addAttribute("message", "Thanh toán bằng MoMo thành công cho đơn hàng #" + orderId);
            } else {
                order.setStatus(Order.Status.CANCELLED);
                order.setMethodPay("MOMO");
                orderRepository.save(order);
                model.addAttribute("status", "fail");
                model.addAttribute("message", "Thanh toán thất bại. " + message);
            }
        } catch (NumberFormatException e) {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "Lỗi: Mã đơn hàng không hợp lệ.");
        }

        return "client/product/payment_result";
    }
}