package com.ddd.example.infrastructure.valueobject;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-07-09 20:47
 */
public class BizException extends RuntimeException {
    /**
     * 错误码
     */
    protected final ErrorCode errorCode;
    /**
     * 自定义具体的异常信息描述，更细粒度的错误，例如参数校验错误，具体哪个id， RuntimeException的message放参数校验的错误码
     */
    private String msg;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 无参默认构造NETWORK_ERROR 网路异常
     */
    public BizException() {
        super(BizErrorCodeEnum.NETWORK_ERROR.getDescription());
        this.errorCode = BizErrorCodeEnum.NETWORK_ERROR;
    }

    /**
     * 指定错误码构造通用异常
     *
     * @param errorCode 错误码
     */
    public BizException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;

        this.msg = errorCode.getDescription();
    }

    /**
     * 指定详细描述构造通用异常，一般使用这个
     *
     * @param detailedMessage 详细描述
     */
    public BizException(ErrorCode errorCode, String detailedMessage) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.msg = detailedMessage;
    }

    /**
     * 指定导火索构造通用异常
     *
     * @param t 导火索
     */
    public BizException(final Throwable t) {
        super(t);
        this.errorCode = BizErrorCodeEnum.NETWORK_ERROR;
    }

    /**
     * 构造通用异常，构造异常，Throwable包装成业务异常，不带详情描述的
     *
     * @param errorCode 错误码
     * @param t         导火索
     */
    public BizException(final ErrorCode errorCode, final Throwable t) {
        super(errorCode.getDescription(), t);
        this.errorCode = errorCode;
    }

    /**
     * 构造通用异常,默认的服务器故障的异常，导火索异常
     *
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public BizException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = BizErrorCodeEnum.NETWORK_ERROR;
    }

    /**
     * 构造异常，Throwable包装成业务异常,带有详情描述的
     *
     * @param errorCode       错误码
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public BizException(final ErrorCode errorCode, final String detailedMessage,
                        final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = errorCode;
    }
}
