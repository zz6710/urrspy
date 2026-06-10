<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ObjectGrid" v-model="prodSearchParam">
        <k-form-item label="信息规则名称">
          <k-field-text v-model="prodSearchParam.prodCode" />
        </k-form-item>
        <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary"
               data-target="addPopup" :data-handler="openAddPage" >
          <md-icon>add</md-icon>
          新增
        </k-btn>
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" data-action="" :data-checkbox="true" data-checkbox-id="prodCode"
              :dataData="dataList" :dataAutoload="false" @data-db-click="tableDataDbClick" :data-display="false">
        <k-grid-column data-header="信披规则名称" data-name="name"/>
        <k-grid-column data-header="信披类型" data-dict="t8_disclosure_type" data-name="type"/>
        <!-- <k-grid-column data-header="信披子类型" data-name="itemType"/> -->
        <k-grid-column data-header="模板名称" data-name="modelName"/>
        <k-grid-column data-header="公告标题" data-name="title"/>
        <k-grid-column data-header="状态" data-name="status"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="btn-custom-plain" data-descript="启用" :data-disabled="scope.row.row.status == '启用'">
            启用
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="md-danger" data-descript="停用" :data-disabled="scope.row.row.status == '停用'">
            停用
          </k-btn>
        </template>
      </k-grid>


      <k-popup ref="addPopup" data-title="信披规则配置">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="基本信息" name="baseInfo">
          </el-tab-pane>
          <el-tab-pane label="时间规则" name="timeRule">
          </el-tab-pane>
        </el-tabs>
        <k-form ref="addForm" :data-model="formData" :data-col="2" v-show="this.activeName == 'baseInfo'">
          <k-form-item label="信披规则ID" :data-col="2">
            <k-field-text v-model="formData.id" style="width: 80%;" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="创建时间" :data-col="2">
            <k-field-text v-model="formData.crtDate" style="width: 80%;" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披规则名称" :data-col="2">
            <k-field-text v-model="formData.name" style="width: 80%;"/>
          </k-form-item>
          <k-form-item label="信披类型" :data-col="2">
            <k-field-select v-model="formData.type" data-dict="t8_disclosure_type" style="width: 80%;"/>
          </k-form-item>
          <!-- <k-form-item label="信披子类型" :data-col="2">
            <k-field-select v-model="formData.itemType" data-action="T8Dict.findNotEstablishProdInfos" style="width: 80%;"
                            data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"/>
          </k-form-item> -->
          <k-form-item label="信披模板" :data-col="2">
            <div>
              <k-field-select v-model="formData.modelId" style="width: 80%;" :data-data="modelData"
                              data-display-field="text" data-value-field="value"/>
              <k-btn class="btn-custom-plain" style="margin-top: -5px;">
                模板在线查看
              </k-btn>
            </div>
          </k-form-item>
          <k-form-item label="公告标题" :data-col="2">
            <div>
              <k-field-text v-model="formData.title" style="width: 80%;"/>
              <k-btn class="btn-custom-plain" style="margin-top: -5px;">
                按示例预览
              </k-btn>
            </div>
          </k-form-item>
          <k-form-item label=" " :data-col="2">
            <k-grid ref="t8ObjectGrid" :dataData="dataList2">
              <k-grid-column data-header="控件" data-name="control"/>
              <k-grid-column data-header="替换含义" data-name="meaning"/>
              <k-grid-column data-header="控件替换示例" data-name="example"/>
              <template slot="operate" slot-scope="scope" >
                <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                       class="btn-custom-plain" data-descript="引用-复制">
                  引用-复制
                </k-btn>
              </template>
            </k-grid>
          </k-form-item>
          <k-form-item label="公告负责角色" :data-col="2">
            <k-field-select v-model="formData.role" style="width: 80%;" :data-data="roleData"
              data-display-field="text" data-value-field="value"/>
          </k-form-item>
          <k-form-item label="信披产品" :data-col="2">
            <div>
              <el-radio v-model="formData.prodType" label="1">所有产品</el-radio>
              <br/>
              <el-radio v-model="formData.prodType" label="2">产品</el-radio>
              <k-field-select v-model="formData.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                              data-display-field="prodCode,prodName" data-value-field="prodCode"
                              :data-multiple="true" style="width: 300px;margin-left: 15px;"
                              :data-disabled="this.formData.prodType != 2"/>
              (多选)
              <br/>
              <br/>
              <el-radio v-model="formData.prodType" label="3">产品组</el-radio>
              <k-field-select v-model="formData.prodCodeGroup" :data-data="prodGroupData"
                  data-display-field="text" data-value-field="value"
                  :data-multiple="true" style="width: 300px;margin-left: 0px;" :data-disabled="this.formData.prodType != 3"/>(多选)
                <k-btn class="btn-custom-plain" style="margin-top: -5px;">
                  产品组维护
                </k-btn>
              <br/>
              <el-radio v-model="formData.prodType" label="4">与产品无直接关联</el-radio><br/>
            </div>
          </k-form-item>
          <k-form-item label="信披渠道" :data-col="2">
            <k-field-checkbox data-label-width="120px" v-model="formData.channel" :data-allowblank="false"
                              :data-data="xpqd" data-display-field="label" data-value-field="value"/>
          </k-form-item>
        </k-form>
        <k-form ref="addForm2" :data-col="2" v-show="this.activeName == 'timeRule'">
          <k-form-item label="信披产品" :data-col="2">
            <div>
              <el-radio v-model="formData.selectType" label="1">业务变更发起</el-radio>
              <el-radio v-model="formData.selectType" label="2">时间规则发起</el-radio>
              <el-radio v-model="formData.selectType" label="3">人工发起</el-radio>
            </div>
          </k-form-item>
          <k-form-item label="业务变更类型" :data-col="2">
            <k-field-tree :data-multiple="false" :dataAllowblank='formData.selectType == 2' :data-flat="false" v-model="formData.updType" data-diffcondition="deptno,parentdeptno" :data-data="updTypeData"
                          data-display-child="children" data-placeholder="请选择业务变更类型" data-display-field="deptname" style="width: 80%;"
                          data-value-field="deptno" :data-type="false">
            </k-field-tree>
          </k-form-item>
          <k-form-item label="基准日期" :data-col="2">
            <k-field-select v-model="formData.baseDate" :data-data="baseDate_dict" style="width: 180px;"
              data-display-field="text" data-value-field="value" data-placeholder=""/>
          </k-form-item>
          <k-form-item label="计划时间设置" :data-col="2">
            <div>
              <div>
                初始公告-生成日期 T
                <k-field-select v-model="formData.initCrtDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="formData.initCrtDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="formData.initCrtDateType" label="1">自然日</el-radio>
                  <el-radio v-model="formData.initCrtDateType" label="2">工作日</el-radio>
              </div>
              <div>
                计划-补录完成日期 T
                <k-field-select v-model="formData.supplementDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="formData.supplementDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="formData.supplementDateType" label="1">自然日</el-radio>
                  <el-radio v-model="formData.supplementDateType" label="2">工作日</el-radio>
              </div>
              <div>
                计划-审批完成日期 T
                <k-field-select v-model="formData.approvalDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="formData.approvalDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="formData.approvalDateType" label="1">自然日</el-radio>
                  <el-radio v-model="formData.approvalDateType" label="2">工作日</el-radio>
              </div>
              <div>
                计划-系统发布日期 T
                <k-field-select v-model="formData.releaseDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="formData.releaseDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="formData.releaseDateType" label="1">自然日</el-radio>
                  <el-radio v-model="formData.releaseDateType" label="2">工作日</el-radio>
              </div>
            </div>
          </k-form-item>
        </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary"
                  :data-handler="saveRule"
                 data-from="addForm" :data-model="formData"
                 data-target="t8ObjectGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-popup>

      <k-popup ref="addDesc" data-title="信披规则配置详情">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="基本信息" name="baseInfo">
          </el-tab-pane>
          <el-tab-pane label="时间规则" name="timeRule">
          </el-tab-pane>
        </el-tabs>
        <k-form ref="addForm" :data-model="formData" :data-col="2" v-show="this.activeName == 'baseInfo'">
          <k-form-item label="信披规则ID" :data-col="2">
            <k-field-text v-model="descFormData.id" style="width: 80%;" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="创建时间" :data-col="2">
            <k-field-text v-model="descFormData.crtDate" style="width: 80%;" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披规则名称" :data-col="2">
            <k-field-text v-model="descFormData.name" style="width: 80%;"/>
          </k-form-item>
          <k-form-item label="信披类型" :data-col="2">
            <k-field-select v-model="descFormData.type" data-dict="t8_disclosure_type" style="width: 80%;"/>
          </k-form-item>
          <!-- <k-form-item label="信披子类型" :data-col="2">
            <k-field-select v-model="descFormData.itemType" data-action="T8Dict.findNotEstablishProdInfos" style="width: 80%;"
                            data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"/>
          </k-form-item> -->
          <k-form-item label="信披模板" :data-col="2">
            <div>
              <k-field-select v-model="descFormData.modelId" style="width: 80%;" :data-data="modelData"
                              data-display-field="text" data-value-field="value"/>
              <k-btn class="btn-custom-plain" style="margin-top: -5px;">
                模板在线查看
              </k-btn>
            </div>
          </k-form-item>
          <k-form-item label="公告标题" :data-col="2">
            <div>
              <k-field-text v-model="descFormData.title" style="width: 80%;"/>
              <k-btn class="btn-custom-plain" style="margin-top: -5px;">
                按示例预览
              </k-btn>
            </div>
          </k-form-item>
          <k-form-item label=" " :data-col="2">
            <k-grid ref="t8ObjectGrid" :dataData="dataList2">
              <k-grid-column data-header="控件" data-name="control"/>
              <k-grid-column data-header="替换含义" data-name="meaning"/>
              <k-grid-column data-header="控件替换示例" data-name="example"/>
              <template slot="operate" slot-scope="scope" >
                <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                       class="btn-custom-plain" data-descript="引用-复制">
                  引用-复制
                </k-btn>
              </template>
            </k-grid>
          </k-form-item>
          <k-form-item label="公告负责角色" :data-col="2">
            <k-field-select v-model="descFormData.role" style="width: 80%;" :data-data="roleData"
              data-display-field="text" data-value-field="value"/>
          </k-form-item>
          <k-form-item label="信披产品" :data-col="2">
            <div>
              <el-radio v-model="descFormData.prodType" label="1">所有产品</el-radio>
              <br/>
              <el-radio v-model="descFormData.prodType" label="2">产品</el-radio>
              <k-field-select v-model="descFormData.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                              data-display-field="prodCode,prodName" data-value-field="prodCode"
                              :data-multiple="true" style="width: 300px;margin-left: 15px;"
                              :data-disabled="this.descFormData.prodType != 2"/>
              (多选)
              <br/>
              <br/>
              <el-radio v-model="descFormData.prodType" label="3">产品组</el-radio>
              <k-field-select v-model="descFormData.prodCodeGroup" :data-data="prodGroupData"
                  data-display-field="text" data-value-field="value"
                  :data-multiple="true" style="width: 300px;margin-left: 0px;" :data-disabled="this.descFormData.prodType != 3"/>(多选)
                <k-btn class="btn-custom-plain" style="margin-top: -5px;">
                  产品组维护
                </k-btn>
              <br/>
              <el-radio v-model="descFormData.prodType" label="4">与产品无直接关联</el-radio><br/>
            </div>
          </k-form-item>
          <k-form-item label="信披渠道" :data-col="2">
            <k-field-checkbox data-label-width="120px" v-model="descFormData.channel" :data-allowblank="false"
                              :data-data="xpqd" data-display-field="label" data-value-field="value"/>
          </k-form-item>
        </k-form>
        <k-form ref="addForm2" :data-col="2" v-show="this.activeName == 'timeRule'">
          <k-form-item label="信披产品" :data-col="2">
            <div>
              <el-radio v-model="descFormData.selectType" label="1">业务变更发起</el-radio>
              <el-radio v-model="descFormData.selectType" label="2">时间规则发起</el-radio>
              <el-radio v-model="descFormData.selectType" label="3">人工发起</el-radio>
            </div>
          </k-form-item>
          <k-form-item label="业务变更类型" :data-col="2">
            <k-field-tree :data-multiple="false" :dataAllowblank='descFormData.selectType == 2' :data-flat="false" v-model="descFormData.updType" data-diffcondition="deptno,parentdeptno" :data-data="updTypeData"
                          data-display-child="children" data-placeholder="请选择业务变更类型" data-display-field="deptname" style="width: 80%;"
                          data-value-field="deptno" :data-type="false">
            </k-field-tree>
          </k-form-item>
          <k-form-item label="基准日期" :data-col="2">
            <k-field-select v-model="descFormData.baseDate" :data-data="baseDate_dict" style="width: 180px;"
              data-display-field="text" data-value-field="value" data-placeholder=""/>
          </k-form-item>
          <k-form-item label="计划时间设置" :data-col="2">
            <div>
              <div>
                初始公告-生成日期 T
                <k-field-select v-model="descFormData.initCrtDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="descFormData.initCrtDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="descFormData.initCrtDateType" label="1">自然日</el-radio>
                  <el-radio v-model="descFormData.initCrtDateType" label="2">工作日</el-radio>
              </div>
              <div>
                计划-补录完成日期 T
                <k-field-select v-model="descFormData.supplementDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="descFormData.supplementDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="descFormData.supplementDateType" label="1">自然日</el-radio>
                  <el-radio v-model="descFormData.supplementDateType" label="2">工作日</el-radio>
              </div>
              <div>
                计划-审批完成日期 T
                <k-field-select v-model="descFormData.approvalDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="descFormData.approvalDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="descFormData.approvalDateType" label="1">自然日</el-radio>
                  <el-radio v-model="descFormData.approvalDateType" label="2">工作日</el-radio>
              </div>
              <div>
                计划-系统发布日期 T
                <k-field-select v-model="descFormData.releaseDate" :data-data="ys_dict" style="width: 60px;"
                  data-display-field="text" data-value-field="value" data-placeholder=""/>
                  <el-input-number v-model="descFormData.releaseDateNum" controls-position="right" :min="0" :max="50"></el-input-number>
                  <el-radio v-model="descFormData.releaseDateType" label="1">自然日</el-radio>
                  <el-radio v-model="descFormData.releaseDateType" label="2">工作日</el-radio>
              </div>
            </div>
          </k-form-item>
        </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary"
                  :data-handler="saveRule"
                 data-from="addForm" :data-model="descFormData"
                 data-target="t8ObjectGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-popup>

    </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import KFieldRadio from "@/components/k-element/k-field-radio/k-field-radio";
import Tools from "@/utils/tools";

export default {
  name: "M8RuleAdmin",
  data() {
    return {
      activeName: 'baseInfo',
      xpqd:[
        {label:'中国光大银行官网',value:'1'},
        {label:'光大理财官网',value:'2'},
        {label:'光大银行销售渠道',value:'3'},
        {label:'光大理财直销渠道',value:'4'},
        {label:'行外代销机构',value:'5'},
        {label:'专户机构邮箱',value:'6'},
      ],
      ys_dict:[
        {text:'1',value:"-"},
        {text:'2',value:"+"},
      ],
      baseDate_dict:[
        {text:'00',value:"产品成立日"},
        {text:'01',value:"产品到期日"},
        {text:'02',value:"每月第1个工作日"},
        {text:'03',value:"每季度第1个工作日"},
        {text:'06',value:"7月第1个工作日"},
        {text:'07',value:"1月第1个工作日"},
        {text:'09',value:"每年第1个自然日"},
        {text:'99',value:"业务生效日"},
      ],
      modelData:[
        {text:'1',value:"发行公告模板（定期开放类).docx"},
        {text:'2',value:"定期报告-季报.docx"},
        {text:'3',value:"丰利系列净值报告.docx"},
        {text:'4',value:"公司年报.docx"},
      ],
      roleData:[
        {text:'1',value:"投资经理岗"},
        {text:'2',value:"监管报送岗"},
        {text:'3',value:"风险经理岗"},
        {text:'4',value:"交易支持岗"},
        {text:'5',value:"清算结算岗"},
        {text:'6',value:"业务管理员"},
        {text:'7',value:"法审经理岗"},
      ],
      prodGroupData:[
        {text:'1',value:"同利系列净值公告"},
      ],
      updTypeData:[
        {"parentdeptno":"ROOT","deptid":"1","children":[{"parentdeptno":"1","deptid":"100_11","deptno":"11","deptname":"新增投资经理"},{"parentdeptno":"1","deptid":"100_12","deptno":"12","deptname":"投资经理变更"}],"deptno":"1","deptname":"投资经理变更"},
        {"parentdeptno":"ROOT","deptid":"2","children":[{"parentdeptno":"2","deptid":"100_21","deptno":"21","deptname":"停止销售商合作"},{"parentdeptno":"2","deptid":"100_22","deptno":"22","deptname":"新增销售商"}],"deptno":"2","deptname":"销售商管理"}
      ],
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodType:'1',
        selectType:'1',
        channel:''
      },
      descFormData:{
        prodType:'1',
        selectType:'1',
        channel:''
      },
      dataList:{
        rows:[
          {name:'发行公告模板（定期开放类)',type:'2',modelName:'发行公告模板（定期开放类).docx',title:'{ProdName}发行公告',status:'启用'},
          {name:'阳光橙按季定开定期报告',type:'6',modelName:'定期报告-季报.docx',title:'{ProdName}第{season}季度报告',status:'停用'},
          {name:'丰利系列净值公告',type:'4',modelName:'丰利系列净值报告.docx',title:'丰利系列净值报告',status:'停用'},
          {name:'公司年报',type:'10',modelName:'公司年报.docx',title:'xx理财有限责任公司{YYYY}年度报告',status:'停用'},
        ]
      },
      dataList2:{
        rows:[
          {control:'{prodName}',meaning:'产品名称',example:'阳光金15M丰利2期'},
          {control:'{YYYY}',meaning:'年度',example:'2021'},
          {control:'{Half-Year}',meaning:'半年度',example:'上半年'},
          {control:'{Season}',meaning:'季度',example:'一季度'},
          {control:'{Month}',meaning:'月份',example:'2月'},
        ]
      }
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    tableDataDbClick(row, column, event){
      this.descFormData=row;
      this.descFormData.id=1;
      this.descFormData.crtDate='20210326';
      this.descFormData.modelId='发行公告模板（定期开放类).docx';
      this.descFormData.role="业务管理员";
      this.descFormData.prodType="2";
      this.descFormData.selectType="2";
      this.descFormData.prodCode="天利01";
      this.descFormData.channel="1,4,2,6,3";
      this.descFormData.baseDate="产品成立日";

      this.descFormData.initCrtDate="+";
      this.descFormData.initCrtDateNum="1";
      this.descFormData.initCrtDateType="2";
      this.descFormData.supplementDate="+";
      this.descFormData.supplementDateNum="1";
      this.descFormData.supplementDateType="2";
      this.descFormData.approvalDate="+";
      this.descFormData.approvalDateNum="1";
      this.descFormData.approvalDateType="2";
      this.descFormData.releaseDate="+";
      this.descFormData.releaseDateNum="2";
      this.descFormData.releaseDateType="2";
      this.$refs.addDesc.popup();
    },
    openAddPage(){
      this.formData.id='系统自动生成';
      this.formData.crtDate=new Date().toLocaleString();//当前时间
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    saveRule(params){
      Tools.alert("保存成功");
    }
  }
}
</script>

<style scoped>

</style>
