import com.ddd.example.infrastructure.utils.HttpClientUtil;
import com.ddd.example.infrastructure.utils.httpdto.JsonStringHttpResponse;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试VictoriaMetrics上报日志，主动通过http上报日志
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-12-11 16:02
 */
public class TestVictoriaMetricsReport {


    /**
     * 构造200上报的数据
     */
    public String build200Metric(String metricName, long timestamp, double value) {
        // 动态创建标签
        Map<String, String> labels = Maps.newHashMap();
        labels.put("path", "service_dog/test/api/demo/" + metricName);
        labels.put("status", "200");
        labels.put("region", "Asia/Shanghai");
        return buildMetric(metricName, labels, value, timestamp);
    }

    /**
     * 构造500上报的数据
     */
    public String build500Metric(String metricName, long timestamp, double value) {
        // 动态创建标签
        Map<String, String> labels = Maps.newHashMap();
        labels.put("path", "service_dog/test/api/demo/" + metricName);
        labels.put("status", "500");
        labels.put("region", "Asia/Shanghai");
        return buildMetric("http_request_duration_seconds_count", labels, value, timestamp);
    }


    /**
     * 构建指标字符串
     *
     * @param metricName 指标名称
     * @param labels     标签集合
     * @param value      指标值
     * @return 指标字符串
     */
    private static String buildMetric(String metricName, Map<String, String> labels, double value, long timestamp) {
        // 构建标签部分
        StringBuilder labelsBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : labels.entrySet()) {
            labelsBuilder.append(entry.getKey())
                    .append("=\"")
                    .append(entry.getValue())
                    .append("\",");
        }
        if (labelsBuilder.length() > 0) {
            labelsBuilder.deleteCharAt(labelsBuilder.length() - 1); // 移除最后的逗号
        }
        // 构建指标字符串，一定有换行符
        return String.format("%s{%s} %f %d\n", metricName, labelsBuilder, value, timestamp);
    }


    /**
     * 发送 HTTP 请求查询 VictoriaMetrics 数据
     *
     * @param promql PromQL 表达式
     * @param start  起始时间（Unix 时间） ，秒
     * @param end    结束时间（Unix 时间），秒
     * @param step   时间步长（如：1h）
     * @return 查询结果
     * @throws IOException 异常
     */
    private static JsonStringHttpResponse queryVictoriaMetrics(String url, String promql, long start, long end, String step, int timeout, String userName, String password) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("query", promql);
        params.put("start", String.valueOf(start));
        params.put("end", String.valueOf(end));
        params.put("step", step);
        return HttpClientUtil.doGetJsonRequest(url, params, timeout, userName, password);
    }

    /**
     * 构造 PromQL 查询表达式
     * 按照小时聚合语句sum(increase(http_requests_total[1h])) by (label1, label2)
     *
     * @param metricName Metric 名称
     * @param labels     标签集合
     * @return PromQL 查询字符串
     */
    private static String buildQuery(String metricName, List<String> labels) {
        if (labels.isEmpty()) {
            return metricName;
        }
        String labelString = String.join(",", labels);
        // PromQL 查询：按照小时聚合请求数量
        return String.format("sum(increase(%s[1h])) by (%s)", metricName, labels);
    }

    @Test
    public void testMetric() {
        String image_metric = "dog_test_http_image_xx_count";
        String text_metric = "dog_test_http_text_xx_count";
        //今天9点的时间戳 2024-12-13 16:00:00
        long time = 1734076800000L;
        String text_200_16 = build200Metric(text_metric, time, 4);
        String text_500_16 = build500Metric(text_metric, time, 5);
        String image_200_16 = build200Metric(image_metric, time, 6);
        String image_500_16 = build500Metric(image_metric, time, 7);

        //时间 2024-12-13 14:00:00
        long time1 = 1734069600000L;
        String text_200_17 = build200Metric(text_metric, time1, 12);
        String text_500_17 = build500Metric(text_metric, time1, 1);
        String image_200_17 = build200Metric(image_metric, time1, 12);
        String image_500_17 = build500Metric(image_metric, time1, 3);

        // 创建指标数据
        StringBuilder metricsData = new StringBuilder();
        metricsData.append(text_200_16);
        metricsData.append(text_500_16);
        metricsData.append(image_200_16);
        metricsData.append(image_500_16);
        metricsData.append(text_200_17);
        metricsData.append(text_500_17);
        metricsData.append(image_200_17);
        metricsData.append(image_500_17);

        System.out.println(metricsData);
        // 请求获取数据的超时时间(即响应时间)，单位毫秒。
        int SOCKET_TIMEOUT = 5000;
        String vm_url = "http://127.0.0.1:8428/api/v1/import/prometheus";
        String userName = "monitoradmin";
        String pwd = "ZmKK5YhdMVZpuOM3";


        boolean result = HttpClientUtil.send204TextRequest(vm_url, metricsData.toString(), SOCKET_TIMEOUT, userName, pwd);
//        上报204就是成功
        System.out.println("result: " + result);
    }


    /**
     * 查询当前不同时间段的的数据统计
     */
    @Test
    public void testQuery() throws IOException {
        String image_metric = "dog_test_http_image_xx_count";
        String text_metric = "dog_test_http_text_xx_count";
        //开始时间 2024-12-13 01:00:00
        long start = 1734022800L;
        //截止时间 2024-12-13 17:00:00
        long end = 1734080400L;
        String step = "1h";
        String url = "http://127.0.0.1:8428/api/v1/query_range";
        String promql = "increase(dog_test_http_text_xx_count[1h])";
        String userName = "monitoradmin";
        String pwd = "ZmKK5YhdMVZpuOM3";
        JsonStringHttpResponse response = queryVictoriaMetrics(url, promql, start, end, step, 5000, userName, pwd);
        System.out.println(response.getBody());
    }

}
