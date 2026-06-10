package com.kayak.dps.expresssion.model.dto;

import com.kayak.dps.direct.model.dto.IndexCodeDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * 指标校验数据模型
 */
@Data
@Accessors(chain = true)
public class ExpressDTO {

    /**
     * 指标校验编号
     */
    private String indexCode;

    /**
     * 指标校验公式
     */
    private String expressVal;

    /**
     * 表达式准许差值
     */
    private BigDecimal deviation;


    public static ExpressDTO dto() {
        return new ExpressDTO();
    }

    /**
     * 初始化数据
     * @return
     */
    public static ExpressDTO initDTO(String indexCode, String expressVal, String deviation) {
        return dto().setIndexCode(indexCode)
                .setExpressVal(expressVal)
                .setDeviation(new BigDecimal(deviation));
    }
}
