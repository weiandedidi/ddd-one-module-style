package com.ddd.example.domain.proxy.valueobject;

import java.util.Objects;

/**
 * @version 1.0
 * @create 2024-07-09 20:54
 */
public enum OtherBizErrorCodeEnum {
    SUCCESS(0, "请求成功"),
    REQUEST_PATH_ERROR(400001, "请求路径错误"),
    REQUEST_METHOD_ERROR(400002, "请求方法错误，请使用 POST 请求"),
    EMPTY_REQUEST_BODY(400003, "请求体请求数据为空，没有包含内容"),
    NON_JSON_FORMAT_REQUEST_BODY(400004, "请求体内容需要符合 json 要求"),
    INCORRECT_REQUEST_BODY_TYPE(400005, "请求体需为字典，不能为其他类型"),
    MISSING_REQUIRED_PARAMETER(400006, "必须的参数（Action）未传\n必须的参数（ImageData 或 ImageURL）未传"),
    INCORRECT_PARAMETER_TYPE(400008, "Action 字段应该是 string 类型\nImageData 字段应该是 string 类型\nImageURL 字段应该是 string 类型"),
    EMPTY_PARAMETER_VALUE(400009, "Action 字段值为空字符\nImageData 字段值为空字符\nImageURL 字段值为空字符"),
    INCORRECT_PARAMETER_VALUE(400010, "Action 值设置错误"),
    BASE64_DATA_PROCESSING_EXCEPTION(400011, "ImageData 字段的 base64 字符串转换字节码异常"),
    INVALID_FILE_FORMAT(400012, "仅支持 jpeg/png/jpg/bmp 格式"),
    FILE_SIZE_REQUIREMENT_NOT_MET(400013, "该文件大小不符合要求，图片要求小于 7M"),
    FILE_DOWNLOAD_ERROR(400015, "无法解析图片链接，下载失败"),
    DUPLICATE_REQUIRED_PARAMETER(400016, "必须的参数（ImageData 或 ImageURL）只能二选一"),
    IMAGE_DECODING_ERROR(410001, "字节码解码为图片错误"),
    IMAGE_DIMENSIONS_REQUIREMENT_NOT_MET(410002, "分辨率长宽尺寸应不高于 5000 不低于 32"),
    SERVICE_INTERFACE_EXCEPTION(500001, "服务接口异常，请联系管理员"),
    ;

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误描述
     */
    private String description;

    OtherBizErrorCodeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isRequestSuccess(Integer code) {
        return Objects.equals(code, SUCCESS.getCode());
    }

}
