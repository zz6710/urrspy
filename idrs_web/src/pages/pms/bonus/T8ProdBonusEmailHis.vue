<template>
  <div>
    <div>
		<k-form-search-customize v-model="searchParam" data-target="t8ProdBonusTaskGrid">
			<k-form-item label="产品代码">
				<k-field-select v-model="searchParam.prodCode" data-action="T8ProdInfo.getProdInfos"
								data-value-field="prodCode"
								data-display-field="prodCode,prodName"></k-field-select>
			</k-form-item>
			<k-form-item label="发送时间">
				<k-field-date v-model="taskDayRange" data-type="daterange" data-date-format="yyyyMMdd"
							  data-value-format="yyyyMMdd"/>
			</k-form-item>
			<k-form-item label="邮件接收人">
				<k-field-select v-model="searchParam.recipient" data-action="User.getAllUser" data-display-field="label"
								data-value-field="label"></k-field-select>
			</k-form-item>
			<k-form-item label="是否发送成功">
				<k-field-select v-model="searchParam.isSend" data-dict="1yes0no"></k-field-select>
			</k-form-item>
		</k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8ProdBonusEmailHisGrid" @data-row-select="selectRow" data-action="T8ProdBonusEmailHis.findT8ProdBonusEmailHiss" data-operate-column="false">
		<k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
		<k-grid-column data-header="收件人邮箱" data-name="recEmailAddress"></k-grid-column>
		<k-grid-column data-header="收件人" data-name="recipient"></k-grid-column>
		<k-grid-column data-header="是否发送成功" data-name="isSend" data-dict="1yes0no"></k-grid-column>
		  <k-grid-column data-header="失败原因" data-name="reason"></k-grid-column>
		<k-grid-column data-header="发送日期" data-name="sendDate" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="发送时间" data-name="sendTime" data-render="renderDateTimeCreate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改邮件提醒服务" data-functype="POPUP" data-size="mini"
            data-target="editT8ProdBonusEmailHisPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdBonusEmailHis.deleteT8ProdBonusEmailHis" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除邮件提醒服务">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>
    
	<!--    添加邮件提醒服务弹出框   -->
	<k-popup ref="addT8ProdBonusEmailHisPopup" data-title="新增">
    	<k-form ref="addT8ProdBonusEmailHisForm" :data-col="2">
			<k-form-item label="id">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="产品代码">
	        	<k-field-text v-model="formData.prodCode"/>
	     	</k-form-item>
			<k-form-item label="收件人邮箱">
	        	<k-field-text v-model="formData.recEmailAddress"/>
	     	</k-form-item>
			<k-form-item label="收件人">
	        	<k-field-text v-model="formData.recipient"/>
	     	</k-form-item>
			<k-form-item label="是否发送成功">
	        	<k-field-text v-model="formData.isSend"/>
	     	</k-form-item>
			<k-form-item label="发送日期">
	        	<k-field-text v-model="formData.sendDate"/>
	     	</k-form-item>
			<k-form-item label="发送时间">
	        	<k-field-text v-model="formData.sendTime"/>
	     	</k-form-item>
	  	
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdBonusEmailHis.addT8ProdBonusEmailHis" data-from="addT8ProdBonusEmailHisForm"
		               :data-model="formData" data-target="t8ProdBonusEmailHisGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>
    
	<!--    修改邮件提醒服务弹出框   -->
	<k-popup ref="editT8ProdBonusEmailHisPopup" data-title="修改">
	  <k-form ref="editT8ProdBonusEmailHisForm" :data-col="2">
		<k-form-item label="id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.prodCode"/>
     	</k-form-item>
		<k-form-item label="收件人邮箱">
        	<k-field-text v-model="formData.recEmailAddress"/>
     	</k-form-item>
		<k-form-item label="收件人">
        	<k-field-text v-model="formData.recipient"/>
     	</k-form-item>
		<k-form-item label="是否发送成功">
        	<k-field-text v-model="formData.isSend"/>
     	</k-form-item>
		<k-form-item label="发送日期">
        	<k-field-text v-model="formData.sendDate"/>
     	</k-form-item>
		<k-form-item label="发送时间">
        	<k-field-text v-model="formData.sendTime"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdBonusEmailHis.updateT8ProdBonusEmailHis" data-from="editT8ProdBonusEmailHisForm"
	        :data-model="formData" data-target="t8ProdBonusEmailHisGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
  </div>
</template>

<script>
	import Tools from "@/utils/tools";

	export default {
		data() {
      return {
        formData: {},
        selectRowData: {},
		  searchParam:{},
		  taskDayRange:[],
      };
    },
	  watch:{
		  taskDayRange() {
			  console.log(this.taskDayRange);
			  this.$set(this.searchParam, 'startDay', this.taskDayRange == null ? '' : this.taskDayRange[0]);
			  this.$set(this.searchParam, 'endDay', this.taskDayRange == null ? '' : this.taskDayRange[1]);
		  },
	  },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
		renderDateTimeCreate(row) {
			return Tools.formatDateTime(row.sendDate, row.sendTime);
		},
    }
  };
</script>
