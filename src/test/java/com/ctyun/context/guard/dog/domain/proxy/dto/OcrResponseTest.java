package com.ddd.example.domain.proxy.dto;


import com.ddd.example.infrastructure.utils.GsonUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OcrResponseTest {

    @Test
    public void testSuccessCovert() {
        String json = "{\"code\":0,\"message\":\"success\",\"result\":{\"TextCount\":5,\"TextBoxes\":[[[69,0],[182,0],[182,60],[69,60]],[[809,4],[1043,4],[1043,67],[809,67]],[[58,1148],[1025,1079],[1034,1200],[67,1270]],[[62,1299],[1004,1231],[1011,1308],[69,1375]],[[274,1393],[791,1355],[795,1416],[278,1454]]],\"TextScores\":[0.5569,0.5662,0.6688,0.4694,0.4998],\"Texts\":[\"荣耀\",\"HONOR\",\"智享生活中国荣耀\",\"直播抽送荣耀Magic3孙一文签名盲盒\",\"11月11日晚19点-20点\"]}}";
        OcrResponse response = GsonUtil.fromJsonString(json, OcrResponse.class);


        // Assertions using AssertJ
        assertThat(response.getXRequestId()).isNull();
        assertThat(response.getMessage()).isEqualTo("success");
        assertThat(response.getCode()).isEqualTo(0);
        assertThat(response.getDetails()).isNull();

        List<OcrResponse.Result> result = response.getResultList();
        assertThat(result).isNotNull();
    }

    @Test
    public void testSuccessV2Covert() {
//        String json = "{\"code\":0,\"result\":[{\"TextCount\":12,\"TextBoxes\":[[[73,1],[174,1],[174,53],[73,53]],[[813,10],[1035,10],[1035,58],[813,58]],[[75,47],[174,47],[174,100],[75,100]],[[735,254],[879,256],[873,572],[729,571]],[[297,292],[396,298],[387,457],[288,451]],[[939,353],[985,353],[982,551],[936,551]],[[874,386],[925,388],[922,512],[871,511]],[[267,428],[325,440],[283,632],[225,619]],[[552,1115],[1021,1076],[1032,1203],[562,1242]],[[48,1151],[528,1115],[538,1247],[58,1283]],[[63,1308],[1003,1241],[1008,1307],[67,1374]],[[285,1394],[789,1364],[792,1413],[288,1438]]],\"Texts\":[\"荣耀\",\"HONOR\",\"11.11\",\"\",\"一\",\"击剑奥运冠军\",\"孙一文\",\"\",\"中国荣耀\",\"智享生活\",\"直播抽送荣耀Magic-3-孙一文签名盲盒\",\"11月11日晚19点-20点\"]}],\"message\":\"success\"}";
        String json = "{\"code\":0,\"result\":[{\"TextCount\":12,\"TextBoxes\":[[[73,1],[174,1],[174,53],[73,53]],[[813,10],[1035,10],[1035,58],[813,58]],[[75,47],[174,47],[174,100],[75,100]],[[735,254],[879,256],[873,572],[729,571]],[[297,292],[396,298],[387,457],[288,451]],[[939,353],[985,353],[982,551],[936,551]],[[874,386],[925,388],[922,512],[871,511]],[[267,428],[325,440],[283,632],[225,619]],[[552,1115],[1021,1076],[1032,1203],[562,1242]],[[48,1151],[528,1115],[538,1247],[58,1283]],[[63,1308],[1003,1241],[1008,1307],[67,1374]],[[285,1394],[789,1364],[792,1413],[288,1438]]],\"Texts\":[\"荣耀\",\"HONOR\",\"11.11\",\"\",\"一\",\"击剑奥运冠军\",\"孙一文\",\"\",\"中国荣耀\",\"智享生活\",\"直播抽送荣耀Magic-3-孙一文签名盲盒\",\"11月11日晚19点-20点\"]}],\"message\":\"success\"}";
        OcrResponse response = GsonUtil.fromJsonString(json, OcrResponse.class);


        // Assertions using AssertJ
        assertThat(response.getXRequestId()).isNull();
        assertThat(response.getMessage()).isEqualTo("success");
        assertThat(response.getCode()).isEqualTo(0);
        assertThat(response.getDetails()).isNull();

        List<OcrResponse.Result> resultList = response.getResultList();
        System.out.println(GsonUtil.toJsonString(resultList));

    }

    @Test
    public void testFailCovert() {
        String json = "{\"code\":500001,\"message\":\"服务接口异常，请联系管理员\",\"details\":\"需要联系管理员处理\"}";
        OcrResponse response = GsonUtil.fromJsonString(json, OcrResponse.class);


        // Assertions using AssertJ
        assertThat(response.getXRequestId()).isNull();
        assertThat(response.getMessage()).isNotEmpty();
        assertThat(response.getCode()).isEqualTo(500001);
        assertThat(response.getDetails()).isNotEmpty();
    }

}