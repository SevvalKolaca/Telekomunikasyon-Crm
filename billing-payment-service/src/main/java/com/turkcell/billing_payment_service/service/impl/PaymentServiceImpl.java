package com.turkcell.billing_payment_service.service.impl;

import com.turkcell.billing_payment_service.dto.PaymentRequestDTO;
import com.turkcell.billing_payment_service.dto.PaymentResponseDTO;
import com.turkcell.billing_payment_service.entity.Bill;
//import com.turkcell.billing_payment_service.entity.Customer;
import com.turkcell.billing_payment_service.entity.Payment;
import com.turkcell.billing_payment_service.repository.PaymentRepository;
import com.turkcell.billing_payment_service.service.BillService;
import com.turkcell.billing_payment_service.service.CustomerService;
import com.turkcell.billing_payment_service.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BillService billService;
    private final CustomerService customerService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, 
                            BillService billService,
                            CustomerService customerService) {
        this.paymentRepository = paymentRepository;
        this.billService = billService;
        this.customerService = customerService;
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        validatePayment(payment);
        payment.setPaymentDate(LocalDateTime.now());
        
        // Fatura kontrolü
        Bill bill = billService.findById(payment.getBillId())
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı"));
                
        // Müşteri kontrolü
        //Customer customer = customerService.findById(payment.getCustomerId())
        //        .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));

        // Ödeme tutarı kontrolü
        if (!payment.getAmount().equals(bill.getAmount())) {
            throw new RuntimeException("Ödeme tutarı fatura tutarı ile eşleşmiyor");
        }

        // Fatura durumunu güncelle
        bill.setStatus("ODENDI");
        bill.setPaymentDate(payment.getPaymentDate().toLocalDate());
        billService.save(bill);

        payment.setStatus("TAMAMLANDI");
        return paymentRepository.save(payment);
    }

    private void validatePayment(Payment payment) {
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new RuntimeException("Geçersiz ödeme tutarı");
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().trim().isEmpty()) {
            throw new RuntimeException("Ödeme yöntemi belirtilmedi");
        }
        if (payment.getCustomerId() == null) {
            throw new RuntimeException("Müşteri bilgisi eksik");
        }
        if (payment.getBillId() == null) {
            throw new RuntimeException("Fatura bilgisi eksik");
        }
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> findByCustomerId(Long customerId) {
        // Müşteri varlığını kontrol et
        customerService.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));
        return paymentRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Payment> findByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new RuntimeException("Geçersiz durum değeri");
        }
        return paymentRepository.findByStatus(status);
    }

    @Override
    public List<Payment> findByPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new RuntimeException("Geçersiz ödeme yöntemi");
        }
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ödeme bulunamadı"));
        
        if ("TAMAMLANDI".equals(payment.getStatus())) {
            throw new RuntimeException("Tamamlanmış ödemeler silinemez");
        }
        
        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
        payment.setCustomerId(paymentRequestDTO.getCustomerId());
        payment.setBillId(paymentRequestDTO.getBillId());
        
        Payment savedPayment = save(payment);
        
        return new PaymentResponseDTO(
            savedPayment.getId(),
            savedPayment.getStatus(),
            savedPayment.getPaymentDate()
        );
    }

    @Override
    public PaymentResponseDTO getPaymentStatus(Long paymentId) {
        Payment payment = findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Ödeme bulunamadı"));
            
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getStatus(),
            payment.getPaymentDate()
        );
    }
} 