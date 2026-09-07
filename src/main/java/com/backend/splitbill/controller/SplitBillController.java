package com.backend.splitbill.controller;

import org.springframework.web.bind.annotation.RestController;

import com.backend.splitbill.service.SplitBillService;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j 
@RestController 
public class SplitBillController {
    
    @Autowired 
    private SplitBillService splitBillService;

    @GetMapping ("/")
    public String home() throws Exception {
        String response = "";
        try {
            response = "<h3>SPLIT BILL</h3>";
        } catch (Exception e) {
            response = "<h3>KONEKSI GAGAL</h3>";
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/api/splitBill")
    public ResponseEntity<Map<String, Object>> splitBill(@RequestBody String payload) throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Map<String, Object> map = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(payload, Map.class);
            // System.out.println("payloadnya: "+ payload);
            // System.out.println("mapnya: "+map);
            
            Map<String, Object> data = splitBillService.splitTheBill(map);
            response.put("status", true);
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("lha kesini: "+e.getMessage());
            response.put("status", false);
            response.put("data", "Failed split bill");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}
