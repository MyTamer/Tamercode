package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.JuGroupAssignResponse;
import com.taobao.api.ApiRuleException;

/**
 * TOP API: taobao.ju.group.assign request
 * 
 * @author auto create
 * @since 1.0, 2011-10-24 16:00:47
 */
public class JuGroupAssignRequest implements TaobaoRequest<JuGroupAssignResponse> {

    private TaobaoHashMap udfParams;

    private Long timestamp;

    /** 
	* 代表需要返回的商品对象字段。可选值：ItemData商品结构体中所有字段均可返回；多个字段用“,”分隔。如果fields为空，或者不传该参数，就默认获得所有的字段
	 */
    private String fields;

    /** 
	* IPHONE,WAP,ANDROID,SINA,163 各种终端类型
	 */
    private String terminalType;

    /** 
	* 终端的唯一标识，web可以用cookie，手机使用手机号码等，确保唯一性即可，用于分配商品组
	 */
    private String uuid;

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFields() {
        return this.fields;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalType() {
        return this.terminalType;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApiMethodName() {
        return "taobao.ju.group.assign";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("fields", this.fields);
        txtParams.put("terminal_type", this.terminalType);
        txtParams.put("uuid", this.uuid);
        if (udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public void putOtherTextParam(String key, String value) {
        if (this.udfParams == null) {
            this.udfParams = new TaobaoHashMap();
        }
        this.udfParams.put(key, value);
    }

    public Class<JuGroupAssignResponse> getResponseClass() {
        return JuGroupAssignResponse.class;
    }

    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(terminalType, "terminalType");
        RequestCheckUtils.checkNotEmpty(uuid, "uuid");
    }
}
