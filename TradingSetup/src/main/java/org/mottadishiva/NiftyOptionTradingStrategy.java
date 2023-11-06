package org.mottadishiva;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import org.mottadishiva.config.JsonFileReader;
import org.mottadishiva.constants.Instruments;
import org.mottadishiva.model.AccessRequest;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NiftyOptionTradingStrategy {


    public static void main(String[] args) {

        JsonFileReader JsonFileReader = new JsonFileReader();
        AccessRequest authentication = JsonFileReader.getAuthentication();
        System.out.println("authentication:" + authentication);


        KiteConnect kiteConnect = new KiteConnect(authentication.getApp_key());
        kiteConnect.setAccessToken(authentication.getAccess_token());

        String loginURL = kiteConnect.getLoginURL();

        System.out.println("Login URL: " + loginURL);


        // Fetch Nifty option instruments
        try {
            List<Instrument> nse = kiteConnect.getInstruments("NSE");
            createOrder(kiteConnect);
            calculateRSI(kiteConnect);
            // Implement your strategy logic here
            // Analyze historical and real-time data, generate buy/sell signals, and place orders
        } catch (Exception e) {
            e.printStackTrace();
        } catch (KiteException e) {
            throw new RuntimeException(e);
        }
    }

    private static void calculateRSI(KiteConnect kiteConnect) {
        try {
            // Fetch historical data for the instrument (e.g., RELIANCE)
            HistoricalData historicalDatas = kiteConnect.getHistoricalData(Date.from(Instant.now().minus(30, ChronoUnit.DAYS)), Date.from(Instant.now()), Instruments.RELIANCE.getInstrument(), "day", true, true);
            List<HistoricalData> historicalData = historicalDatas.dataArrayList;
            // Calculate RSI
            int period = 14; // RSI period
            double avgGain = 0;
            double avgLoss = 0;

            for (int i = 1; i < historicalData.size(); i++) {
                double priceDifference = historicalData.get(i).close - historicalData.get(i - 1).close;
                if (priceDifference >= 0) {
                    avgGain += priceDifference;
                } else {
                    avgLoss -= priceDifference;
                }

                if (i >= period) {
                    double rs = avgGain / period / (avgLoss / period);
                    double rsi = 100 - (100 / (1 + rs));

                    // Implement your trading logic based on RSI values (e.g., buy/sell decisions)
                    if (rsi < 30) {
                        System.out.println("RSI is below 30. Buy signal.");
                        // Implement buy logic here
                    } else if (rsi > 70) {
                        System.out.println("RSI is above 70. Sell signal.");
                        // Implement sell logic here
                    }

                    // Remove the first data point from the average gain/loss calculation
                    double prevPriceDifference = historicalData.get(i - period).close - historicalData.get(i - period - 1).close;
                    if (prevPriceDifference >= 0) {
                        avgGain -= prevPriceDifference;
                    } else {
                        avgLoss += prevPriceDifference;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (KiteException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createOrder(KiteConnect kiteConnect) {
        try {
            OrderParams orderParams = new OrderParams();
            orderParams.exchange = "NSE";
            orderParams.tradingsymbol = "INFY";
            orderParams.transactionType = "BUY";
            orderParams.quantity = 1;
            orderParams.orderType = "MARKET";
            orderParams.product = "CNC";
            Order order = kiteConnect.placeOrder(orderParams, "regular");
            System.out.println("Order ID: " + order.orderId);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (KiteException e) {
            throw new RuntimeException(e);
        }
    }

    // Implement your strategy logic and order placement methods here
}
