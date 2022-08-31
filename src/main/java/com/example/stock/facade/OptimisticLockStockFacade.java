package com.example.stock.facade;

import com.example.stock.repository.StockRepository;
import com.example.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

  private final OptimisticLockStockService optimisticLockStockService;

  // TODO OptimisticLock 은 데이터 업데이트가 실패했을때 재시도를 명시해줘야한다.
  public void decrease(Long id, Long quantity) throws InterruptedException {
    while (true) {
      try {
        optimisticLockStockService.decrease(id, quantity);

        break;
      } catch (Exception e) {
        Thread.sleep(50);
      }
    }
  }


}
