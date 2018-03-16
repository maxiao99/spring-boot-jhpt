/*
 * Copyright (C) 2016 CK, Inc. All Rights Reserved.
 */

package com.tyj.jhpt.web;

import com.github.fartherp.framework.common.util.CsvUtil;
import com.github.fartherp.framework.common.util.DateUtil;
import com.github.fartherp.framework.core.util.JsonResp;
import com.github.fartherp.framework.core.validate.ValidUtils;
import com.tyj.jhpt.bo.Alarm;
import com.tyj.jhpt.bo.AllCar;
import com.tyj.jhpt.bo.DeviceGpsInfo;
import com.tyj.jhpt.bo.DeviceInfo;
import com.tyj.jhpt.bo.Dianya;
import com.tyj.jhpt.bo.DianyaDetail;
import com.tyj.jhpt.bo.Fadongji;
import com.tyj.jhpt.bo.QudongDianji;
import com.tyj.jhpt.bo.QudongDianjiDetail;
import com.tyj.jhpt.bo.RanliaoDianchi;
import com.tyj.jhpt.bo.Supers;
import com.tyj.jhpt.bo.Wendu;
import com.tyj.jhpt.bo.WenduDetail;
import com.tyj.jhpt.server.command.platform.PlatformThreeCommand;
import com.tyj.jhpt.server.command.platform.PlatformTwoCommand;
import com.tyj.jhpt.server.util.DeviceMsgUtils;
import com.tyj.jhpt.service.AlarmService;
import com.tyj.jhpt.service.AllCarService;
import com.tyj.jhpt.service.DeviceGpsInfoService;
import com.tyj.jhpt.service.CompositeDictionaryService;
import com.tyj.jhpt.service.DianyaDetailService;
import com.tyj.jhpt.service.DianyaService;
import com.tyj.jhpt.service.FadongjiService;
import com.tyj.jhpt.service.DeviceInfoService;
import com.tyj.jhpt.service.IShortMsgSender;
import com.tyj.jhpt.service.QudongDianjiDetailService;
import com.tyj.jhpt.service.QudongDianjiService;
import com.tyj.jhpt.service.RanliaoDianchiService;
import com.tyj.jhpt.service.SupersService;
import com.tyj.jhpt.service.WenduDetailService;
import com.tyj.jhpt.service.WenduService;
import com.tyj.jhpt.validate.EditGroup;
import com.tyj.jhpt.vo.DeviceInfoPageVo;
import com.tyj.jhpt.vo.DeviceInfoVo;
import com.tyj.jhpt.vo.Merges;
import com.tyj.jhpt.vo.MsgPageVo;
import com.tyj.jhpt.vo.RealTimePageVo;
import com.tyj.jhpt.vo.SettingConfigVo;
import com.tyj.jhpt.vo.TerminalConfigVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2016/5/1
 */
@Controller
@RequestMapping(value = "device_info")
public class DeviceInfoController extends AbstractController {
    @Resource(name = "deviceInfoService")
    DeviceInfoService deviceInfoService;

    @Resource(name = "deviceGpsInfoService")
    DeviceGpsInfoService deviceGpsInfoService;

    @Resource(name = "compositeDictionaryService")
    CompositeDictionaryService compositeDictionaryService;

    @Resource(name = "allCarService")
    AllCarService allCarService;

    @Resource(name = "qudongDianjiService")
    QudongDianjiService qudongDianjiService;

    @Resource(name = "qudongDianjiDetailService")
    QudongDianjiDetailService qudongDianjiDetailService;

    @Resource(name = "ranliaoDianchiService")
    RanliaoDianchiService ranliaoDianchiService;

    @Resource(name = "fadongjiService")
    FadongjiService fadongjiService;

    @Resource(name = "supersService")
    SupersService supersService;

    @Resource(name = "alarmService")
    AlarmService alarmService;

    @Resource(name = "dianyaService")
    DianyaService dianyaService;

    @Resource(name = "dianyaDetailService")
    DianyaDetailService dianyaDetailService;

    @Resource(name = "wenduService")
    WenduService wenduService;

    @Resource(name = "wenduDetailService")
    WenduDetailService wenduDetailService;

    @Resource(name = "platformTwoCommand")
    PlatformTwoCommand platformTwoCommand;

    @Resource(name = "platformThreeCommand")
    PlatformThreeCommand platformThreeCommand;

    @Resource(name = "yunpianShortMessageSender")
    IShortMsgSender shortMessageSender;

    @ResponseBody
    @RequestMapping(value = "/page/list", method = {RequestMethod.GET}, produces = {"application/json"})
    @ApiOperation(value = "用户列表分页", notes = "入参格式：keyword=1&currentPage=1&limit=10")
    @ApiImplicitParam(name = "vo", value = "用户分页vo", required = true, dataType = "DeviceInfoPageVo")
    public String list(DeviceInfoPageVo vo) {
        List<DeviceInfo> l = deviceInfoService.findPageDeviceInfo(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/add_device_info", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "添加设备", notes = "入参格式：{\"iccid\":\"13203\",\"terminalSeq\":\"123456\",\"carVin\":\"LRDS6PEB3HR002350\",\"terminalNo\":\"123456\",\"name\":\"张三\",\"age\":40,\"drivingLicense\":1,\"identityNo\":\"361083197409156638\",\"telephone\":\"18611715695\",\"plateNo\":\"粤B12350\",\"carType\":1}")
    @ApiImplicitParam(name = "vo", value = "设备vo", required = true, dataType = "DeviceInfoVo")
    public String add(@Valid @RequestBody DeviceInfoVo vo, BindingResult error) {
        ValidUtils.valid(vo, error);

        DeviceInfo deviceInfo = deviceInfoService.findByIdentityNo(vo.getIdentityNo());
        if (deviceInfo != null) {
            return JsonResp.asData().error("身份证号已存在,请重新添加").toJson();
        }
        deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(vo, deviceInfo);
        deviceInfo.setCreateTime(new Date());
        deviceInfoService.saveEntitySelective(deviceInfo);
        return JsonResp.asData().success().toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/edit_device_info", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "编辑设备", notes = "入参格式：{\"id\":1,\"iccid\":\"13203\",\"terminalSeq\":\"123456\",\"carVin\":\"LRDS6PEB3HR002350\",\"terminalNo\":\"123456\",\"name\":\"张三\",\"age\":40,\"drivingLicense\":1,\"identityNo\":\"361083197409156638\",\"telephone\":\"18611715695\",\"plateNo\":\"粤B12350\",\"carType\":1}")
    @ApiImplicitParam(name = "vo", value = "设备vo", required = true, dataType = "DeviceInfoVo")
    public String edit(@Valid @RequestBody DeviceInfoVo vo, BindingResult error) {
        ValidUtils.valid(vo, error, EditGroup.class);

        DeviceInfo deviceInfo = deviceInfoService.findByIdentityNo(vo.getIdentityNo());
        if (deviceInfo != null && !deviceInfo.getId().equals(vo.getId())) {
            return JsonResp.asData().error("身份证号已存在,请重新编辑").toJson();
        }
        deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(vo, deviceInfo);
        deviceInfoService.updateEntitySelective(deviceInfo);
        return JsonResp.asData().success().toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/del_device_info", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "删除设备", notes = "入参格式：id=1")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long")
    public String delUser(@RequestParam Long id) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        deviceInfoService.delete(deviceInfo);
        return JsonResp.asEmpty().toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/get_device_info", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "查看设备", notes = "入参格式：id:1")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long")
    public String getUser(@RequestParam Long id) {
        DeviceInfo deviceInfo = deviceInfoService.findById(id);
        return JsonResp.asData(deviceInfo).toJson();
    }

    /**
     * 激活设备
     * @param id 设备id
     * @param testTime 测试时间
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/activate")
    public String activate(@RequestParam("carVin") Long id,
                           @RequestParam(value = "testTime", required = false) String testTime) {
        DeviceInfo dev = deviceInfoService.findById(id);
        if (dev != null) {
            return JsonResp.asData().success().toJson();
        }
        return JsonResp.asData().error("激活短信发送失败").toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/page/msg_list", method = {RequestMethod.GET}, produces = {"application/json"})
    @ApiOperation(value = "告警信息分页", notes = "入参格式：plateNo=1&startDate=2018-10-10 10:10:10&endDate=2018-10-10 20:10:10&currentPage=1&limit=10")
    @ApiImplicitParam(name = "vo", value = "告警信息分页vo", required = true, dataType = "UserPageVo")
    public String msgList(MsgPageVo vo) {
        List<DeviceGpsInfo> l = deviceGpsInfoService.findPageMsg(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 下载告警信息
     * @param response 响应
     * @param request 请求
     * @param p 参数
     */
    @ResponseBody
    @RequestMapping(value = "/download_msg")
    public void downloadAlarmTeam(HttpServletResponse response, HttpServletRequest request,
                                  @RequestParam Map<String, Object> p) {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("dicType", 6);
        Map<Integer, String> msgType = compositeDictionaryService.findMapByParams(map);
        String[] title = new String [] {"设备号", "消息ID", "采集时间", "上传时间", "经度",
                "纬度", "车速", "特征值", "告警类型", "驾驶员ID", "驾驶员ID"};
        List<DeviceGpsInfo> ul = deviceGpsInfoService.findMsg(p);
        List<String[]> list = new ArrayList<String[]>(ul.size());
        for (DeviceGpsInfo d : ul) {
            String [] ss = new String [title.length];
            ss[0] = d.getCarVin();
            ss[1] = String.valueOf(d.getMsgId());
            ss[2] = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss,  d.getEventTime());
            ss[3] = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, d.getUploadTime());
            ss[4] = String.valueOf(d.getLongitude());
            ss[5] = String.valueOf(d.getLatitude());
            ss[6] = String.valueOf(d.getSpeed());
            ss[7] = "0";
            ss[8] = "";
            ss[9] = String.valueOf(d.getDriverPersonId());
            list.add(ss);
        }
        CsvUtil.writeCsvFile(response, request, "设备数据", title, list);
    }

    /**
     * 整车数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/all_car_list")
    public String findPageAllCar(RealTimePageVo<AllCar> vo) {
        List<AllCar> l = allCarService.findPageAllCar(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 驱动电机数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/qudong_dianji_list")
    public String findPageQudongDianji(RealTimePageVo<QudongDianji> vo) {
        List<QudongDianji> list = new ArrayList<QudongDianji>();
        List<QudongDianji> l = qudongDianjiService.findPageQudongDianji(vo.convertPageMap());
        if (CollectionUtils.isNotEmpty(l)) {
            List<Long> longs = new ArrayList<Long>();
            for (QudongDianji o : l) {
                longs.add(o.getId());
            }
            List<QudongDianjiDetail> details = qudongDianjiDetailService.findByIds(longs);
            Long front = -1L;
            List<Merges> merges = new ArrayList<Merges>();
            for (int i = 0; i < details.size(); i++) {
                QudongDianjiDetail detail = details.get(i);
                QudongDianji dto = new QudongDianji();
                BeanUtils.copyProperties(detail, dto, "id");
                for (QudongDianji bo : l) {
                    if (bo.getId().equals(detail.getQudongDianjiId())) {
                        dto.setId(bo.getId());
                        dto.setCarVin(bo.getCarVin());
                        dto.setEventTime(bo.getEventTime());
                        dto.setPlateNo(bo.getPlateNo());
                        break;
                    }
                }
                list.add(dto);
                Long current = detail.getQudongDianjiId();
                if (!front.equals(current)) {
                    Merges merge = new Merges();
                    merge.setIndex(i);
                    merge.setId(current);
                    merges.add(merge);
                }
                front = current;
                for (Merges merge : merges) {
                    if (merge.getId().equals(current)) {
                        merge.increaseByLong();
                        break;
                    }
                }
            }
            vo.setMerges(merges);
        }
        vo.setRows(list);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 燃料电池数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/ranliao_dianchi_list")
    public String findPageRanliaoDianchi(RealTimePageVo<RanliaoDianchi> vo) {
        List<RanliaoDianchi> l = ranliaoDianchiService.findPageRanliaoDianchi(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 发动机数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/fadongji_list")
    public String findPageFadongji(RealTimePageVo<Fadongji> vo) {
        List<Fadongji> l = fadongjiService.findPageFadongji(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 极值数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/supers_list")
    public String findPageSupers(RealTimePageVo<Supers> vo) {
        List<Supers> l = supersService.findPageSupers(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 报警数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/alarm_list")
    public String findPageAlarm(RealTimePageVo<Alarm> vo) {
        List<Alarm> l = alarmService.findPageAlarm(vo.convertPageMap());
        vo.setRows(l);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 可充电储能装置电压数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/dianya_list")
    public String findPageDianya(RealTimePageVo<Dianya> vo) {
        List<Dianya> list = new ArrayList<Dianya>();
        List<Dianya> l = dianyaService.findPageDianya(vo.convertPageMap());
        if (CollectionUtils.isNotEmpty(l)) {
            List<Long> longs = new ArrayList<Long>();
            for (Dianya o : l) {
                longs.add(o.getId());
            }
            List<DianyaDetail> details = dianyaDetailService.findByIds(longs);
            Long front = -1L;
            List<Merges> merges = new ArrayList<Merges>();
            for (int i = 0; i < details.size(); i++) {
                DianyaDetail detail = details.get(i);
                Dianya dto = new Dianya();
                BeanUtils.copyProperties(detail, dto, "id");
                for (Dianya bo : l) {
                    if (bo.getId().equals(detail.getDianyaId())) {
                        dto.setId(bo.getId());
                        dto.setCarVin(bo.getCarVin());
                        dto.setEventTime(bo.getEventTime());
                        dto.setPlateNo(bo.getPlateNo());
                        break;
                    }
                }
                list.add(dto);
                Long current = detail.getDianyaId();
                if (!front.equals(current)) {
                    Merges merge = new Merges();
                    merge.setIndex(i);
                    merge.setId(current);
                    merges.add(merge);
                }
                front = current;
                for (Merges merge : merges) {
                    if (merge.getId().equals(current)) {
                        merge.increaseByLong();
                        break;
                    }
                }
            }
            vo.setMerges(merges);
        }
        vo.setRows(list);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 可充电储能装置温度数据列表
     */
    @ResponseBody
    @RequestMapping(value = "/page/wendu_list")
    public String findPageWendu(RealTimePageVo<Wendu> vo) {
        List<Wendu> list = new ArrayList<Wendu>();
        List<Wendu> l = wenduService.findPageWendu(vo.convertPageMap());
        if (CollectionUtils.isNotEmpty(l)) {
            List<Long> longs = new ArrayList<Long>();
            for (Wendu o : l) {
                longs.add(o.getId());
            }
            List<WenduDetail> details = wenduDetailService.findByIds(longs);
            Long front = -1L;
            List<Merges> merges = new ArrayList<Merges>();
            for (int i = 0; i < details.size(); i++) {
                WenduDetail detail = details.get(i);
                Wendu dto = new Wendu();
                BeanUtils.copyProperties(detail, dto, "id");
                for (Wendu bo : l) {
                    if (bo.getId().equals(detail.getWenduId())) {
                        dto.setId(bo.getId());
                        dto.setCarVin(bo.getCarVin());
                        dto.setEventTime(bo.getEventTime());
                        dto.setPlateNo(bo.getPlateNo());
                        break;
                    }
                }
                list.add(dto);
                Long current = detail.getWenduId();
                if (!front.equals(current)) {
                    Merges merge = new Merges();
                    merge.setIndex(i);
                    merge.setId(current);
                    merges.add(merge);
                }
                front = current;
                for (Merges merge : merges) {
                    if (merge.getId().equals(current)) {
                        merge.increaseByLong();
                        break;
                    }
                }
            }
            vo.setMerges(merges);
        }
        vo.setRows(list);
        return JsonResp.asData(vo).setDatePattern(DateUtil.yyyy_MM_dd_HH_mm_ss).toJson();
    }

    /**
     * 平台参数查询
     */
    @ResponseBody
    @RequestMapping(value = "/param_query")
    public String paramQuery(SettingConfigVo vo) {
        String [] ds = vo.getIds().split(",");
        List<Long> list = new ArrayList<Long>();
        for (String d : ds) {
            list.add(Long.valueOf(d));
        }
        List<String> errorCarVins;
        if (CollectionUtils.isNotEmpty(list)) {
            errorCarVins = new ArrayList<String>();
            List<DeviceInfo> deviceInfos = deviceInfoService.findByIds(list);
            for (DeviceInfo deviceInfo : deviceInfos) {
                String vin = deviceInfo.getCarVin();
                String s = DeviceMsgUtils.param(vin, "80", vo);
                String phone = deviceInfo.getTelephone();
                int send = shortMessageSender.send(phone, s);
                if (send != 0) {
                    errorCarVins.add(vin);
                }
            }
            return JsonResp.asData().error("激活短信发送失败vin: " + errorCarVins.toString()).toJson();
        }
        return JsonResp.asData().error("没有选择发送的设备").toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/setting_config", method = {RequestMethod.POST}, produces = {"application/json"})
    @ApiOperation(value = "平台参数设置指令", notes = "入参格式：{\"id\":1,\"userName\":\"admin\",\"realName\":\"admin\",\"password\":\"admin\",\"confirmPassword\":\"admin\",\"authId\":1}")
    @ApiImplicitParam(name = "vo", value = "平台参数设置vo", required = true, dataType = "SettingConfigVo")
    public String settingConfig(@RequestBody SettingConfigVo vo) {
        String [] ds = vo.getIds().split(",");
        List<Long> list = new ArrayList<Long>();
        for (String d : ds) {
            list.add(Long.valueOf(d));
        }
        List<String> errorCarVins;
        if (CollectionUtils.isNotEmpty(list)) {
            errorCarVins = new ArrayList<String>();
            List<DeviceInfo> deviceInfos = deviceInfoService.findByIds(list);
            for (DeviceInfo deviceInfo : deviceInfos) {
                String vin = deviceInfo.getCarVin();
                String s = DeviceMsgUtils.param(vin, "81", vo);
                String phone = deviceInfo.getTelephone();
                int send = shortMessageSender.send(phone, s);
                if (send != 0) {
                    errorCarVins.add(vin);
                }
            }
            return JsonResp.asData().error("激活短信发送失败vin: " + errorCarVins.toString()).toJson();
        }
        return JsonResp.asData().error("没有选择发送的设备").toJson();
    }

    /**
     * 平台车载终端控制指令
     */
    @ResponseBody
    @RequestMapping(value = "/terminal_config")
    public String terminalConfig(TerminalConfigVo vo) {
        String [] ds = vo.getIds().split(",");
        List<Long> list = new ArrayList<Long>();
        for (String d : ds) {
            list.add(Long.valueOf(d));
        }
        List<String> errorCarVins;
        if (CollectionUtils.isNotEmpty(list)) {
            errorCarVins = new ArrayList<String>();
            List<DeviceInfo> deviceInfos = deviceInfoService.findByIds(list);
            for (DeviceInfo deviceInfo : deviceInfos) {
                String vin = deviceInfo.getCarVin();
                String s = DeviceMsgUtils.control(vin, vo);
                String phone = deviceInfo.getTelephone();
                int send = shortMessageSender.send(phone, s);
                if (send != 0) {
                    errorCarVins.add(vin);
                }
            }
            return JsonResp.asData().error("激活短信发送失败vin: " + errorCarVins.toString()).toJson();
        }
        return JsonResp.asData().error("没有选择发送的设备").toJson();
    }
}
