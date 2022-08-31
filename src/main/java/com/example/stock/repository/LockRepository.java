package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// TODO NamedLock
// TODO 실무에서는 별도의 datasource(쓰레드풀이 부족해질 수 있기에) 또는 Stock 에 바로 쓰는게아닌 별도의 jdbc 사용해야 한다.
public interface LockRepository extends JpaRepository<Stock, Long> {

  @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
  void getLock(String key);

  @Query(value = "select release_lock(:key)", nativeQuery = true)
  void releaseLock(String key);
}
