package com.backend.splitbill.service;

import java.util.Map;

public interface SplitBillService {

    public Map<String, Object> splitTheBill(Map<String, Object> data) throws Exception;
}
