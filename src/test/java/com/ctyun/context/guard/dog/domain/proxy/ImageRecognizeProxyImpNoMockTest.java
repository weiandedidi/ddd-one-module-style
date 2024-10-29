package com.ddd.example.domain.proxy;

import com.ddd.example.domain.proxy.dto.OcrRequest;
import com.ddd.example.domain.proxy.dto.OcrResponse;
import com.ddd.example.infrastructure.proxyimpl.ImageRecognizeProxyImpl;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-07-16 19:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageRecognizeProxyImpNoMockTest {

    @Autowired
    ImageRecognizeProxyImpl imageRecognizeProxy;

    /**
     * mock http请求
     */
    @Test
    public void testOcr() {
        OcrRequest ocrRequest = new OcrRequest();
        ocrRequest.setImageURL(Lists.newArrayList("\"http://example.com/image3.jpg\""));
        OcrResponse ocrResponse = imageRecognizeProxy.getOcrResult(ocrRequest);
        System.out.println(ocrResponse);
    }
}
