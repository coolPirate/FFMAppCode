package ffm.geok.com.model;

import java.util.List;

public class LoginRefObj {
    private boolean isNewRecord;
    private String id;
    private String updateDate;
    private String updateBy;
    private String createBy;
    private String status;
    private String createDate;
    private String empCode;
    private String empName;
    private String empNameEn;
    private LoginOffice office;
    private LoginCompany company;
    private List<String> employeePostList;
    private List<String> employeePosts;

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpNameEn() {
        return empNameEn;
    }

    public void setEmpNameEn(String empNameEn) {
        this.empNameEn = empNameEn;
    }

    public LoginOffice getOffice() {
        return office;
    }

    public void setOffice(LoginOffice office) {
        this.office = office;
    }

    public LoginCompany getCompany() {
        return company;
    }

    public void setCompany(LoginCompany company) {
        this.company = company;
    }

    public List<String> getEmployeePostList() {
        return employeePostList;
    }

    public void setEmployeePostList(List<String> employeePostList) {
        this.employeePostList = employeePostList;
    }

    public List<String> getEmployeePosts() {
        return employeePosts;
    }

    public void setEmployeePosts(List<String> employeePosts) {
        this.employeePosts = employeePosts;
    }
}
