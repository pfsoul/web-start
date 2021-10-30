package top.soulblack.quick.common.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author : soulblack
 * @since : 2021/10/31
 */
@ApiModel(description = "请求返回结果")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final transient String DEFAULT_SUCCESS_CODE = "0";
    public static final transient String DEFAULT_ERROR_CODE = "-1";
    @ApiModelProperty("0:成功;其他:失败错误码")
    private String code;
    @ApiModelProperty("返回的结果")
    private T data;
    @ApiModelProperty("错误信息，给开发者使用。（可选）")
    private String message;
    @ApiModelProperty("提示信息，终端用户使用。（可选）")
    private String info;
    @ApiModelProperty("请求id")
    private String requestId;

    public Result(T data) {
        this.data = data;
        this.code = "0";
        this.message = "success";
    }

    public Result() {
        this.code = "-1";
        this.message = "failure";
    }

    public boolean isSuccess() {
        return "0".equals(this.code);
    }

    public static Result success() {
        return new Result((Object) null);
    }

    public static Result success(Object data) {
        return new Result(data);
    }

    public static Result DefaultFailure(String msg) {
        Result result = new Result();
        result.setCode("-1");
        result.setMessage(msg);
        return result;
    }

    public String getCode() {
        return this.code;
    }

    public T getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }

    public String getInfo() {
        return this.info;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public Result<T> setCode(final String code) {
        this.code = code;
        return this;
    }

    public Result<T> setData(final T data) {
        this.data = data;
        return this;
    }

    public Result<T> setMessage(final String message) {
        this.message = message;
        return this;
    }

    public Result<T> setInfo(final String info) {
        this.info = info;
        return this;
    }

    public Result<T> setRequestId(final String requestId) {
        this.requestId = requestId;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Result)) {
            return false;
        } else {
            Result<?> other = (Result) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71:
                {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label71;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label71;
                    }

                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                label57:
                {
                    Object this$message = this.getMessage();
                    Object other$message = other.getMessage();
                    if (this$message == null) {
                        if (other$message == null) {
                            break label57;
                        }
                    } else if (this$message.equals(other$message)) {
                        break label57;
                    }

                    return false;
                }

                Object this$info = this.getInfo();
                Object other$info = other.getInfo();
                if (this$info == null) {
                    if (other$info != null) {
                        return false;
                    }
                } else if (!this$info.equals(other$info)) {
                    return false;
                }

                Object this$requestId = this.getRequestId();
                Object other$requestId = other.getRequestId();
                if (this$requestId == null) {
                    if (other$requestId == null) {
                        return true;
                    }
                } else if (this$requestId.equals(other$requestId)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Result;
    }

    public int hashCode() {
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $info = this.getInfo();
        result = result * 59 + ($info == null ? 43 : $info.hashCode());
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        return result;
    }

    public String toString() {
        return "Result(code=" + this.getCode() + ", data=" + this.getData() + ", message=" + this.getMessage() + ", info=" + this.getInfo() + ", requestId=" + this.getRequestId() + ")";
    }
}
