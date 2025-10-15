package com.driven.dm.order.infrastructure.event;

import com.driven.dm.order.domain.entity.OrderStatusHistory;
import com.driven.dm.order.domain.event.OrderStatusChangedEvent;
import com.driven.dm.order.infrastructure.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderStatusHistoryEventHandler {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onStatusChanged(OrderStatusChangedEvent event) {
        orderStatusHistoryRepository.save(OrderStatusHistory.of(event));
    }
}
