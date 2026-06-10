<template>
	<div>
		<div>
			<k-form-search-customize data-target="t8ProdQuotaGrid" v-model="prodSearchParam">

        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="查询起始日">
          <k-field-date v-model="prodSearchParam.startEstablishDate" data-type="date" :data-max-value="prodSearchParam.endEstablishDate===''?'29991230':prodSearchParam.endEstablishDate"/>
        </k-form-item>
        <k-form-item label="查询结束日">
          <k-field-date v-model="prodSearchParam.endEstablishDate"  data-type="date"
                        :data-min-value="prodSearchParam.startEstablishDate===''?'19700101':prodSearchParam.startEstablishDate"/>
        </k-form-item>
				<k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="openBox" data-target="addT8ProdQuotaPopup" v-show="showCreate"
               v-if="global.isShowAuthorityButton('T8ProdQuota.addT8ProdQuota')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
			</k-form-search-customize>
		</div>
		<div>
      <k-grid ref="t8ProdQuotaGrid" @data-row-select="selectStaticTemp" data-action="T8ProdQuota.findT8ProdQuotas1">
        <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
        <k-grid-column data-header="成立/开放日" data-name="quotaDate" data-type="date"></k-grid-column>
        <!--<k-grid-column data-header="预期募集规模" data-name="expectedRaiseFund"></k-grid-column> -->
        <k-grid-column data-header="总额度(亿)" data-name="totalSaleQuota" data-type="money"></k-grid-column>
        <k-grid-column data-header="状态" data-name="confirmStatus" data-dict="confirm_status"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUserName"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime" data-type="time"></k-grid-column>
        <template slot="operate" slot-scope="scope">
					<k-btn class="md-info md-just-icon md-simple"
                 data-descript="修改销售额度"
                 data-functype="POPUP"
                 data-size="mini" v-if="global.isShowAuthorityButton('T8ProdQuota.updateT8ProdQuota')"
                 data-target="editT8ProdQuotaPopup" v-show="showUpdate" :data-disabled="scope.row.row.confirmStatus>=1">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple"
                 data-descript="新增销售商额度"
                 data-functype="POPUP"
                 data-size="mini" v-if="global.isShowAuthorityButton('T8ProdQuota.addDistributorQuotas')"
                 data-target="addSaleQuotaPopup"
                 :data-handler="openSalesBox" v-show="showCreateSale">
            <md-icon>add</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple"
                 data-functype="SUBMIT" v-if="global.isShowAuthorityButton('T8ProdQuota.deleteProdAndDistriQuota')"
                 data-action="T8ProdQuota.deleteProdAndDistriQuota"
                 data-size="mini"
                 data-type="danger"
                 data-target="t8ProdQuotaGrid"
                 :data-confirm="true"
                 data-descript="删除产品额度" v-show="showDelete" :data-disabled="scope.row.row.confirmStatus>=1">
            <md-icon>close</md-icon>
          </k-btn>
				</template>
			</k-grid>
		</div>
		<k-grid ref="t8DistributorQuotaManageGrid" @data-row-select="selectRow" data-action="T8DistributorQuotaManage.findTotalDeptQuota">
			<k-grid-column data-header="销售部门" data-name="managerDept" data-dict="manager_dept"></k-grid-column>
			<k-grid-column data-header="申请额度(亿)" data-name="totalDeptQuota" data-type="money"></k-grid-column>
			<template slot="operate" slot-scope="scope">
        <k-btn
          class="md-info md-just-icon md-simple"
          data-descript="查看销售商额度详情"
          data-functype="POPUP"
          data-size="mini"
          data-target="salesQuotaDetailPopup"
        >
          <md-icon>zoom_in</md-icon>
        </k-btn>
        <k-btn
          class="md-info md-just-icon md-simple"
          data-descript="调整"
          data-functype="POPUP"
          data-size="mini" v-if="global.isShowAuthorityButton('T8ProdQuota.updateDidtributorQuota')"
          data-target="editDistriQuotaPopup"
          :data-handler="queryQuota"
          v-show="showAdjust"
        >
          <md-icon>edit</md-icon>
        </k-btn>
			</template>
		</k-grid>
    <!--销售商额度需求列表详情 -->
    <k-popup ref="salesQuotaDetailPopup" data-title="部门销售商额度需求信息" @data-opened="refreshSalesGrid"
             :data-dialog-drag="true" data-width="60%">
      <k-grid ref="t8SalesQuotaDetailGrid" data-action="T8DistributorQuotaManage.findQuotaListByDeptNo"
              data-operate-column="false" :data-autoload="false">
        <k-grid-column data-header="销售商名称" data-name="distributorName"></k-grid-column>
        <k-grid-column data-header="所属部门" data-name="managerDept" data-dict="manager_dept"></k-grid-column>
        <k-grid-column data-header="申请额度(亿)" data-name="quota" data-type="money"></k-grid-column>
        <k-grid-column data-header="确认状态" data-name="confirmStatus" data-dict="confirm_status"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUserName"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime"></k-grid-column>
      </k-grid>
    </k-popup>
    <!--新增销售商额度需求弹出框-->
    <k-popup ref="addSaleQuotaPopup" data-title="新增销售商额度" data-width="65%" :data-dialog-drag="true">
      <k-form ref="addT8ProdQuotaForm" :data-col="4" data-input-width="160px" data-label-width="110px" data-total-width="1100px">
        <k-form-item label="产品代码">

          <k-field-select
            data-action="T8Dict.findNotEstablishProdInfos"
            data-display-field="prodCode,prodName"
            v-model="formData.prodCode"
            data-multiple="false"
            data-value-field="prodCode"
            :data-disabled="true"
          ></k-field-select>
        </k-form-item>
        <k-form-item label="成立/开放日">
          <k-field-select v-model="formData.quotaDate" :data-disabled="true"/>
        </k-form-item>
        <!--
				<k-form-item label="预期募集规模">
					<k-field-text v-model="formData.expectedRaiseFund" :data-allowblank="false" />
				</k-form-item>
				-->
        <k-form-item label="销售总额度(亿)">
          <k-field-text v-model="formData.totalSaleQuota" :data-disabled="true"/>
        </k-form-item>
      </k-form>
      <k-form ref="addForm1"
              v-for="(item, index) in salesItems"
              :key="index"
              :data-col="5"
              data-input-width="140px"
              data-label-width="110px"
              data-total-width="1200px"
      >
        <k-form-item label="销售商名称">
          <!--
           data-action="T82001.findDistributorDict"
           :data-params="{managerDept:item.managerDept}"-->
          <k-field-select
            ref="distributor"
            v-model="item.distributorCode"
            :data-params="{prodCode:formData.prodCode}"
            :data-multiple="false"
            data-value-field="distributorCode"
            data-display-field="distributorName"
            :data-allowblank="false"
            data-action="T82001.findDistributorByCode"
            @data-on-change="changeDistributor(salesItems,item.distributorCode,index)"
          ></k-field-select>

        </k-form-item>
        <k-form-item label="所属部门">
          <k-field-select v-model="item.managerDept" ref="deptSelect"
                          data-action="T8DistributorQuotaManage.findDeptByDisCode"
                          data-display-field="managerDeptName"
                          data-value-field="managerDept"
                          :dataAllowblank="false"
                          :data-multiple="false"
          ></k-field-select>
        </k-form-item>
        <k-form-item label="申请额度(亿)">
          <k-field-text v-model="item.quota" :data-allowblank="false"
                        data-validate-type="money" data-min-value="(0" data-max-value="999999.99"/>
        </k-form-item>

        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增"
               @click="() => salesItems.push({})">
          <md-icon>add</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行"
               @click="deleteEvent(index)">
          <md-icon md-src="/static/svg/delete.svg" />
        </k-btn>
      </k-form>
      <div style="margin: 0 auto; width: 255px">
        <k-btn
          class="btn-custom-primary"
          data-functype="SUBMIT"
          :data-handler="salesSubmitHandle"
          data-action="T8DistributorQuotaManage.addDistributorQuotas"
          data-target="t8ProdQuotaGrid"
          :data-model="formData"
        >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
    </k-popup>
    <!--修改销售商额度需求 -->
    <k-popup ref="editDistriQuotaPopup" :data-dialog-drag="true" data-width="60%">
      <k-form ref="editDistriQuotaForm" :data-col="4" data-input-width="160px" data-label-width="110px" data-total-width="1100px">
        <k-form-item label="产品代码">
          <k-field-select
            v-model="formData.prodCode"
            data-multiple="false"
            data-action="T8ProdInfo.getProdInfos"
            data-value-field="prodCode"
            data-display-field="prodCode"
            data-disabled="true"
          ></k-field-select>
        </k-form-item>
        <k-form-item label="产品成立日">
          <k-field-date v-model="formData.quotaDate" data-disabled="true"/>
        </k-form-item>
        <!--
				<k-form-item label="预期募集规模">
					<k-field-text v-model="formData.expectedRaiseFund" />
				</k-form-item>-->
        <k-form-item label="销售总额度(亿)">
          <k-field-text v-model="formData.totalSaleQuota" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="剩余额度(亿)">
          <k-field-text v-model="formData.remainQuota" data-disabled="true"/>
        </k-form-item>
        <k-form-item label="部门名称">
          <k-field-select v-model="formData.managerDept"  data-dict="manager_dept" data-disabled="true"></k-field-select>
        </k-form-item>
        <k-form-item label="部门总额度(亿)">
          <k-field-text v-model="formData.deptTotalQuota" data-disabled="true"></k-field-text>
        </k-form-item>
      </k-form>
      <k-form
        ref="addForm3"
        v-for="(item, index) in editEnvItems"
        :key="index"
        :data-col="5"
        data-input-width="120px"
        data-label-width="110px"
        data-total-width="1200px"
      >
        <k-form-item label="销售商名称">
          <k-field-select
            ref="distributor"
            v-model="item.distributorCode"
            data-multiple="false"
            data-value-field="distributorCode"
            data-display-field="distributorName"
            :data-allowblank="false"
            data-action="T82001.findDistributorByCode"
            :data-params="{prodCode:formData.prodCode}"
            @data-on-change="changeDistributor(editEnvItems,item.distributorCode,index)"
            :data-disabled="item.confirmStatus=='1'"

          ></k-field-select>

        </k-form-item>
        <k-form-item label="所属部门">
          <k-field-select v-model="item.managerDept" ref="deptSelect"
                          data-action="T8DistributorQuotaManage.findDeptByDisCode"
                          data-display-field="managerDeptName"
                          :data-params="{distributorCode:item.distributorCode}"
                          data-value-field="managerDept"
                          :dataAllowblank="false"
                          :data-disabled="item.confirmStatus=='1'"
          ></k-field-select>
        </k-form-item>
        <k-form-item label="申请额度(亿)">
          <k-field-text v-model="item.quota" :data-allowblank="false" :data-disabled="item.confirmStatus=='1'"
                        data-validate-type="money" data-min-value="(0" data-max-value="999999.99"/>
        </k-form-item>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增"
               @click="() => editEnvItems.push({})">
          <md-icon>add</md-icon>
        </k-btn>
        <template v-if="item.confirmStatus==0||item.confirmStatus==undefined">
          <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行"
                 @click="deleteEventUpdate(index)">
            <md-icon md-src="/static/svg/delete.svg"/>
          </k-btn>
        </template>
      </k-form>
      <div style="margin: 0 auto; width: 255px">
        <k-btn
          class="btn-custom-primary"
          data-functype="SUBMIT"
          :data-handler="editSubmitHandle"
          data-action="T8DistributorQuotaManage.updateDidtributorQuota"
          data-target="t8DistributorQuotaManageGrid"
          :data-model="formData"
        >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
    </k-popup>
		<!--    添加产品额度弹出框   -->
    <k-popup ref="addT8ProdQuotaPopup" data-title="新增" :data-dialog-drag="true">
      <k-form ref="addT8ProdQuotaForm" :data-col="2" data-input-width="160px" data-label-width="100px">
        <k-form-item label="产品代码">

          <k-field-select
            data-action="T8Dict.findTaProdInfos"
            data-display-field="prodCode,prodName"
            v-model="formData.prodCode"
            data-multiple="false"
            data-value-field="prodCode"
            :data-allowblank="false"
            @data-on-change="changeData(formData.prodCode)"
          ></k-field-select>
        </k-form-item>
        <k-form-item label="成立/开放日">
          <k-field-select
            :data-allowblank="false"
            v-model="formData.quotaDate"
            data-action="T8ProdWorkdays.findProdOpenDays"
            ref="openDays"
            data-display-field="changeDate"
            data-value-field="changeDate"
          />
        </k-form-item>
        <!--
				<k-form-item label="预期募集规模">
					<k-field-text v-model="formData.expectedRaiseFund" :data-allowblank="false" />
				</k-form-item>
				-->
        <k-form-item label="总额度(亿)">
          <k-field-text v-model="formData.totalSaleQuota" :data-allowblank="false"
                        data-validate-type="money" data-min-value="(0" data-max-value="999999.99"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="T8ProdQuota.addT8ProdQuota"
            data-target="t8ProdQuotaGrid"
            :data-model="formData"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改产品额度弹出框   -->
    <k-popup ref="editT8ProdQuotaPopup" data-title="修改" :data-dialog-drag="true">
      <k-form ref="editT8ProdQuotaForm" :data-col="2" data-input-width="160px" data-label-width="100px">
        <k-form-item label="产品代码">
          <k-field-select
            v-model="formData.prodCode"
            data-multiple="false"
            data-action="T8ProdInfo.getProdInfos"
            data-value-field="prodCode"
            data-display-field="prodName"
            :data-allowblank="false"
            @data-on-change="changeData(formData.prodCode)"
            data-disabled="true"
          ></k-field-select>
        </k-form-item>
        <k-form-item label="成立/开放日">
          <k-field-select
            v-model="formData.quotaDate"
            :data-params="{prodCode:formData.prodCode}"
            data-action="T8ProdWorkdays.findProdOpenDays"
            ref="openDays"
            :data-allowblank="false"
            data-display-field="changeDate"
            data-value-field="changeDate"
          />
        </k-form-item>
        <!--
				<k-form-item label="预期募集规模">
					<k-field-text v-model="formData.expectedRaiseFund" />
				</k-form-item>-->
        <k-form-item label="总额度(亿)">
          <k-field-text v-model="formData.totalSaleQuota" :data-allowblank="false"
                        data-validate-type="money" data-min-value="(0" data-max-value="999999.99"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="T8ProdQuota.updateT8ProdQuota"
            data-target="t8ProdQuotaGrid"
            :data-model="formData"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools";
export default {
  name: "M82002",
  data() {
    return {
      //查询条件
      prodSearchParam: {
        prodCode: '',
        startEstablishDate: '',
        endEstablishDate: ''
      },
      formData: {},
			selectRowData: {},
			envItems: [], //新增总额度与销售商额度时的销售商额度需求数据
      id: '',
      editEnvItems: [],//修改销售商额度时的销售商额度需求数据
      salesItems: [], //新增销售商额度时的销售商额度需求数据
      managerDept: '', //所属部门
      totalQuotaId: '',//总额度id
      showCreate: true,//是否显示新增按钮
      showUpdate: true,//是否显示修改按钮
      showCreateSale: true,//是否显示新增销售商额度按钮
      showDelete: true,//是否显示删除总额度按钮
      showAdjust: true,//是否显示调整部门销售额度按钮
      deptList: [],//所属部门
    };
	},
  created() {
    this.global.getProdUser('');
    this.$nextTick(()=>{
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
      let prodCode = this.$route.query.prod_code;
      if(prodCode !=''&&prodCode!=undefined){
        this.$refs.t8ProdQuotaGrid.load({prodCode:prodCode});
      }
    });
  },
  methods: {
    //刷新部门销售商详情列表
    refreshSalesGrid(){
      this.$refs.t8SalesQuotaDetailGrid.load({managerDept:this.managerDept,totalQuotaId:this.totalQuotaId});
    },
    //二级查询记录被选中事件
		selectRow(row) {
      const _this = this;
			_this.selectRowData = Object.assign({}, row);
			this.managerDept = _this.selectRowData.managerDept;
			this.totalQuotaId = _this.selectRowData.totalQuotaId
      this.$set(this.formData,"managerDept",this.managerDept);
			//查询部门总额度
      this.findDeptTotalQuota(this.totalQuotaId,this.managerDept);
		},
    //根据总额度id与部门id查询部门总额度
    findDeptTotalQuota(id,deptNo){
      this.httpUtil.comnQuery({
        action: 'T8DistributorQuotaManage.findDeptTotalQuota',
        params: {
          managerDept: deptNo,
          totalQuotaId:id
        }
      }).then(data => {
        if(data.rows.length> 0 ){
          this.$set(this.formData,"deptTotalQuota",data.rows[0].deptTotalQuota)
        }
      });
    },
    //新增功能删除行事件
		deleteEvent(index) {
			if (this.salesItems.length > 1) {
				this.salesItems.splice(index, 1);
			}
		},
    //修改功能删除行事件
    deleteEventUpdate(index) {
      if (this.editEnvItems.length > 1) {
        this.editEnvItems.splice(index, 1);
      }
    },
		openBox() {
      this.formData = {};
			this.envItems = [{}];
		},
    openSalesBox(){
      this.salesItems=[{}];
    },
    //修改销售商额度需求提交操作
    editSubmitHandle(value){
      //验证数组中每个对象必填项是否已填
      let result = true;
      let disQuotaForm = this.$refs.addForm3;
      if(disQuotaForm && disQuotaForm.length>0){
        for(let i=0;i<disQuotaForm.length;i++){
          result = disQuotaForm[i].validate();
          if(result === false){
            break;
          }
        }
      }
      if(result === false){
        return false;
      }
      if (this.editEnvItems && this.editEnvItems.length > 0) {
        value.json = JSON.stringify({ envItemsConf: this.editEnvItems });
      }
    },
    //新增销售商额度需求提交操作
    salesSubmitHandle(value){
      //验证数组中每个对象必填项是否已填
      let result = true;
      let disQuotaForm = this.$refs.addForm1;
      if(disQuotaForm && disQuotaForm.length>0){
        for(let i=0;i<disQuotaForm.length;i++){
          result = disQuotaForm[i].validate();
          if(result === false){
            break;
          }
        }
      }
      if(result === false){
        return false;
      }
      //检查销售商部门
      if(this.formData.managerDept == ""){
        Tools.alert("请选择销售商部门！","danger");
        return false;
      }
      if (this.salesItems && this.salesItems.length > 0) {
        value.json = JSON.stringify({ envItemsConf: this.salesItems });
      }
    },
    //一级查询选中
		selectStaticTemp(row) {
			const _this = this;
			_this.selectRowData = assign({}, row);
			_this.formData = assign({}, row);
			this.$refs.t8DistributorQuotaManageGrid.load({totalQuotaId:_this.selectRowData.id});
			//查询剩余额度
      this.getRemainQuota(_this.formData.id);
		},
    //根据id查询剩余额度
    getRemainQuota(id) {
      this.httpUtil.comnQuery({
        action: 'T8ProdQuota.findProdRemainQuotas',
        params: {
          id: id,
        }
      }).then(data => {
        if(data.rows.length> 0 ){
          this.$set(this.formData,"remainQuota",data.rows[0].remainQuota)
        }
      });
    },
		changeData(value) {
      this.$set(this.formData, "quotaDate", '');
      if (value == null || value == "") {
        return;
      }
      //刷新开放日列表
      this.$refs.openDays.load({prodCode: value});
    },
    //销售商改变时，查询销售商所属部门  item：数组，用于区分改变哪个数组中的值 value销售商代码  index 数组下标
		changeDistributor(item,value,index) {
      this.$refs.deptSelect[index].load({distributorCode: value});
    },
    //根据总额度id与部门编号查询各销售商额度
    queryQuota(value){
      this.httpUtil
        .comnQuery({
          action: "T8DistributorQuotaManage.findT8DisQuotaByTotalIdAndDeptNo",
          params: {
            totalQuotaId: value.totalQuotaId,
            managerDept:value.managerDept
          },
        })
        .then((data) => {
          this.$nextTick(() => {
            this.editEnvItems = data.rows;
          });
        });
    },
	},
};
</script>
