package wang.patrick.smarter;

public class ResCred {
    String credusername;
    Resident resid;
    String credhash;
    String credreg;

    public ResCred(String credusername, Resident resid,String credhash,String credreg){
        this.credusername =credusername;
        this.resid =resid;
        this.credhash =credhash;
        this.credreg =credreg;
    }
}
