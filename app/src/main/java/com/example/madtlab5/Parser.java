package com.example.madtlab5;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

public class Parser {

    // Method to parse JSON data
    public List<CurrencyRate> parseJson(String jsonData) {
        List<CurrencyRate> currencyRates = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            // Loop through all currency objects
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject currencyObject = jsonObject.getJSONObject(key);

                String code = currencyObject.getString("code");
                double rate = currencyObject.getDouble("rate");
                currencyRates.add(new CurrencyRate(code, rate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencyRates;
    }



    // Method to parse XML data
    public List<CurrencyRate> parseXml(String xmlData) {
        List<CurrencyRate> currencyRates = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlData));
            Document doc = builder.parse(is);

            // Parse <item> nodes for currency info
            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element itemElement = (Element) itemList.item(i);

                // Extract <targetCurrency> and <exchangeRate> elements
                String code = itemElement.getElementsByTagName("targetCurrency").item(0).getTextContent();
                double rate = Double.parseDouble(itemElement.getElementsByTagName("exchangeRate").item(0).getTextContent());

                currencyRates.add(new CurrencyRate(code, rate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencyRates;
    }
}
