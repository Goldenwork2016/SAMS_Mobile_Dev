package com.sherdle.universal.model;

import android.graphics.Color;

public class AwardInformation {
    private String id, studentId, name,lastName,sport,level,status,school,fall,winters,spring,summer,year,student_type,admit_type,class_level,award_active_status;
    private String school_id,sport_id,user_type;

    // this constructor for the information . . . ;
    public AwardInformation(String id,String studentId, String name, String lastName,String sportId, String sport,
                            String level, String status,String schoolId, String school, String fall, String
                                    winters, String spring, String summer, String year,String
                                    student_type,String admit_type,String class_level,String user_type,String awardActive_status) {
        this.studentId = studentId;
        this.name = name;
        this.lastName = lastName;
        this.sport_id=sportId;
        this.sport = sport;
        this.level = level;
        this.status = status;
        this.school_id=schoolId;
        this.school = school;
        this.fall = fall;
        this.winters = winters;
        this.spring = spring;
        this.summer = summer;
        this.year = year;
        this.student_type=student_type;
        this.admit_type=admit_type;
        this.class_level=class_level;
        this.id=id;
        this.user_type=user_type;
        this.award_active_status = awardActive_status;

    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }


    public String getLastName() {
        return lastName;
    }

    public String getSport() {
        return sport;
    }

    public String getLevel() {
        return level;
    }

    public String getStatus() {
        return status;
    }

    public String getSchool() {
        return school;
    }

    public String getFall() {
        return fall;
    }

    public String getWinters() {
        return winters;
    }

    public String getSpring() {
        return spring;
    }

    public String getSummer() {
        return summer;
    }

    public String getYear() {
        return year;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setFall(String fall) {
        this.fall = fall;
    }

    public void setWinters(String winters) {
        this.winters = winters;
    }

    public void setSpring(String spring) {
        this.spring = spring;
    }

    public void setSummer(String summer) {
        this.summer = summer;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStudent_type() {
        return student_type;
    }

    public void setStudent_type(String student_type) {
        this.student_type = student_type;
    }

    public String getAdmit_type() {
        return admit_type;
    }

    public void setAdmit_type(String admit_type) {
        this.admit_type = admit_type;
    }

    public String getClass_level() {
        return class_level;
    }

    public void setClass_level(String class_level) {
        this.class_level = class_level;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getAward_active_status() {
        return award_active_status;
    }

    public void setAward_active_status(String award_active_status) {
        this.award_active_status = award_active_status;
    }
}
