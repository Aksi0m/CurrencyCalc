package aksiom.currencycalc.realm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Aksiom on 6/29/2016.
 */
public class Currency extends RealmObject {

    @SerializedName("median_rate")
    @Expose
    private String medianRate;
    @SerializedName("unit_value")
    @Expose
    private Integer unitValue;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("buying_rate")
    @Expose
    private String buyingRate;
    @SerializedName("selling_rate")
    @Expose
    private String sellingRate;
    private Date downloadDate;

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Integer unitValue) {
        this.unitValue = unitValue;
    }

    public String getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(String buyingRate) {
        this.buyingRate = buyingRate;
    }

    public String getMedianRate() {
        return medianRate;
    }

    public void setMedianRate(String medianRate) {
        this.medianRate = medianRate;
    }

    public String getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(String sellingRate) {
        this.sellingRate = sellingRate;
    }
}
