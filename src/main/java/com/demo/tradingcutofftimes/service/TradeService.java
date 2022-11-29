package com.demo.tradingcutofftimes.service;

import com.demo.tradingcutofftimes.model.Trade;
import com.demo.tradingcutofftimes.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class TradeService {


    @Autowired
    private TradeRepository tradeRepository;


    public ResponseEntity<List<Trade>> getAllTradingDetails() {
        try {

            List<Trade> tradeList = new ArrayList<>();
            tradeRepository.findAll().forEach(tradeList::add);

            if (tradeList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tradeList, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Trade> getTradingDetailsById(@PathVariable Long id) {

        Optional<Trade> tradingData = tradeRepository.findById(id);
        return tradingData.map(trade -> new ResponseEntity<>(trade, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> findCutOffTimeByCurrencyPairAndDate(@PathVariable String date, @PathVariable String iso1,@PathVariable String iso2) {

        StringBuilder tradingCutOffTime = new StringBuilder();
        ResponseEntity<String> validationResult=validateIsoPairAndDate(date,iso1,iso2);
        if(!validationResult.getStatusCode().equals(HttpStatus.OK)) {
            return validationResult;
        }
        List<String> isoPair=new ArrayList<>();
        isoPair.add(iso1);
        isoPair.add(iso2);
            List<String> cutOffTimeListByCurrencyPairAndDate = tradeRepository.findCutOffTimeByCurrencyPairAndDate(date, isoPair);

            if (cutOffTimeListByCurrencyPairAndDate.size() > 0) {

                if (cutOffTimeListByCurrencyPairAndDate.get(0) != null && cutOffTimeListByCurrencyPairAndDate.get(1) != null) {
                    if (cutOffTimeListByCurrencyPairAndDate.get(0).equalsIgnoreCase("Never possible")) { //first Never possible, return "Never possible"
                        tradingCutOffTime.append(cutOffTimeListByCurrencyPairAndDate.get(1));
                        return new ResponseEntity<>(String.valueOf(tradingCutOffTime), HttpStatus.OK);
                    } else if (cutOffTimeListByCurrencyPairAndDate.get(1).equalsIgnoreCase("Never possible")) { //last Never possible, return "Never possible"
                        tradingCutOffTime.append(cutOffTimeListByCurrencyPairAndDate.get(1));
                        return new ResponseEntity<>(String.valueOf(tradingCutOffTime), HttpStatus.OK);
                    } else if (cutOffTimeListByCurrencyPairAndDate.get(0).equalsIgnoreCase("Always possible") || cutOffTimeListByCurrencyPairAndDate.get(1).equalsIgnoreCase("Always possible")) { //Any or both Always possible
                        if (cutOffTimeListByCurrencyPairAndDate.get(0).equalsIgnoreCase(cutOffTimeListByCurrencyPairAndDate.get(1))) {//Match cases, both "Always possible"--- return "Always possible"
                            tradingCutOffTime.append(cutOffTimeListByCurrencyPairAndDate.get(0));
                            return new ResponseEntity<>(String.valueOf(tradingCutOffTime), HttpStatus.OK);
                        } else { //"Always possible" and number combination
                            if (!cutOffTimeListByCurrencyPairAndDate.get(0).equalsIgnoreCase("Always possible")) { // first data having numeric value--return that
                                tradingCutOffTime.append(cutOffTimeListByCurrencyPairAndDate.get(0));
                                return new ResponseEntity<>(String.valueOf(tradingCutOffTime), HttpStatus.OK);
                            } else if (!cutOffTimeListByCurrencyPairAndDate.get(1).equalsIgnoreCase("Always possible")) { //second data having numeric value---return that
                                tradingCutOffTime.append(cutOffTimeListByCurrencyPairAndDate.get(1));
                                return new ResponseEntity<>(String.valueOf(tradingCutOffTime), HttpStatus.OK);
                            }
                        }
                    } else { //two cut off time compare by removing . and converting to int from string and  return earlier cutoff date
                        int firstCutOffTime = Integer.parseInt(cutOffTimeListByCurrencyPairAndDate.get(0).replace(".", ""));
                        int nextCutOffTime = Integer.parseInt(cutOffTimeListByCurrencyPairAndDate.get(1).replace(".", ""));
                        if (firstCutOffTime >= nextCutOffTime) {
                            tradingCutOffTime.append(cutOffTimeListByCurrencyPairAndDate.get(1));
                        } else {
                            tradingCutOffTime.append(cutOffTimeListByCurrencyPairAndDate.get(0));
                        }
                        return new ResponseEntity<>(String.valueOf(tradingCutOffTime), HttpStatus.OK);
                    }

                }


            }

        return new ResponseEntity<String>(String.valueOf(tradingCutOffTime), HttpStatus.OK);
    }


    public ResponseEntity<Trade> addTradingDetails(@RequestBody Trade trade) {

        Trade tradeObj = tradeRepository.save(trade);

        return new ResponseEntity<>(tradeObj, HttpStatus.OK);
    }

    public ResponseEntity<List<Trade>> addTradingDetailsInBulk(@RequestBody List<Trade> tradeList) {

        List<Trade> tradeObjList;
        if (tradeList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            tradeObjList = tradeRepository.saveAll(tradeList);
        }
        return new ResponseEntity<>(tradeObjList, HttpStatus.OK);
    }

    public ResponseEntity<Trade> updateTradingDetailsById(@PathVariable Long id, @RequestBody Trade newTradeData) {
        Optional<Trade> oldTradeData = tradeRepository.findById(id);
        if (oldTradeData.isPresent()) {
            Trade updatedTradeData = oldTradeData.get();
            updatedTradeData.setIso(newTradeData.getIso());
            updatedTradeData.setCountry(newTradeData.getCountry());
            updatedTradeData.setDate(newTradeData.getDate());
            updatedTradeData.setCutOffTimes(newTradeData.getCutOffTimes());
            Trade tradeObj = tradeRepository.save(updatedTradeData);
            return new ResponseEntity<>(tradeObj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteTradingDetailsById(@PathVariable Long id) {

        ResponseEntity<String> validationResult=validateId(id);
        if(!validationResult.getStatusCode().equals(HttpStatus.OK)) {
            return validationResult;
        }
        tradeRepository.deleteById(id);
        return new ResponseEntity<>("Id has been deleted",HttpStatus.OK);
    }

    protected ResponseEntity<String> validateIsoPairAndDate(String date1,String iso1,String iso2){

        String isoFirst=tradeRepository.getIso(iso1);
        String isoSecond=tradeRepository.getIso(iso2);
        String date=tradeRepository.getDate(date1);

            if(isoFirst==null || isoSecond==null){
             return   new ResponseEntity<>("Input Currency Pair are not correct",HttpStatus.BAD_REQUEST);
            }
            else if(date==null){
                return   new ResponseEntity<>("Date should be either {Today or Tomorrow or AfterTomorrow}",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.OK);
    }

    protected  ResponseEntity<String> validateId(Long id){
        String id1=tradeRepository.getId(id);
        if(id1==null) {
            return new ResponseEntity<>("Id is not present, Please provide correct Id",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
