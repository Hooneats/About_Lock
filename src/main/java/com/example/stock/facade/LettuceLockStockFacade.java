package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// TODO Lettuce 는 구현이 간단하나 Spin Lock 방식이기에 Redis 에 부하를 줄 수 있다.
@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

  private final RedisLockRepository redisLockRepository;

  private final StockService stockService;

  public void decrease(Long key, Long quantity) throws InterruptedException {
    while (!redisLockRepository.lock(key)) {
      // TODO Redis 에 부하를 덜기위해 살짝 sleep
      Thread.sleep(100);
    }

    try {
      stockService.decrease(key, quantity);
    } finally {
      redisLockRepository.unlock(key);
    }
  }

}
