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


}
