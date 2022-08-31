package com.example.stock.facade;

import com.example.stock.service.StockService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.stereotype.Component;

// TODO Redisson 은 pusub 기반이기에 Redis 에 부하 적다 그러나 외부 라이브러리 사용해야하고, 구현이 좀 더 복잡하다.
@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

  private final RedissonClient redissonClient;

  private final StockService stockService;

  public void decrease(Long id, Long quantity) {
    RLock lock = redissonClient.getLock(id.toString());

    try {
      boolean tryLock = lock.tryLock(5, 1, TimeUnit.SECONDS);
      if (!tryLock) {
        System.out.println("lock 획득 실패");
        return;
      }

      stockService.decrease(id, quantity);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }finally {
      lock.unlock();
    }
  }

}
