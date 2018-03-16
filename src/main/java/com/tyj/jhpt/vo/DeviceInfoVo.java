/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.vo;

import com.tyj.jhpt.validate.EditGroup;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/3/15
 */
public class DeviceInfoVo {
    @NotNull(message = "用户id不能为空", groups = {EditGroup.class})
    @Range(min = 1, max = Integer.MAX_VALUE, message = "用户id必须大于0", groups = {EditGroup.class})
    private Long id;

    @NotEmpty(message = "SIM卡ICCID号不能为空")
    private String iccid;

    @NotEmpty(message = "终端序列号不能为空")
    private String terminalSeq;

    @NotEmpty(message = "VIN不能为空")
    private String carVin;

    @NotEmpty(message = "车载终端编号不能为空")
    private String terminalNo;
    private String dianjiNo;
    private String fadongjiNo;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Range(min = 0, max = 150, message = "年龄不正确")
    private Integer age;

    @NotNull(message = "驾照类型不能为空")
    private Integer drivingLicense;

    @NotEmpty(message = "身份证号不能为空")
    @Pattern(regexp = "^\\d{18}(\\d{2}[A-Za-z0-9])?$", message = "身份证号码不正确")
    private String identityNo;

    @NotEmpty(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须11位")
    @Pattern(regexp = "^(13|15|18)\\d{9}$", message = "手机号码不正确")
    private String telephone;

    @NotEmpty(message = "车牌号不能为空")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5][\\da-zA-Z]{6}$", message = "车牌号码无效（例：粤B12350）")
    private String plateNo;

    @NotNull(message = "车型不能为空")
    private Integer carType;

    private String weixinAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getTerminalSeq() {
        return terminalSeq;
    }

    public void setTerminalSeq(String terminalSeq) {
        this.terminalSeq = terminalSeq;
    }

    public String getCarVin() {
        return carVin;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getDianjiNo() {
        return dianjiNo;
    }

    public void setDianjiNo(String dianjiNo) {
        this.dianjiNo = dianjiNo;
    }

    public String getFadongjiNo() {
        return fadongjiNo;
    }

    public void setFadongjiNo(String fadongjiNo) {
        this.fadongjiNo = fadongjiNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(Integer drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getWeixinAccount() {
        return weixinAccount;
    }

    public void setWeixinAccount(String weixinAccount) {
        this.weixinAccount = weixinAccount;
    }
}
