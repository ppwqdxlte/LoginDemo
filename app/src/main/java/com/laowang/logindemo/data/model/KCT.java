package com.laowang.logindemo.data.model;

import java.io.Serializable;

public class KCT extends BaseToken<TokenType> implements Serializable {

    private static final Long serialVersionUID = 1L;
    private final TokenType TOKEN_TYPE = TokenType.KCT;

    public KCT() {
        setTokenType(TOKEN_TYPE);
    }

    public KCT(String batchNo, String meterNo, String token, String date) {
        super(batchNo, meterNo, token, date);
        setTokenType(TOKEN_TYPE);
    }

    @Override
    public TokenType getTOKEN_TYPE() {
        return TOKEN_TYPE;
    }
}
