package com.ddd.example.adapter.controller;


import com.ddd.example.infrastructure.valueobject.BizErrorCodeEnum;
import org.junit.Assert;
import org.junit.Test;

public class ResponseVOTest {

    @Test
    public void testSuccessResponseWithData() {
        String expectedMsg = BizErrorCodeEnum.SUCCESS.getDescription();
        Integer expectedCode = Integer.valueOf(BizErrorCodeEnum.SUCCESS.getCode());
        ResponseVO<String> response = ResponseVO.successResponse("testData");
        Assert.assertEquals(expectedCode, response.getCode());
        Assert.assertEquals(expectedMsg, response.getMsg());
        Assert.assertEquals("testData", response.getData());
    }

    @Test
    public void testSuccessEmptyResponse() {
        String expectedMsg = BizErrorCodeEnum.SUCCESS.getDescription();
        Integer expectedCode = Integer.valueOf(BizErrorCodeEnum.SUCCESS.getCode());
        ResponseVO<Void> response = ResponseVO.successEmptyResponse();
        Assert.assertEquals(expectedCode, response.getCode());
        Assert.assertEquals(expectedMsg, response.getMsg());
        Assert.assertNull(response.getData());
    }

    @Test
    public void testFailureResponse() {
        BizErrorCodeEnum errorEnum = BizErrorCodeEnum.PARAM_VALIDATION_ERROR;
        String expectedMsg = errorEnum.getDescription();
        Integer expectedCode = Integer.valueOf(errorEnum.getCode());
        ResponseVO<Void> response = ResponseVO.failure(errorEnum);
        Assert.assertEquals(expectedCode, response.getCode());
        Assert.assertEquals(expectedMsg, response.getMsg());
        Assert.assertNull(response.getData());
    }

    @Test
    public void testFailureDetailMessage() {
        BizErrorCodeEnum errorEnum = BizErrorCodeEnum.OTHER_NETWORK_ERROR;
        String detailMessage = "Detailed error message";
        String expectedMsg = detailMessage; // Assuming the detail message overrides the error description
        Integer expectedCode = Integer.valueOf(errorEnum.getCode());
        ResponseVO<Void> response = ResponseVO.failureDetailMessage(errorEnum, detailMessage);
        Assert.assertEquals(expectedCode, response.getCode());
        Assert.assertEquals(expectedMsg, response.getMsg());
        Assert.assertNull(response.getData());
    }

    @Test
    public void testFailureData() {
        BizErrorCodeEnum errorEnum = BizErrorCodeEnum.NETWORK_ERROR;
        String expectedMsg = errorEnum.getDescription();
        Integer expectedCode = Integer.valueOf(errorEnum.getCode());
        String testData = "failureData";
        ResponseVO<String> response = ResponseVO.failureData(errorEnum, testData);
        Assert.assertEquals(expectedCode, response.getCode());
        Assert.assertEquals(expectedMsg, response.getMsg());
        Assert.assertEquals(testData, response.getData());
    }
}