package com.turkcell.billing_payment_service.controller;

import com.turkcell.billing_payment_service.entity.Bill;
import com.turkcell.billing_payment_service.enums.PaymentMethod;
import com.turkcell.billing_payment_service.enums.PaymentStatus;
import com.turkcell.billing_payment_service.service.BillService;
import io.github.ergulberke.event.billingPayment.BillCreatedEvent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.stream.function.StreamBridge;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/bills")
public class BillController {
    private final BillService billService;
    private final StreamBridge streamBridge;

    public BillController(BillService billService, StreamBridge streamBridge) {
        this.billService = billService;
        this.streamBridge = streamBridge;
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        return new ResponseEntity<>(billService.save(bill), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        return billService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Bill>> getBillsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(billService.findByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Bill>> getBillsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(billService.findByStatus(status));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Bill>> getOverdueBills(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate checkDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(billService.findByDueDateBefore(checkDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public String get() {
    //     Bill bill = new Bill();
    //     bill.setBillNumber("1234567890");
    //     bill.setAmount(100.0);
    //     bill.setDueDate(LocalDate.now().plusDays(30));
    //     bill.setStatus("PENDING");
    //     bill.setCustomerId(1L);

        BillCreatedEvent billCreatedEvent = new BillCreatedEvent(1L,"asd",123.0,LocalDate.now(),LocalDate.now(), "BEKLIYOR",123L);

        streamBridge.send("billingPaymentFunction-out-0", billCreatedEvent);
        return "Bill & Payment Service";

        
    }
    




} 