package com.demo.tradingcutofftimes.controller;


import com.demo.tradingcutofftimes.model.Trade;
import com.demo.tradingcutofftimes.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@Validated
public class TradeController {

    @Autowired
    private TradeService tradeService;


   @GetMapping("/getAllTradingDetails")
    public ResponseEntity<List<Trade>> getAllTradingDetails() {
       return tradeService.getAllTradingDetails();
   }

    @GetMapping("/findCutOffTimeByCurrencyPairAndDate/{iso1}/{iso2}/{date}")
    public ResponseEntity<String> findCutOffTimeByCurrencyPairAndDate(@PathVariable String iso1,@PathVariable String iso2,@PathVariable String date){

        return tradeService.findCutOffTimeByCurrencyPairAndDate(date,iso1,iso2);
    }

    @GetMapping("/getTradingDetailsById/{id}")
    public ResponseEntity<Trade> getTradingDetailsById(@PathVariable Long id){

        return tradeService.getTradingDetailsById(id);
    }

    @PostMapping("/addTradingDetails")
    public ResponseEntity<Trade> addTradingDetails(@RequestBody @Valid  Trade trade){

      return  tradeService.addTradingDetails(trade);
    }

    @PostMapping("/addTradingDetailsInBulk")
    public ResponseEntity<List<Trade>> addTradingDetailsInBulk(@RequestBody @Valid  List<Trade> tradeList){

       return tradeService.addTradingDetailsInBulk(tradeList);
    }

    @PostMapping("/updateTradingDetailsById/{id}")
    public ResponseEntity<Trade> updateTradingDetailsById(@PathVariable Long id,@Valid @RequestBody Trade newTradeData){
       return tradeService.updateTradingDetailsById(id,newTradeData);
    }

    @DeleteMapping(("/deleteTradingDetailsById/{id}"))
    public ResponseEntity<String> deleteTradingDetailsById(@PathVariable Long id){
        return tradeService.deleteTradingDetailsById(id);
    }
}
