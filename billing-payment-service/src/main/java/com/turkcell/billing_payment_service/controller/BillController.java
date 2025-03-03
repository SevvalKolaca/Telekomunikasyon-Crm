package com.turkcell.billing_payment_service.controller;

import com.turkcell.billing_payment_service.entity.Bill;
import com.turkcell.billing_payment_service.service.BillService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
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
} 