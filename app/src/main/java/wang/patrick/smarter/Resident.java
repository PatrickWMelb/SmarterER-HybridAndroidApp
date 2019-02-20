package wang.patrick.smarter;

public class Resident {
    Integer resid;
    String resfirstname;
    String ressurname;
    String resaddress;
    String respostcode;
    String resemail;
    String resnumber;
    String resenergy;
    String resmobile;
    String resdob;



    public Resident(Integer resid,String resfirstname,String ressurname,String resaddress,String respostcode,String resemail,String resnumber,String resenergy,String resmobile,String resdob){
        this.resid =resid;
        this.resfirstname =resfirstname;
        this.ressurname = ressurname;
        this.resaddress = resaddress;
        this.respostcode =respostcode;
        this.resemail = resemail;
        this.resnumber =resnumber;
        this.resenergy= resenergy;
        this.resmobile = resmobile;
        this.resdob =resdob;
    }


}
