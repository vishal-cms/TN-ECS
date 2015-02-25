package cms.com.tn_ecs.objectholders;

/**
 * Created by vishal_mokal on 13/2/15.
 */
public class ZoneInfo {

    private String zoneID;
    private String zoneName;
    private String sunDivisions;


    public String getSunDivisions() {
        return sunDivisions;
    }

    public void setSunDivisions(String sunDivisions) {
        this.sunDivisions = sunDivisions;
    }

    public String getZoneID() {

        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }


}
