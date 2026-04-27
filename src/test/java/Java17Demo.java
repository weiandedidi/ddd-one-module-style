import com.ddd.example.infrastructure.utils.JSONUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * java17的新特性测试
 *
 * @author maqidi
 * @version 1.0
 * @create 2026-04-27 17:35
 */
public class Java17Demo {

    @Test
    public void testVar() {
        var logList = new ArrayList<String>();
        var letterList = buildList();
        System.out.println(letterList);

        //优化stream，精简stream().toList()
        List<Integer> numbers = List.of(1, 2, 3, 4, 6);
        List<Integer> data = numbers.stream().toList();
        System.out.println(data);

    }

    private static List<String> buildList() {
        //不可变集合测试. list.add不能添加
        return List.of("a", "b", "c");
    }

    //测试无业务方法定义的实体类record
    public static record Aoo(
            String name,
            Integer age,
            String address
    ) {
    }

    @Test
    public void testJsonBlock() {
        String jsonStr = """
                {
                        "name": "张三",
                        "age": 12,
                        "address": "北京市朝阳区"
                    }
                """;
        System.out.println(jsonStr);
        System.out.println("================= 测试json反序列化 ==============");
        Aoo aoo = JSONUtil.fromJsonString(jsonStr, Aoo.class);
        System.out.println(aoo.name());
        System.out.println(aoo.age());
        System.out.println(aoo.age());

        //html block
        String html = """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <!-- 网页编码，防止乱码 -->
                    <meta charset="UTF-8">
                    <!-- 手机端自适应 -->
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>我的第一个HTML页面</title>
                                
                    <!-- 这里写简单样式 -->
                    <style>
                        body {
                            font-family: Arial;
                            background-color: #f5f5f5;
                            margin: 30px;
                        }
                        .box {
                            background: white;
                            padding: 20px;
                            border-radius: 10px;
                            max-width: 600px;
                            margin: auto;
                        }
                        button {
                            background: #42b983;
                            color: white;
                            border: none;
                            padding: 10px 20px;
                            border-radius: 5px;
                            cursor: pointer;
                        }
                        button:hover {
                            background: #359469;
                        }
                    </style>
                </head>
                                
                <body>
                    <div class="box">
                        <h1>👋 你好，这是 HTML Demo</h1>
                        <p>这是一个简单的 HTML 示例，包含常用标签：</p>
                                
                        <h3>📋 我的学习清单</h3>
                        <ul>
                            <li>学习 HTML</li>
                            <li>学习 CSS</li>
                            <li>学习 JavaScript</li>
                        </ul>
                                
                        <h3>🖼️ 示例图片</h3>
                        <img src="https://picsum.photos/400/200" alt="示例图片" width="400">
                                
                        <br><br>
                        <button onclick="alert('你点击了按钮！')">点我试试</button>
                    </div>
                </body>
                </html>""";
        System.out.println(html);

        //测试sql 字符块
        String sql = """
                INSERT INTO users (username, email, age, gender, city)
                VALUES
                ('张三', 'zhangsan@test.com', 25, '男', '北京'),
                ('李四', 'lisi@test.com', 30, '女', '上海'),
                ('王五', 'wangwu@test.com', 28, '男', '广州'),
                ('赵六', 'zhaoliu@test.com', 22, '女', '深圳'),
                ('钱七', 'qianqi@test.com', 35, '男', '杭州');
                """;
        System.out.println(sql);
    }

    @Test
    public void testInstanceof() {
        var obj = new Object();

        if (obj instanceof String str) {
            System.out.println(str.length());
        } else {
            System.out.println("转化定义了");
        }
    }

    @Test
    public void testSwitchLambda() {
        int status = 13 % 2;
        String desc = switch (status) {
            case 0 -> "待执行";
            case 1 -> "执行中";
            default -> "未知";
        };
        System.out.println(desc);

        int score = 87;
        String level = switch (score) {
            case 90, 100 -> "优秀";
            case 60, 70, 80 -> "合格";
            default -> {
                yield "不合格";
            }
        };
        System.out.println(level);
    }


}
