package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

//@Transactional
@Service
@RequiredArgsConstructor
public class StockService {

  private final StockRepository stockRepository;

  // TODO NamedLock 사용시 부모의 Transaction 과 별도로 실행되게하기 위해 propagation = Propagation.REQUIRES_NEW
//  @Transactional(TxType.REQUIRES_NEW)
  @Transactional
  public synchronized void decrease(Long id, Long quantity) {
    Stock stock = stockRepository.findById(id).orElseThrow();
    stock.decrease(quantity);
    stockRepository.save(stock);
  }

}
