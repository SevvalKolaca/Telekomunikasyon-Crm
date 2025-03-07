package io.github.ergulberke.event.billingPayment;

import java.time.LocalDate;



public class BillCreatedEvent {

    private Long id;

    private String billNumber;

    private Double amount;

    private LocalDate dueDate;

    private LocalDate paymentDate;

    private String status;

    private Long customerId;


    public BillCreatedEvent(Long id, String billNumber, Double amount, LocalDate dueDate, LocalDate paymentDate, String status, Long customerId) {
        this.id = id;
        this.billNumber = billNumber;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.status = status;
        this.customerId = customerId;
    }

    public BillCreatedEvent() {

    }

    public Long getId() {
        return id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
