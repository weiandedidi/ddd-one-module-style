package com.ddd.example.domain.proxy;

import com.ddd.example.domain.proxy.dto.OcrRequest;
import com.ddd.example.domain.proxy.dto.OcrResponse;
import com.ddd.example.infrastructure.proxyimpl.ImageRecognizeProxyImpl;
import com.ddd.example.infrastructure.utils.GsonUtil;
import com.ddd.example.infrastructure.utils.HttpClientUtil;
import com.ddd.example.infrastructure.utils.httpdto.JsonStringHttpResponse;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

@RunWith(MockitoJUnitRunner.class) // 使用 MockitoJUnitRunner
public class ImageRecognizeProxyImplTest {

    @InjectMocks
    private ImageRecognizeProxyImpl imageRecognizeProxy;

    private final String ocrUrl = "http://mock-url.com";

    public static final List<String> imageUrlList = Lists.newArrayList("http://mock-url.com.imag");

    @Before
    public void setUp() {
        // 使用反射来设置私有字段
        try {
            Field ocrUrlField = ImageRecognizeProxyImpl.class.getDeclaredField("ocrUrl");
            ocrUrlField.setAccessible(true);
            ocrUrlField.set(imageRecognizeProxy, ocrUrl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOcrResultSuccess() {
        // Arrange
        OcrRequest ocrRequest = new OcrRequest();
        //全字段set
        ocrRequest.setImageURL(imageUrlList);
        String requestBody = GsonUtil.toJsonString(ocrRequest);

        String responseBody = "{\"mock\": \"response\"}";
        JsonStringHttpResponse mockResponse = new JsonStringHttpResponse();
        mockResponse.setBody(responseBody);
        mockResponse.setHeaders(new HashMap<>());
        mockResponse.getHeaders().put("x-request-id", "12345");

        try (MockedStatic<HttpClientUtil> httpClientUtilMockedStatic = mockStatic(HttpClientUtil.class)) {

            httpClientUtilMockedStatic.when(() -> HttpClientUtil.sendPostRequest(anyString(), anyString())).thenReturn(mockResponse);

            // Act
            OcrResponse result = imageRecognizeProxy.getOcrResult(ocrRequest);

            // Assert
            assertEquals("12345", result.getXRequestId());
            httpClientUtilMockedStatic.verify(() -> HttpClientUtil.sendPostRequest(ocrUrl, requestBody));
        }
    }


    @Test
    public void testGetOcrResult_HttpClientReturnsNull() {
        // 准备数据
        OcrRequest ocrRequest = new OcrRequest();
        //全字段set
        ocrRequest.setImageURL(imageUrlList);
        String requestBody = GsonUtil.toJsonString(ocrRequest);
        MockedStatic<HttpClientUtil> httpClientUtilMockedStatic = null;

        //静态方法需要使用mockStatic使用进行静态mock
        try {
            httpClientUtilMockedStatic = mockStatic(HttpClientUtil.class);
            httpClientUtilMockedStatic.when(() -> HttpClientUtil.sendPostRequest(anyString(), anyString())).thenReturn(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageRecognizeProxy.getOcrResult(ocrRequest);
//        assertThrows(BizException.class, () -> imageRecognizeProxy.getOcrResult(ocrRequest));
//        System.out.println("这句话执行");
    }
}