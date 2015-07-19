package com.yzc.lovehuali.bmob;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2015/1/23 0023.
 */
public class StudentUser extends BmobUser {
    private String nickname;
    private String studentId;
    private String eduSystemPw;
    private String stuClass;
    private String department,grade,major;
    private Boolean isBindingEduSystem;
    private Boolean isMale;
    private String stuCourse;

    public String getNickname() {
        return nickname;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getEduSystemPw() {
        return eduSystemPw;
    }

    public Boolean getIsMale() {
        return isMale;
    }

    public String getStuClass() {
        return stuClass;
    }

    public String getDepartment() {
        return department;
    }

    public String getGrade() {
        return grade;
    }

    public String getMajor() {
        return major;
    }

    public Boolean getIsBindingEduSystem() {
        return isBindingEduSystem;
    }

    public String getStuCourse() {
        return stuCourse;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setEduSystemPw(String eduSystemPw) {
        this.eduSystemPw = eduSystemPw;
    }

    public void setIsMale(Boolean isMale) {
        this.isMale = isMale;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setStuCourse(String stuCourse) {
        this.stuCourse = stuCourse;
    }

    public void setIsBindingEduSystem(Boolean isBindingEduSystem) {
        this.isBindingEduSystem = isBindingEduSystem;
    }
}
