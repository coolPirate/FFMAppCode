package ffm.geok.com.model;

import java.util.List;

public class ResponseModelLogin2 {

    /**
     * user : {"isNewRecord":false,"id":"fujian_j2sy","updateDate":"2019-03-21 13:44","updateBy":"system","createBy":"system","status":"0","remarks":"","createDate":"2019-03-21 13:44","refCode":"fujian_j2sy","userName":"福建省","mobile":"","extend":{"extendS6":"","extendS4":"","extendS5":"","extendS3":"","extendS2":"","extendS7":"","extendS1":"","extendS8":""},"mgrType":"0","loginCode":"fujian","phone":"","oldLastLoginIp":"117.136.0.229","userCode":"fujian_j2sy","userWeight":0,"userType":"employee","lastLoginIp":"117.136.0.229","email":"","refName":"福建省","lastLoginDate":"2019-04-11 18:00:21","refObj":{"isNewRecord":false,"id":"fujian_j2sy","updateDate":"2019-03-21 13:44","updateBy":"system","createBy":"system","status":"0","createDate":"2019-03-21 13:44","empCode":"fujian_j2sy","empName":"福建省","empNameEn":"","office":{"isNewRecord":false,"id":"35","updateDate":"2019-03-21 13:42","updateBy":"system","createBy":"system","status":"0","remarks":"","createDate":"2019-03-21 13:42","treeLevel":0,"treeSort":370,"treeSorts":"0000000370,","treeLeaf":"1","treeNames":"福建省","parentCodes":"0,","officeCode":"35","viewCode":"35","officeName":"福建省","fullName":"福建省","officeType":"3","leader":"","phone":"","address":"","zipCode":"","email":"","extend":{"extendS6":"","extendS4":"","extendS5":"","extendS3":"","extendS2":"","extendS7":"","extendS1":"","extendS8":""},"isRoot":true,"isTreeLeaf":true,"parentCode":"0"},"company":{"isNewRecord":true,"companyCode":"","companyName":"","companyOfficeList":[],"isRoot":false,"isTreeLeaf":false},"employeePostList":[],"employeePosts":[]},"corpCode_":"0","corpName_":"JeeSite","avatarUrl":"/ctxPath/static/images/user1.jpg","oldLoginDate":"2019-04-11 18:00:21"}
     * result : true
     * message : 登录成功！
     * sessionid : 54185d4b53d54571b4577ffb67e80b8e
     * __url : /fpims/front/index
     */

    private UserBean user;
    private String result;
    private String message;
    private String sessionid;
    private String __url;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String get__url() {
        return __url;
    }

    public void set__url(String __url) {
        this.__url = __url;
    }

    public static class UserBean {
        /**
         * isNewRecord : false
         * id : fujian_j2sy
         * updateDate : 2019-03-21 13:44
         * updateBy : system
         * createBy : system
         * status : 0
         * remarks :
         * createDate : 2019-03-21 13:44
         * refCode : fujian_j2sy
         * userName : 福建省
         * mobile :
         * extend : {"extendS6":"","extendS4":"","extendS5":"","extendS3":"","extendS2":"","extendS7":"","extendS1":"","extendS8":""}
         * mgrType : 0
         * loginCode : fujian
         * phone :
         * oldLastLoginIp : 117.136.0.229
         * userCode : fujian_j2sy
         * userWeight : 0
         * userType : employee
         * lastLoginIp : 117.136.0.229
         * email :
         * refName : 福建省
         * lastLoginDate : 2019-04-11 18:00:21
         * refObj : {"isNewRecord":false,"id":"fujian_j2sy","updateDate":"2019-03-21 13:44","updateBy":"system","createBy":"system","status":"0","createDate":"2019-03-21 13:44","empCode":"fujian_j2sy","empName":"福建省","empNameEn":"","office":{"isNewRecord":false,"id":"35","updateDate":"2019-03-21 13:42","updateBy":"system","createBy":"system","status":"0","remarks":"","createDate":"2019-03-21 13:42","treeLevel":0,"treeSort":370,"treeSorts":"0000000370,","treeLeaf":"1","treeNames":"福建省","parentCodes":"0,","officeCode":"35","viewCode":"35","officeName":"福建省","fullName":"福建省","officeType":"3","leader":"","phone":"","address":"","zipCode":"","email":"","extend":{"extendS6":"","extendS4":"","extendS5":"","extendS3":"","extendS2":"","extendS7":"","extendS1":"","extendS8":""},"isRoot":true,"isTreeLeaf":true,"parentCode":"0"},"company":{"isNewRecord":true,"companyCode":"","companyName":"","companyOfficeList":[],"isRoot":false,"isTreeLeaf":false},"employeePostList":[],"employeePosts":[]}
         * corpCode_ : 0
         * corpName_ : JeeSite
         * avatarUrl : /ctxPath/static/images/user1.jpg
         * oldLoginDate : 2019-04-11 18:00:21
         */

        private boolean isNewRecord;
        private String id;
        private String updateDate;
        private String updateBy;
        private String createBy;
        private String status;
        private String remarks;
        private String createDate;
        private String refCode;
        private String userName;
        private String mobile;
        private ExtendBean extend;
        private String mgrType;
        private String loginCode;
        private String phone;
        private String oldLastLoginIp;
        private String userCode;
        private int userWeight;
        private String userType;
        private String lastLoginIp;
        private String email;
        private String refName;
        private String lastLoginDate;
        private RefObjBean refObj;
        private String corpCode_;
        private String corpName_;
        private String avatarUrl;
        private String oldLoginDate;

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
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

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getRefCode() {
            return refCode;
        }

        public void setRefCode(String refCode) {
            this.refCode = refCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public ExtendBean getExtend() {
            return extend;
        }

        public void setExtend(ExtendBean extend) {
            this.extend = extend;
        }

        public String getMgrType() {
            return mgrType;
        }

        public void setMgrType(String mgrType) {
            this.mgrType = mgrType;
        }

        public String getLoginCode() {
            return loginCode;
        }

        public void setLoginCode(String loginCode) {
            this.loginCode = loginCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOldLastLoginIp() {
            return oldLastLoginIp;
        }

        public void setOldLastLoginIp(String oldLastLoginIp) {
            this.oldLastLoginIp = oldLastLoginIp;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public int getUserWeight() {
            return userWeight;
        }

        public void setUserWeight(int userWeight) {
            this.userWeight = userWeight;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getLastLoginIp() {
            return lastLoginIp;
        }

        public void setLastLoginIp(String lastLoginIp) {
            this.lastLoginIp = lastLoginIp;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRefName() {
            return refName;
        }

        public void setRefName(String refName) {
            this.refName = refName;
        }

        public String getLastLoginDate() {
            return lastLoginDate;
        }

        public void setLastLoginDate(String lastLoginDate) {
            this.lastLoginDate = lastLoginDate;
        }

        public RefObjBean getRefObj() {
            return refObj;
        }

        public void setRefObj(RefObjBean refObj) {
            this.refObj = refObj;
        }

        public String getCorpCode_() {
            return corpCode_;
        }

        public void setCorpCode_(String corpCode_) {
            this.corpCode_ = corpCode_;
        }

        public String getCorpName_() {
            return corpName_;
        }

        public void setCorpName_(String corpName_) {
            this.corpName_ = corpName_;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getOldLoginDate() {
            return oldLoginDate;
        }

        public void setOldLoginDate(String oldLoginDate) {
            this.oldLoginDate = oldLoginDate;
        }

        public static class ExtendBean {
            /**
             * extendS6 :
             * extendS4 :
             * extendS5 :
             * extendS3 :
             * extendS2 :
             * extendS7 :
             * extendS1 :
             * extendS8 :
             */

            private String extendS6;
            private String extendS4;
            private String extendS5;
            private String extendS3;
            private String extendS2;
            private String extendS7;
            private String extendS1;
            private String extendS8;

            public String getExtendS6() {
                return extendS6;
            }

            public void setExtendS6(String extendS6) {
                this.extendS6 = extendS6;
            }

            public String getExtendS4() {
                return extendS4;
            }

            public void setExtendS4(String extendS4) {
                this.extendS4 = extendS4;
            }

            public String getExtendS5() {
                return extendS5;
            }

            public void setExtendS5(String extendS5) {
                this.extendS5 = extendS5;
            }

            public String getExtendS3() {
                return extendS3;
            }

            public void setExtendS3(String extendS3) {
                this.extendS3 = extendS3;
            }

            public String getExtendS2() {
                return extendS2;
            }

            public void setExtendS2(String extendS2) {
                this.extendS2 = extendS2;
            }

            public String getExtendS7() {
                return extendS7;
            }

            public void setExtendS7(String extendS7) {
                this.extendS7 = extendS7;
            }

            public String getExtendS1() {
                return extendS1;
            }

            public void setExtendS1(String extendS1) {
                this.extendS1 = extendS1;
            }

            public String getExtendS8() {
                return extendS8;
            }

            public void setExtendS8(String extendS8) {
                this.extendS8 = extendS8;
            }
        }

        public static class RefObjBean {
            /**
             * isNewRecord : false
             * id : fujian_j2sy
             * updateDate : 2019-03-21 13:44
             * updateBy : system
             * createBy : system
             * status : 0
             * createDate : 2019-03-21 13:44
             * empCode : fujian_j2sy
             * empName : 福建省
             * empNameEn :
             * office : {"isNewRecord":false,"id":"35","updateDate":"2019-03-21 13:42","updateBy":"system","createBy":"system","status":"0","remarks":"","createDate":"2019-03-21 13:42","treeLevel":0,"treeSort":370,"treeSorts":"0000000370,","treeLeaf":"1","treeNames":"福建省","parentCodes":"0,","officeCode":"35","viewCode":"35","officeName":"福建省","fullName":"福建省","officeType":"3","leader":"","phone":"","address":"","zipCode":"","email":"","extend":{"extendS6":"","extendS4":"","extendS5":"","extendS3":"","extendS2":"","extendS7":"","extendS1":"","extendS8":""},"isRoot":true,"isTreeLeaf":true,"parentCode":"0"}
             * company : {"isNewRecord":true,"companyCode":"","companyName":"","companyOfficeList":[],"isRoot":false,"isTreeLeaf":false}
             * employeePostList : []
             * employeePosts : []
             */

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
            private OfficeBean office;
            private CompanyBean company;
            private List<?> employeePostList;
            private List<?> employeePosts;

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
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

            public OfficeBean getOffice() {
                return office;
            }

            public void setOffice(OfficeBean office) {
                this.office = office;
            }

            public CompanyBean getCompany() {
                return company;
            }

            public void setCompany(CompanyBean company) {
                this.company = company;
            }

            public List<?> getEmployeePostList() {
                return employeePostList;
            }

            public void setEmployeePostList(List<?> employeePostList) {
                this.employeePostList = employeePostList;
            }

            public List<?> getEmployeePosts() {
                return employeePosts;
            }

            public void setEmployeePosts(List<?> employeePosts) {
                this.employeePosts = employeePosts;
            }

            public static class OfficeBean {
                /**
                 * isNewRecord : false
                 * id : 35
                 * updateDate : 2019-03-21 13:42
                 * updateBy : system
                 * createBy : system
                 * status : 0
                 * remarks :
                 * createDate : 2019-03-21 13:42
                 * treeLevel : 0
                 * treeSort : 370
                 * treeSorts : 0000000370,
                 * treeLeaf : 1
                 * treeNames : 福建省
                 * parentCodes : 0,
                 * officeCode : 35
                 * viewCode : 35
                 * officeName : 福建省
                 * fullName : 福建省
                 * officeType : 3
                 * leader :
                 * phone :
                 * address :
                 * zipCode :
                 * email :
                 * extend : {"extendS6":"","extendS4":"","extendS5":"","extendS3":"","extendS2":"","extendS7":"","extendS1":"","extendS8":""}
                 * isRoot : true
                 * isTreeLeaf : true
                 * parentCode : 0
                 */

                private boolean isNewRecord;
                private String id;
                private String updateDate;
                private String updateBy;
                private String createBy;
                private String status;
                private String remarks;
                private String createDate;
                private int treeLevel;
                private int treeSort;
                private String treeSorts;
                private String treeLeaf;
                private String treeNames;
                private String parentCodes;
                private String officeCode;
                private String viewCode;
                private String officeName;
                private String fullName;
                private String officeType;
                private String leader;
                private String phone;
                private String address;
                private String zipCode;
                private String email;
                private ExtendBeanX extend;
                private boolean isRoot;
                private boolean isTreeLeaf;
                private String parentCode;

                public boolean isIsNewRecord() {
                    return isNewRecord;
                }

                public void setIsNewRecord(boolean isNewRecord) {
                    this.isNewRecord = isNewRecord;
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

                public String getRemarks() {
                    return remarks;
                }

                public void setRemarks(String remarks) {
                    this.remarks = remarks;
                }

                public String getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(String createDate) {
                    this.createDate = createDate;
                }

                public int getTreeLevel() {
                    return treeLevel;
                }

                public void setTreeLevel(int treeLevel) {
                    this.treeLevel = treeLevel;
                }

                public int getTreeSort() {
                    return treeSort;
                }

                public void setTreeSort(int treeSort) {
                    this.treeSort = treeSort;
                }

                public String getTreeSorts() {
                    return treeSorts;
                }

                public void setTreeSorts(String treeSorts) {
                    this.treeSorts = treeSorts;
                }

                public String getTreeLeaf() {
                    return treeLeaf;
                }

                public void setTreeLeaf(String treeLeaf) {
                    this.treeLeaf = treeLeaf;
                }

                public String getTreeNames() {
                    return treeNames;
                }

                public void setTreeNames(String treeNames) {
                    this.treeNames = treeNames;
                }

                public String getParentCodes() {
                    return parentCodes;
                }

                public void setParentCodes(String parentCodes) {
                    this.parentCodes = parentCodes;
                }

                public String getOfficeCode() {
                    return officeCode;
                }

                public void setOfficeCode(String officeCode) {
                    this.officeCode = officeCode;
                }

                public String getViewCode() {
                    return viewCode;
                }

                public void setViewCode(String viewCode) {
                    this.viewCode = viewCode;
                }

                public String getOfficeName() {
                    return officeName;
                }

                public void setOfficeName(String officeName) {
                    this.officeName = officeName;
                }

                public String getFullName() {
                    return fullName;
                }

                public void setFullName(String fullName) {
                    this.fullName = fullName;
                }

                public String getOfficeType() {
                    return officeType;
                }

                public void setOfficeType(String officeType) {
                    this.officeType = officeType;
                }

                public String getLeader() {
                    return leader;
                }

                public void setLeader(String leader) {
                    this.leader = leader;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getZipCode() {
                    return zipCode;
                }

                public void setZipCode(String zipCode) {
                    this.zipCode = zipCode;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public ExtendBeanX getExtend() {
                    return extend;
                }

                public void setExtend(ExtendBeanX extend) {
                    this.extend = extend;
                }

                public boolean isIsRoot() {
                    return isRoot;
                }

                public void setIsRoot(boolean isRoot) {
                    this.isRoot = isRoot;
                }

                public boolean isIsTreeLeaf() {
                    return isTreeLeaf;
                }

                public void setIsTreeLeaf(boolean isTreeLeaf) {
                    this.isTreeLeaf = isTreeLeaf;
                }

                public String getParentCode() {
                    return parentCode;
                }

                public void setParentCode(String parentCode) {
                    this.parentCode = parentCode;
                }

                public static class ExtendBeanX {
                    /**
                     * extendS6 :
                     * extendS4 :
                     * extendS5 :
                     * extendS3 :
                     * extendS2 :
                     * extendS7 :
                     * extendS1 :
                     * extendS8 :
                     */

                    private String extendS6;
                    private String extendS4;
                    private String extendS5;
                    private String extendS3;
                    private String extendS2;
                    private String extendS7;
                    private String extendS1;
                    private String extendS8;

                    public String getExtendS6() {
                        return extendS6;
                    }

                    public void setExtendS6(String extendS6) {
                        this.extendS6 = extendS6;
                    }

                    public String getExtendS4() {
                        return extendS4;
                    }

                    public void setExtendS4(String extendS4) {
                        this.extendS4 = extendS4;
                    }

                    public String getExtendS5() {
                        return extendS5;
                    }

                    public void setExtendS5(String extendS5) {
                        this.extendS5 = extendS5;
                    }

                    public String getExtendS3() {
                        return extendS3;
                    }

                    public void setExtendS3(String extendS3) {
                        this.extendS3 = extendS3;
                    }

                    public String getExtendS2() {
                        return extendS2;
                    }

                    public void setExtendS2(String extendS2) {
                        this.extendS2 = extendS2;
                    }

                    public String getExtendS7() {
                        return extendS7;
                    }

                    public void setExtendS7(String extendS7) {
                        this.extendS7 = extendS7;
                    }

                    public String getExtendS1() {
                        return extendS1;
                    }

                    public void setExtendS1(String extendS1) {
                        this.extendS1 = extendS1;
                    }

                    public String getExtendS8() {
                        return extendS8;
                    }

                    public void setExtendS8(String extendS8) {
                        this.extendS8 = extendS8;
                    }
                }
            }

            public static class CompanyBean {
                /**
                 * isNewRecord : true
                 * companyCode :
                 * companyName :
                 * companyOfficeList : []
                 * isRoot : false
                 * isTreeLeaf : false
                 */

                private boolean isNewRecord;
                private String companyCode;
                private String companyName;
                private boolean isRoot;
                private boolean isTreeLeaf;
                private List<?> companyOfficeList;

                public boolean isIsNewRecord() {
                    return isNewRecord;
                }

                public void setIsNewRecord(boolean isNewRecord) {
                    this.isNewRecord = isNewRecord;
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

                public boolean isIsRoot() {
                    return isRoot;
                }

                public void setIsRoot(boolean isRoot) {
                    this.isRoot = isRoot;
                }

                public boolean isIsTreeLeaf() {
                    return isTreeLeaf;
                }

                public void setIsTreeLeaf(boolean isTreeLeaf) {
                    this.isTreeLeaf = isTreeLeaf;
                }

                public List<?> getCompanyOfficeList() {
                    return companyOfficeList;
                }

                public void setCompanyOfficeList(List<?> companyOfficeList) {
                    this.companyOfficeList = companyOfficeList;
                }
            }
        }
    }
}
