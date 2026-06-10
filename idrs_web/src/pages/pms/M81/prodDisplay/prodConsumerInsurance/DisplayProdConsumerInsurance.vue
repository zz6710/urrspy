<template>
  <k-form dataInputWidth="700px" ref="addT8ProdConsumerInsuranceForm" :data-col="1">
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel"  >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="基本信息"/>
        </div>
        <k-form ref="addForm" data-input-width="240px">
        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode"
                          :dataAllowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-disabled="true"
                        :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="填报日期">
          <k-field-date v-model="formData.filledDate"
                        :data-max-length="8"
                        :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="填报部门">
          <k-field-cascader style="width:100%" v-model="formData.filledDept" data-diffcondition="deptno,parentdeptno" :data-disabled="true"
                            :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num
                            :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                            data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
          </k-field-cascader>
        </k-form-item>
        <k-form-item label="填报人">
          <k-field-select v-model="formData.filledUser"   data-display-field="username" :dataAllowblank="false"
                          data-action="User.getUser" data-value-field="userid" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="审核人部门">
          <k-field-cascader style="width:100%" v-model="formData.approvalDept" data-diffcondition="deptno,parentdeptno" :data-disabled="true"
                            :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num
                            :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                            data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
          </k-field-cascader>
        </k-form-item>
        <k-form-item label="审核人">
          <k-field-select v-model="formData.approvalUser"   data-display-field="username" :dataAllowblank="false"
                          data-action="User.getUser" data-value-field="userid" :data-disabled="true" />
        </k-form-item>
        </k-form>
      </div>
    </div>
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel" >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="审批要素"/>
        </div>
        <k-form dataLabelWidth="240px">
        <k-form-item label="关键词提示及详细解释" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.keyWords" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        </k-form>
      </div>
    </div>
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel" >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="内容摘要"/>
        </div>
        <k-form dataLabelWidth="240px">
        <k-form-item label="特征/属性" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.featuresAttributes" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="风险状况及对应客户风险承受能力" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.riskLevel" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="风险提示" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.riskStatement" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="收益分配" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.incomeDistribution" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="后续争议解决途径" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.disputeResolution" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="减轻或免除自身责任条款/限制或排除消费者主要权利的条款" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.liabilityClause" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        </k-form>
      </div>
    </div>
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel" >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="消费者个人金融信息安全保护条款"/>
        </div>
        <k-form dataLabelWidth="240px">
        <k-form-item label="消费者个人金融信息安全保护条款" data-input-width="505px">
          <k-field-text :dataAllowblank="false" v-model="formData.protectionClause" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        </k-form>
      </div>
    </div>
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel" >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="是否提示收益区间"/>
        </div>
        <k-form dataLabelWidth="240px">
        <k-form-item label="是否提示收益区间" data-input-width="505px">
          <!--              <k-field-radio  v-model="formData.isIncomeRange" :data-data="options"/>-->
          <k-field-radio v-model="formData.radioValue2" :data-data="options1" :data-default-value="'1'"
                         data-value-field="value" data-display-field="username" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="融资单位和项目名称" data-input-width="505px">
          <k-field-text  v-model="formData.financeName" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="期限规模" data-input-width="505px">
          <k-field-text   v-model="formData.termScale" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="交易结构" data-input-width="505px">
          <k-field-text v-model="formData.transactionStructure" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="到期收益分配" data-input-width="505px">
          <k-field-text   v-model="formData.yieldOfMaturity" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="测算依据和测算方式" data-input-width="505px">
          <k-field-text  v-model="formData.guessAndEstimate" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        </k-form>
      </div>
    </div>
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel" >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="是否冷静期"/>
        </div>
        <k-form dataLabelWidth="240px">
        <k-form-item label="是否设置冷静期或募集期" data-input-width="505px">
          <!--              <k-field-radio  v-model="formData.isCoolingOff" :data-data="options"/>{{ formData.isCoolingOff }}-->
          <k-field-radio v-model="formData.radioValue" :data-data="options1" :data-default-value="'1'"
                         data-value-field="value" data-display-field="username" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="冷静期或募集期说明" v-show="formData.radioValue=='1'" data-input-width="505px">
          <k-field-text  v-model="formData.coolingOffDesc" :dataAllowblank="formData.radioValue=='0'" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        </k-form>
      </div>
    </div>
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel" >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="允许消费者在有效时段对购买行为做出反悔决定"/>
        </div>
        <k-form dataLabelWidth="310px">
        <k-form-item label="允许消费者在有效时段对购买行为做出反悔决定" data-input-width="440px">
          <k-field-text  v-model="formData.isGoBack" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        </k-form>
      </div>
    </div>
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel" >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="收费"/>
        </div>
        <k-form dataLabelWidth="240px">
        <k-form-item label="收费标准" data-input-width="505px">
          <k-field-text  v-model="formData.chargingStandard" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="主要依据" data-input-width="505px">
          <k-field-text  v-model="formData.mainBasis" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="成立条件" data-input-width="505px">
          <k-field-text  v-model="formData.establishMethod" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="收取方式" data-input-width="505px">
          <k-field-text  v-model="formData.collectionMethod" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="可能采取的优惠" data-input-width="505px">
          <k-field-text  v-model="formData.possibleBenefits" :data-disabled="true" inputType="textarea" :rows="1"/>
        </k-form-item>
        </k-form>
      </div>
    </div>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdConsumerInsurance",
      props:{
        formData:{},
      },
      computed: {
        querydeptGraphql() {
          return "{queryDept(action:\"find\") {rows{deptno, deptname, parentdeptno, deptid},results}}"
        }
      },
      data() {
        return {
          options1: [{
            value: '1',
            username: '是',
          }, {
            value: '0',
            username: '否',
          }],
        }
      }
    }
</script>

<style scoped>

</style>
