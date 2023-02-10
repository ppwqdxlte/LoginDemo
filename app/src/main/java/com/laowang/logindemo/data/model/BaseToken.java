package com.laowang.logindemo.data.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class BaseToken<TT> implements Serializable {

    private static final Long serialVersionUID = 1L;
    /**
     * batch number:regex yyyyMMddhhmmss
     * eg. 20220330102525
     */
    private String batchNo = null;
    /**
     * meter ID
     * eg. 0150000844798
     */
    private String meterNo = null;
    /**
     * token or tokens
     * single-token: eg. 1004 7913 0897 6141 1858
     * multi-token:  eg. 4753 7213 9375 3945 8484    4028 3150 0447 6150 4382
     */
    private String token = null;
    /**
     * created data and time
     * eg. 2022-03-07 16:06:51.000
     */
    private String date = null;       // eg. 2022-03-30 10:25:25.000
    /**
     * token type
     * eg. TokenType.TCC  TokenType.KCT
     */
    private TT tokenType = null;           // TokenType defined in this system.

    public BaseToken(){}

    public BaseToken(String batchNo, String meterNo, String token, String date) {
        this.batchNo = batchNo;
        this.meterNo = meterNo;
        this.token = token;
        this.date = date;
    }

    @Override
    public String toString() {
        return "BaseToken{" +
                "batchNo='" + batchNo + '\'' +
                ", meterNo='" + meterNo + '\'' +
                ", token='" + token + '\'' +
                ", date=" + date +
                ", tokenType=" + tokenType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseToken)) return false;
        BaseToken<?> baseToken = (BaseToken<?>) o;
        return Objects.equals(getBatchNo(), baseToken.getBatchNo()) && Objects.equals(getMeterNo(), baseToken.getMeterNo()) && Objects.equals(getToken(), baseToken.getToken()) && Objects.equals(getDate(), baseToken.getDate()) && Objects.equals(tokenType, tokenType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBatchNo(), getMeterNo(), getToken(), getDate(), tokenType);
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected TT getTokenType(){
        return this.tokenType;
    }

    protected void setTokenType(TT tokenType){
        if (this.tokenType == null){
            this.tokenType = tokenType;
        }
    }

    public abstract TT getTOKEN_TYPE();
}
