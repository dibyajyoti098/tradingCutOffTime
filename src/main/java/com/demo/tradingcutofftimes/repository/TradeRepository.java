package com.demo.tradingcutofftimes.repository;

import com.demo.tradingcutofftimes.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TradeRepository extends JpaRepository<Trade,Long> {

    @Query("SELECT td.cutOffTimes FROM trade td WHERE td.date=:date and td.iso in(:isoPair)")
    List<String> findCutOffTimeByCurrencyPairAndDate(String date,List<String> isoPair);

    @Query("SELECT td.iso FROM trade td WHERE  td.iso=:iso")
    String getIso(String iso);

    @Query("SELECT td.date FROM trade td WHERE  td.date=:date")
    String getDate(String date);

    @Query("SELECT td.id FROM trade td WHERE  td.id=:id")
    String getId(Long id);
}
