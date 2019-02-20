package wang.patrick.smarter;

import java.util.Random;

public class ElecUsageGen {


    private Integer WashCount = 0;
    private Integer WashChance;
    private Integer AirChance;
    private Integer AirCount = 0;
    private Double[] Fridge24 = new Double[24];
    private Double[] Wash24 = new Double[24];
    private Double[] Air24 = new Double[24];


    public void Gennew24() {
        Random r = new Random();
        int fridgeBase = r.nextInt(50) + 30;
        int washBase = r.nextInt(90) + 40;
        int airBase = r.nextInt(4) + 1;

        for (int i = 0; i < 24; i++) {
            Fridge24[i] = ((double) fridgeBase + r.nextInt(9)) / 100;
        }

        for (int i = 0; i < 24; i++) {
            if (i <= 5 || i >= 21) {
                Wash24[i] = 0.0;
            } else {
                Wash24[i] = 0.0;
                if (WashCount == 0) {
                    WashChance = r.nextInt(5);
                    if (WashChance > 3) {
                        Wash24[i] = ((double) washBase + r.nextInt(9)) / 100;
                        WashCount++;
                    }

                } else if ((WashCount < 3) && (Wash24[i - 1] > 0)) {
                    WashChance = r.nextInt(5);
                    if (WashChance > 2) {
                        Wash24[i] = ((double) washBase + r.nextInt(9)) / 100;
                        WashCount++;
                    }
                }
            }


        }
        for (int i = 0; i < 24; i++) {
            if (i < 8 || i >= 23) {
                Air24[i] = 0.0;
            } else if (AirCount < 10) {
                Air24[i] = 0.0;
                AirChance = r.nextInt(5);
                if (AirChance > 1) {
                    Air24[i] =  airBase + ((double)r.nextInt(9) / 10);
                    AirCount++;
            }


            }
        }


    }
    public String getFridge(Integer hour) {

        return Fridge24[hour].toString();

    }
    public String getWash(Integer hour) {

        return Wash24[hour].toString();

    }
    public String getAir(Integer hour, Double temp) {
        if (temp> 20){
            return Air24[hour].toString();
        }
        else{
            return "0";
        }




    }

}

