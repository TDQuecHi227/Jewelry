package com.hhd.jewelry.controller;

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
}