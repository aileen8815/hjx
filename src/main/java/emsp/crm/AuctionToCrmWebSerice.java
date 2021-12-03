package emsp.crm;

public abstract interface AuctionToCrmWebSerice {
    public abstract String getMessage();

    public abstract String getCustomer(String paramString1, String paramString2);

    public abstract String PayCashCard1(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2, int paramInt4, int paramInt5,
            int paramInt6, int paramInt7, String paramString3);

    public abstract String CancelCRMTrans(String paramString, int paramInt);

    public abstract String CancelCRMTrans(int paramInt1, int paramInt2);

    public abstract String getCDNR(String paramString) throws Throwable;
}
