package ffm.geok.com.model;

import java.util.List;

public class LoginCompany {
    private boolean isNewRecord;
    private String companyCode;
    private String companyName;
    private List<String> companyOfficeList;
    private boolean isRoot;
    private boolean isTreeLeaf;

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<String> getCompanyOfficeList() {
        return companyOfficeList;
    }

    public void setCompanyOfficeList(List<String> companyOfficeList) {
        this.companyOfficeList = companyOfficeList;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public boolean isTreeLeaf() {
        return isTreeLeaf;
    }

    public void setTreeLeaf(boolean treeLeaf) {
        isTreeLeaf = treeLeaf;
    }
}
