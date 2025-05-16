package com.crystal.bazarposmobile.retrofit.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RequestHeaderList implements Serializable {

    @SerializedName("Type")
    private String type;

    @SerializedName("StoreIds")
    private List<String> storeIds;

    @SerializedName("CustomerId")
    private String customerId;

    @SerializedName("EndDate")
    private String endDate;

    @SerializedName("BeginDate")
    private String beginDate;

    @SerializedName("PageIndex")
    private Integer pageIndex;

    @SerializedName("PageSize")
    private Integer pageSize;

    public RequestHeaderList(String type, List<String> storeIds, String customerId, String endDate, String beginDate, Integer pageIndex, Integer pageSize) {
        this.type = type;
        this.storeIds = storeIds;
        this.customerId = customerId;
        this.endDate = endDate;
        this.beginDate = beginDate;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setStoreIds(List<String> storeIds){
        this.storeIds = storeIds;
    }

    public List<String> getStoreIds(){
        return storeIds;
    }

    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }

    public String getCustomerId(){
        return customerId;
    }

    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public void setBeginDate(String beginDate){
        this.beginDate = beginDate;
    }

    public String getBeginDate(){
        return beginDate;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "RequestHeaderList{" +
                "type='" + type + '\'' +
                ", storeIds=" + storeIds +
                ", customerId='" + customerId + '\'' +
                ", endDate='" + endDate + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}