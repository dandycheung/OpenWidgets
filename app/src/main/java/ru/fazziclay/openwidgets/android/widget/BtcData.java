package ru.fazziclay.openwidgets.android.widget;

import android.annotation.SuppressLint;

public class BtcData {
    // all values initialized to null
    public String id = null;
    public String symbol = null;
    public String name = null;
    public String image = null;
    public Double current_price = null;
    public Long market_cap = null;
    public Integer market_cap_rank = null;
    public Long fully_diluted_valuation = null;
    public Long total_volume = null;
    public Double high_24h = null;
    public Double low_24h = null;
    public Float price_change_24h = null;
    public Float price_change_percentage_24h = null;
    public Float market_cap_change_24h = null;
    public Float market_cap_change_percentage_24h = null;
    public Integer circulating_supply = null;
    public Integer total_supply = null;
    public Integer max_supply = null;
    public Double ath = null;
    public Double ath_change_percentage = null;
    public String ath_date = null;
    public Double atl = null;
    public Double atl_change_percentage = null;
    public String atl_date = null;
    public String roi = null;
    public String last_updated = null;

    /**
     *  Formats the 24-hour price change information to a string
     *  in the users' locale and adds a '+' symbol if the
     *  change is positive (and '-' symbol if negative)
     *
     *  @return the 24-hour price change percentage as a String
     */
    public String dayChangeString() {
        // format change percent with two decimal places
        // and using LOCALE information (by default)

        @SuppressLint("DefaultLocale")
        String changeStr = String.format("%.2f", price_change_percentage_24h);

        return (price_change_percentage_24h > 0 ? "+" : "") + changeStr +"%";
    }

    /**
     * Formats the price information to a string
     * and adds a decimal or comma where needed.
     *
     * @return the current price as a String
     */
    String priceString() {
        String priceString = current_price.toString();
        int length = priceString.length();

        // API returns cents with only 1 int if ending in 0
        // example: 12,345.9 (not .90)
        // check to add extra zero(s) if needed

        // string already contains a period
        if (priceString.contains(".")) {
            // if the decimal is followed by two integers, do nothing
            if (priceString.charAt(length - 3) == '.') {
                // do nothing
            } else {
                // add a zero to the end
                priceString += "0";
            }
        } else {
            // string contains no period
            // adds extra zeros
            priceString += ".00";
        }

        String resultString = "";

        length = priceString.length();    // update value of length
        if (length > 6) {                 // over $1,000 (for example)
            resultString = priceString.subSequence(0, priceString.length() - 6) + ","
                + priceString.subSequence(priceString.length() - 6, priceString.length());

            if (length > 9) {             // over $1,000,000 (for example)
                length = resultString.length();
                resultString = resultString.subSequence(0, length - 10) + ","
                    + resultString.subSequence(length - 10, length);
            }
        }

        return resultString;
    }
}
