package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class VoucherList implements Serializable {
    private Date date;
    private Iterable<VoucherData> data;

    public VoucherList() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<VoucherData> getData() {
        return data;
    }

    public void setData(Iterable<VoucherData> data) {
        this.data = data;
    }
}
