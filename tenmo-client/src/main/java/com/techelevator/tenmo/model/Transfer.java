package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Integer transferID;
    private Integer transferTypeId;
    private Integer transferStatusId;
    private Integer accountFrom;
    private Integer accountTo;
    private BigDecimal amount;

    public Transfer() {
    }

    public Transfer(Integer transferID, Integer transferTypeId, Integer transferStatusId, Integer accountFrom, Integer accountTo, BigDecimal amount) {
        this.transferID = transferID;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Integer getTransferID() {
        return transferID;
    }

    public void setTransferID(Integer transferID) {
        this.transferID = transferID;
    }

    public Integer getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Integer transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public Integer getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Integer transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Integer getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Integer getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Integer accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferID=" + transferID +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", amount=" + amount +
                '}';
    }
}
