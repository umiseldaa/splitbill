package com.backend.splitbill.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.backend.splitbill.service.SplitBillService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SplitBillServiceImpl implements SplitBillService {

    @Override
    public Map<String, Object> splitTheBill(Map<String, Object> data) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            String username = "umiseldaa";
            BigDecimal totalGroupExpense = BigDecimal.ZERO;

            String title = data.get("title").toString();
            List<String> allParticipant = (List<String>) data.get("all_participant");
            List<Map<String, Object>> detail = (List<Map<String, Object>>) data.get("detail");

            // System.out.println("cb: "+detail.get(0).get("item").toString());
            // List<Map<String, Object>> listDetailItemEachPar = new ArrayList<>();
            // for (int i = 0; i < detail.size(); i++) {
            // Map<String, Object> temp = new HashMap<>();
            // List<String> buyer = (List<String>) detail.get(i).get("participant");
            // BigDecimal itemPrice = (BigDecimal) detail.get(i).get("price");
            // BigDecimal price = itemPrice.divide(BigDecimal.valueOf(buyer.size()), 2,
            // RoundingMode.HALF_UP);
            // temp.put("item", detail.get(i).get("item"));
            // temp.put("priceEachParticipant", price);
            // listDetailItemEachPar.add(temp);
            // }
            for (int i = 0; i < detail.size(); i++) {
                List<String> buyer = (List<String>) detail.get(i).get("participant");
                BigDecimal itemPrice = new BigDecimal(detail.get(i).get("price").toString());
                BigDecimal price = itemPrice.divide(BigDecimal.valueOf(buyer.size()), 2, RoundingMode.HALF_UP);
                // untuk total group sekalian
                totalGroupExpense = totalGroupExpense.add(itemPrice);

                detail.get(i).put("price_for_each_participant", price);
            }

            int serviceChargePct = calcServiceChargePct(username);
            BigDecimal serviceChargeAmount = totalGroupExpense.multiply(BigDecimal.valueOf(serviceChargePct));

            // System.out.println("new detail : " + detail);

            // List<Map<String, Object>> listPartiWithItemBought = new ArrayList<>();
            // for (int i = 0; i < allParticipant.size(); i++) {
            // Map<String, Object> temp = new HashMap<>();
            // String name = allParticipant.get(i).toString();

            // List<String> item = new ArrayList<>();
            // for (int j = 0; j < detail.size(); j++) {
            // List<String> buyerOfItem = (List<String>) detail.get(j).get("participant");
            // if (buyerOfItem.contains(name)) {
            // item.add(detail.get(j).get("item").toString());
            // }
            // }
            // System.out.println("name: " + name + " | item: " + item);
            // temp.put("name", name);
            // }
            // List<Map<String, Object>> listBuyerWithItem = new ArrayList<>();
            // for (int i = 0; i < listPartiWithItemBought.size(); i++) {
            // Map<String, Object> temp = new HashMap<>();
            // List<Map<String, Object>> expenseDetail = new ArrayList<>();

            // for (int j = 0; j < array.length; j++) {

            // }

            // temp.put("payer_name",listPartiWithItemBought.get(i).get("name"));
            // temp.put("expense_detail", expenseDetail);
            // }

            List<Map<String, Object>> listPartiWithItemBought = new ArrayList<>();
            for (int i = 0; i < allParticipant.size(); i++) {
                Map<String, Object> temp = new LinkedHashMap<>();
                String payer_name = allParticipant.get(i).toString();

                List<String> item = new ArrayList<>();
                List<Map<String, Object>> listExpense = new ArrayList<>();

                for (int j = 0; j < detail.size(); j++) {
                    List<String> participantOfItem = (List<String>) detail.get(j).get("participant");
                    if (participantOfItem.contains(payer_name)) {
                        // Map<String, Object> mapExpense = new HashMap<>();
                        item.add(detail.get(j).get("item").toString());
                        // mapExpense.put(payer_name, mapExpense)
                        listExpense.add((Map<String, Object>) detail.get(j));
                    }
                }
                // System.out.println("payer_name: " + payer_name + " | item: " + item);
                temp.put("payer_name", payer_name);
                temp.put("list_expense", listExpense);
                listPartiWithItemBought.add(temp);
            }
            // System.out.println("listpartiwithitembought : " + listPartiWithItemBought);

            List<Map<String, Object>> splitBillResult = new ArrayList<>();
            for (int i = 0; i < listPartiWithItemBought.size(); i++) {
                List<Map<String, Object>> listDetailExpense = new ArrayList<>();
                Map<String, Object> temp = new LinkedHashMap<>();
                List<Map<String, Object>> listExpense = (List<Map<String, Object>>) listPartiWithItemBought.get(i)
                        .get("list_expense");

                String payer_name = listPartiWithItemBought.get(i).get("payer_name").toString();
                String tempPaidTo = "";
                BigDecimal tempTotalMoneyOwe = BigDecimal.ZERO;

                for (int j = 0; j < listExpense.size(); j++) {
                    Map<String, Object> tempInnerExpenseDetail = new LinkedHashMap<>();

                    tempPaidTo = listExpense.get(j).get("paid_by").toString();
                    tempTotalMoneyOwe = new BigDecimal(listExpense.get(j).get("price_for_each_participant").toString());

                    // skip bayar ke diri sendiri
                    if (payer_name.equals(tempPaidTo)) {
                        continue;
                    }

                    boolean found = false;
                    for (Map<String, Object> existing : listDetailExpense) {
                        if (existing.get("paid_to").equals(tempPaidTo)) {
                            BigDecimal existingTotal = (BigDecimal) existing.get("total");
                            existing.put("total", existingTotal.add(tempTotalMoneyOwe));
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        tempInnerExpenseDetail.put("paid_to", tempPaidTo);
                        tempInnerExpenseDetail.put("total", tempTotalMoneyOwe);
                        listDetailExpense.add(tempInnerExpenseDetail);
                    }

                    // System.out.println("----------------------------");
                    // System.out.println("payer_name: " +
                    // listPartiWithItemBought.get(i).get("payer_name").toString()
                    // + "| tempinnerdetail:" + tempInnerExpenseDetail);
                    // System.out.println("----------------------------");
                }
                temp.put("payer_name", payer_name);
                temp.put("detail_expense", listDetailExpense);
                splitBillResult.add(temp);
            }

            result.put("title", title);
            result.put("service_charge_pct", serviceChargePct);
            result.put("service_charge_amount", serviceChargeAmount);
            result.put("split_bill_result", splitBillResult);
            // System.out.println("====================result : " + result);
        } catch (Exception e) {
            log.error("[Split the Bill Service] Error: \n", e);
            throw new Exception(e.getMessage().toString());
        }
        return result;
    }

    private int calcServiceChargePct(String username) {
        int sum = 0;
        char[] chr = username.toLowerCase().toCharArray();

        for (int i = 0; i < chr.length; i++) {
            sum += (int) chr[i];
        }

        return sum % 10;
    }

}
