package wang.patrick.smarter;

public class ElecUsage {
    Integer usageid;
    Resident resid;
    Integer usagehour;
    Double usagefridge;
    Double usagewash;
    Double usageac;
    Double usagetemp;
    String usagedate;

    public ElecUsage(Integer UsageId, Resident resid,Integer UsageHour,String UsageFridge,String UsageAC,String UsageWash,String UsageTemp,String UsageDate){
        this.usageid = UsageId;
        this.resid =resid;
        this.usagefridge = Double.valueOf(UsageFridge);
        this.usagehour = UsageHour;
        this.usageac=Double.valueOf(UsageAC);
        this.usagewash=Double.valueOf(UsageWash);
        this.usagetemp=Double.valueOf(UsageTemp);
        this.usagedate=UsageDate;
    }
}