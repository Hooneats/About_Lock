package com.example.stock.facade;

import static org.junit.jupiter.api.Assertions.*;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import com.example.stock.service.PessimisticLockStockService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OptimisticLockStockFacadeTest {

  @Autowired
  private OptimisticLockStockFacade optimisticLockStockFacade;
  @Autowired
  private StockRepository repository;

  @BeforeEach
  public void before() {
    Stock stock = new Stock(1L, 100L);
    repository.save(stock);
  }
  @AfterEach
  public void after() {
    repository.deleteAll();
  }

  @Test
  public void 동시에_100개_요청() throws InterruptedException {
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    // TODO CountDownLatch 는 다른 쓰레드에서 작업이 수행되기까지 대기할 수 있게 해준다.
    CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          optimisticLockStockFacade.decrease(1L, 1L);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        } finally {
          countDownLatch.countDown();
        }
      });
    }
    countDownLatch.await();

    Stock stock = repository.findById(1L).orElseThrow();
    assertEquals(0L, stock.getQuantity());
  }

}