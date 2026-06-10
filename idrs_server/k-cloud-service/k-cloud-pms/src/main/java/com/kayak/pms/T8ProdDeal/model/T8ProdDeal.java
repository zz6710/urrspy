package com.kayak.pms.T8ProdDeal.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud  
 * @description: 产品流水表
 * @author:  WangZhenXin
 * @create: 2021-01-06 09:13:02 
 * @memo 备注信息
 */

@Data
@GraphQLModel(fetcher="t8ProdDealService",table="ods_amng_prod_deal")
public class T8ProdDeal {

	/**
	 * id
	 */
   	@GraphQLField(label="id", sql="id=$S{id}",field="id",key = true)
	private String id;

	@GraphQLField(key = true ,kkhtml = "KFieldText", label = "t8_prod_info_id", sql = "t8_prod_info_id = $S{t8ProdInfoId}" ,field = "t8_prod_info_id")
	private String t8ProdInfoId;
	/**
	 * 产品流水事件:1-认购,2-申购,3-赎回,4-现金分红,5-红利再投,6-费用,7-兑付,8-付息
	 */
   	@GraphQLField(label="流水类型", sql="prod_deal_type=$S{prodDealType}",field="prod_deal_type")
	private String prodDealType;

	/**
	 * 产品代码
	 */
   	@GraphQLField(label="产品代码", sql="prod_code=$S{prodCode}",field="prod_code")
	private String prodCode;

	/**
	 * 子产品代码
	 */
   	@GraphQLField(label="子产品代码", sql="sub_prod_code=$S{subProdCode}",field="sub_prod_code")
	private String subProdCode;

	/**
	 * 发生日期
	 */
   	@GraphQLField(label="发生日期", sql="change_date=$S{changeDate}",field="change_date")
	private String changeDate;

	/**
	 * 百元应收利息
	 */
   	@GraphQLField(label="百元应收利息", sql="pay_unit_interest=$S{payUnitInterest}",field="pay_unit_interest")
	private Double payUnitInterest;

	/**
	 * 费用类型:不是费用的交易流水该字段为null值。
	 */
   	@GraphQLField(label="费用类型", sql="fee_type=$S{feeType}",field="fee_type")
	private String feeType;

	/**
	 * 费用金额
	 */
   	@GraphQLField(label="费用金额", sql="fee_money=$S{feeMoney}",field="fee_money")
	private Double feeMoney;

	/**
	 * 认申赎金额
	 */
   	@GraphQLField(label="认申赎金额", sql="subs_amt=$S{subsAmt}",field="subs_amt")
	private Double subsAmt;

	/**
	 * 认申赎份额
	 */
   	@GraphQLField(label="认申赎份额", sql="subs_vol=$S{subsVol}",field="subs_vol")
	private Double subsVol;

	/**
	 * 赎回份额
	 */
   	@GraphQLField(label="赎回份额", sql="redeem_vol=$S{redeemVol}",field="redeem_vol")
	private Double redeemVol;

	/**
	 * 赎回金额
	 */
   	@GraphQLField(label="赎回金额", sql="redeem_amt=$S{redeemAmt}",field="redeem_amt")
	private Double redeemAmt;

	/**
	 * 赎回收益,到期收益,付息收益
	 */
   	@GraphQLField(sql="convert_income=$S{convertIncome}",field="convert_income")
	private Double convertIncome;

	/**
	 * 是否跑批完成
	 */
   	@GraphQLField(label="是否跑批完成", sql="iscomputed=$S{iscomputed}",field="iscomputed")
	private String iscomputed;

	/**
	 * 产品付息到期支持撤销用于区分当前付息记录是计划产生还是自定义产生。
	 */
   	@GraphQLField(sql="isrevoke=$S{isrevoke}",field="isrevoke")
	private String isrevoke;

	/**
	 * 交易状态：0——录入1——确认2——撤销
	 */
   	@GraphQLField(label="交易状态", sql="trans_stat=$S{transStat}",field="trans_stat")
	private String transStat;

	/**
	 * 客户实际收益率
	 */
   	@GraphQLField(label="客户实际收益率", sql="real_rate=$S{realRate}",field="real_rate")
	private Double realRate;

	/**
	 * 实际付息日（中间付息）
	 */
   	@GraphQLField(label="实际付息日（中间付息）", sql="real_intrday=$S{realIntrday}",field="real_intrday")
	private String realIntrday;

	/**
	 * 兑付净值
	 */
   	@GraphQLField(label="兑付净值", sql="cash_net=$S{cashNet}",field="cash_net")
	private Double cashNet;

	/**
	 * 理财产品投资资产的含税合计(百分之0)
	 */
   	@GraphQLField(label="理财产品投资资产的含税合计(百分之0)", sql="hs_zero=$S{hsZero}",field="hs_zero")
	private Double hsZero;

	/**
	 * 理财产品投资资产的含税合计(百分之3)
	 */
   	@GraphQLField(label="理财产品投资资产的含税合计(百分之3)", sql="hs_three=$S{hsThree}",field="hs_three")
	private Double hsThree;

	/**
	 * 理财产品投资资产的含税合计(百分之6)
	 */
   	@GraphQLField(label="理财产品投资资产的含税合计(百分之6)", sql="hs_six=$S{hsSix}",field="hs_six")
	private Double hsSix;

	/**
	 * 分红计划id
	 */
   	@GraphQLField(label="分红计划id", sql="repo_bonus_plan_id=$S{repoBonusPlanId}",field="repo_bonus_plan_id")
	private String repoBonusPlanId;

	/**
	 * 开户行行号
	 */
   	@GraphQLField(label="开户行行号", sql="opening_account_num=$S{openingAccountNum}",field="opening_account_num")
	private String openingAccountNum;

	/**
	 * 开户行名称
	 */
   	@GraphQLField(label="开户行名称", sql="opening_account_name=$S{openingAccountName}",field="opening_account_name")
	private String openingAccountName;

	/**
	 * 资金账户
	 */
   	@GraphQLField(label="资金账户", sql="account_code=$S{accountCode}",field="account_code")
	private String accountCode;

	/**
	 * 资金账户名称
	 */
   	@GraphQLField(label="资金账户名称", sql="account_name=$S{accountName}",field="account_name")
	private String accountName;

	/**
	 * 业绩报酬
	 */
   	@GraphQLField(label="业绩报酬", sql="remuneration=$S{remuneration}",field="remuneration")
	private Double remuneration;

	/**
	 * 客户年化收益率
	 */
   	@GraphQLField(label="客户年化收益率", sql="customer_annualized_rate=$S{customerAnnualizedRate}",field="customer_annualized_rate")
	private String customerAnnualizedRate;

	/**
	 * 创建日期
	 */
   	@GraphQLField(label="创建日期", sql="create_date=$S{createDate}",field="create_date")
	private String createDate;

	/**
	 * 创建时间
	 */
   	@GraphQLField(label="创建时间", sql="create_time=$S{createTime}",field="create_time")
	private String createTime;

	/**
	 * 更新日期
	 */
   	@GraphQLField(label="更新日期", sql="update_date=$S{updateDate}",field="update_date")
	private String updDate;

	/**
	 * 更新时间
	 */
   	@GraphQLField(label="更新时间", sql="update_time=$S{updateTime}",field="update_time")
	private String updTime;

	/**
	 * 创建人
	 */
   	@GraphQLField(label="创建人", sql="create_user_id=$S{createUserId}",field="create_user_id")
	private String crtUserId;

	/**
	 * 创建人名称
	 */
   	@GraphQLField(label="创建人名称", sql="create_user_name=$S{createUserName}",field="create_user_name")
	private String crtUserName;

}
