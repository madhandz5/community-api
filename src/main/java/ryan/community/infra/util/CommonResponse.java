package ryan.community.infra.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ToString
public class CommonResponse {
    public enum Code {
        SUCCESS("0000", "success"),
        //        ACCOUNT
        LOGIN_REQUIRED("1000", "Need to login"),
        EMAIL_PASSWORD_MISMATCH("1002", "Email and password you entered didn't match"),
        USED_EMAIL("1003", "Email already used"),
        USER_NOT_EXIST("1004", "User not exist"),
        INVALID_PASSWORD("1005", "Password is not valid"),
        INAPPROPRIATE_USER("1006", "Inappropriate user"),
        NOT_FOUND_PROVIDER("1007", "not found provider"),

//        REQUEST

        //REQUEST,
        BAD_REQUEST("4000", "Bad request"),
        ACCESS_DENIED("4003", "Access is denied"),

        //SERVER
        SERVER_ERROR("8000", "Server error"),
        EXCEPTION("8888", "Exception"),
        NO_DATA("9999", "No Data");


        private String codeNumber;
        private String codeMessage;

        Code(String codeNumber, String codeMessage) {
            this.codeNumber = codeNumber;
            this.codeMessage = codeMessage;
        }

        public String getCodeNumber() {
            return codeNumber;
        }

        public String getCodeMessage() {
            return codeMessage;
        }
    }

    @JsonProperty("rCode")
    private String rCode;

    @JsonProperty("rMessage")
    private String rMessage;

    @JsonProperty("rData")
    private Map<?, ?> rData;

    public CommonResponse() {
        this.rCode = "RET" + Code.SERVER_ERROR.codeNumber;
        this.rMessage = Code.SERVER_ERROR.getCodeMessage();
        this.rData = null;
    }

    public CommonResponse(Code code, Map<?, ?> rData) {
        this.rCode = "RET" + code.codeNumber;
        this.rMessage = code.codeMessage;
        this.rData = rData;
    }

    public void setCode(String code, String message) {
        this.rCode = "RET" + code;
        this.rMessage = message;
    }

    public void setCode(Code code) {
        this.rCode = "RET" + code.codeNumber;
        this.rMessage = code.codeMessage;
    }

    public void setrMessage(String rMessage) {
        this.rMessage = rMessage;
    }

    public void setrData(Map<?, ?> rData) {
        this.rData = rData;
    }
}
