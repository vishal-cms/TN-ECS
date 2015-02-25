package cms.com.tn_ecs.objectholders;

import java.util.ArrayList;

/**
 * Created by vishal_mokal on 13/2/15.
 */
public class PropertyTaxArrears {

    private String name;
    private String od;
    private String st;
    private ArrayList<HalfYear> halfYear;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public ArrayList<HalfYear> getHalfYear() {
        return halfYear;
    }

    public void setHalfYear(ArrayList<HalfYear> halfYear) {
        this.halfYear = halfYear;
    }
}

