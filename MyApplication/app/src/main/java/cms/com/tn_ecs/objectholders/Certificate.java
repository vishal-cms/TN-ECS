package cms.com.tn_ecs.objectholders;

/**
 * Created by vishal_mokal on 29/1/15.
 */
public class Certificate {
    private String regNo;
    private String name;
    private String sex;
    private String fatherName;
    private String motherName;
    private String date;
    private String englishUrl;

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnglishUrl() {
        return englishUrl;
    }

    public void setEnglishUrl(String englishUrl) {
        this.englishUrl = englishUrl;
    }
}
