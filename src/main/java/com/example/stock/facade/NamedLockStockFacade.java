package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// TODO NamedLock 은 타임아웃을 손쉽게 구현할 수 있고 분산락을 구현할 때 주로 사용된다.
@Service
@RequiredArgsConstructor
public class NamedLockStockFacade {

  private final LockRepository lockRepository;
  private final StockService stockService;

  @Transactional
  public void decrease(Long id, Long quantity) {
    try {
      lockRepository.getLock(id.toString());
      stockService.decrease(id, quantity);
    } finally {
      lockRepository.releaseLock(id.toString());
    }
  }

}
