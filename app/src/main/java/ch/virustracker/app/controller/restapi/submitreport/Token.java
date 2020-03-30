package ch.virustracker.app.controller.restapi.submitreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Token sent into Report for POST /vt-server-token
 */
public class Token {

    @SerializedName("preimage")
    @Expose
    private String preimage;

    @SerializedName("lat")
    @Expose
    private Double lat;
    
    @SerializedName("long")
    @Expose
    private Double _long;

    public String getPreimage() {
        return preimage;
    }

    public void setPreimage(String preimage) {
        this.preimage = preimage;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

}