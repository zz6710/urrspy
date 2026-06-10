<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8DisclosureNoticeChannelGrid" v-model="prodSearchParam">
        <k-form-item label="产品代码" v-show="true" data-input-width="194px" data-label-width="150px">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" />
        </k-form-item>
        <k-form-item label="产品名称" data-input-width="194px" data-label-width="150px">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="发布渠道" v-show="true" data-input-width="194px" data-label-width="150px">
          <k-field-select v-model="prodSearchParam.disclosureNoticeChannelId" data-action="T8Dict.findT8ChannelInfos"
                          data-display-field="id,channelName" data-value-field="id" />
        </k-form-item>
        <k-form-item label="信披类型" data-input-width="194px" data-label-width="150px">
          <k-field-select v-model="prodSearchParam.disclosureType" data-dict="xp_doc_type"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" data-input-width="194px" data-label-width="150px">
          <k-field-select v-model="prodSearchParam.disclosureSonType" data-dict="xp_son_type"></k-field-select>
        </k-form-item>
        <k-form-item label="发布状态" v-show="true" data-input-width="194px" data-label-width="150px">
          <k-field-select v-model="prodSearchParam.noticeChannelPublicStatus" data-dict="xp_release_status" />
        </k-form-item>
        <k-form-item label="基准日期" data-input-width="194px" data-label-width="150px">
          <k-field-date v-model="prodSearchParam.prodBaseDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="发布日期区间"  data-input-width="194px" data-label-width="150px">
          <k-field-date v-model="channelPublicDateRange" data-type="daterange"/>
        </k-form-item>
        <k-btn slot="button" style="width: 120px" class="md-rose" data-functype="EXPORT" data-target="t8DisclosureNoticeChannelGrid"
               :data-export-name="'渠道发布记录'">
          <md-icon>cloud_download</md-icon>
          导出数据
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8DisclosureNoticeChannelGrid" @data-row-select="selectRow" data-action="T8DisclosureNoticeChannel.findT8DisclosureNoticeChannels" data-operate-column="false">
		<k-grid-column data-header="主键id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="公告表id" data-name="disclosureNoticeId" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-header="产品代码" data-name="prodCode" ></k-grid-column>
        <k-grid-column data-header="产品名称" data-name="prodName" ></k-grid-column>
        <k-grid-column data-header="公告标题" data-name="noticeTitle" data-width="300"></k-grid-column>
        <k-grid-column data-header="文档版本" data-name="docVersion" ></k-grid-column>
        <k-grid-column data-header="发布渠道" data-name="channelName" ></k-grid-column>
        <k-grid-column data-header="基准日期" data-name="prodBaseDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" ></k-grid-column>
        <k-grid-column data-align="center" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" ></k-grid-column>
		<k-grid-column data-header="公告相关渠道id" data-name="disclosureNoticeChannelId" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="发布状态" data-name="noticeChannelPublicStatus" data-dict="xp_release_status"></k-grid-column>
		<k-grid-column data-header="发布日期" data-name="channelPublicDate" data-type="date" data-width="100"></k-grid-column>
		<k-grid-column data-header="创建日期" data-name="createDate" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="创建时间" data-name="createTime" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-header="更新日期" data-name="crtDate" data-hidden="true" data-align="center" data-render="renderDateTimeCreate" data-export="false"></k-grid-column>
		<k-grid-column data-header="更新日期" data-name="updateDate" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="更新时间" data-name="updateTime" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="创建人id" data-name="createUserId" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="更新人id" data-name="updateUserId" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="创建人姓名" data-name="createUserName" data-hidden="true" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改公告渠道信息表" data-functype="POPUP" data-size="mini"
            data-target="editT8DisclosureNoticeChannelPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8DisclosureNoticeChannel.deleteT8DisclosureNoticeChannel" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除公告渠道信息表">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加公告渠道信息表弹出框   -->
	<k-popup ref="addT8DisclosureNoticeChannelPopup" data-title="新增">
    	<k-form ref="addT8DisclosureNoticeChannelForm" :data-col="2">
			<k-form-item label="主键id">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="公告表id">
	        	<k-field-text v-model="formData.disclosureNoticeId"/>
	     	</k-form-item>
			<k-form-item label="公告相关渠道id">
	        	<k-field-text v-model="formData.disclosureNoticeChannelId"/>
	     	</k-form-item>
			<k-form-item label="发布状态">
	        	<k-field-text v-model="formData.noticeChannelPublicStatus"/>
	     	</k-form-item>
			<k-form-item label="发布时间">
	        	<k-field-text v-model="formData.channelPublicDate"/>
	     	</k-form-item>
			<k-form-item label="创建日期">
	        	<k-field-text v-model="formData.createDate"/>
	     	</k-form-item>
			<k-form-item label="创建时间">
	        	<k-field-text v-model="formData.createTime"/>
	     	</k-form-item>
			<k-form-item label="更新日期">
	        	<k-field-text v-model="formData.updateDate"/>
	     	</k-form-item>
			<k-form-item label="更新时间">
	        	<k-field-text v-model="formData.updateTime"/>
	     	</k-form-item>
			<k-form-item label="创建人id">
	        	<k-field-text v-model="formData.createUserId"/>
	     	</k-form-item>
			<k-form-item label="更新人id">
	        	<k-field-text v-model="formData.updateUserId"/>
	     	</k-form-item>
			<k-form-item label="创建人姓名">
	        	<k-field-text v-model="formData.createUserName"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8DisclosureNoticeChannel.addT8DisclosureNoticeChannel" data-from="addT8DisclosureNoticeChannelForm"
		               :data-model="formData" data-target="t8DisclosureNoticeChannelGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改公告渠道信息表弹出框   -->
	<k-popup ref="editT8DisclosureNoticeChannelPopup" data-title="修改">
	  <k-form ref="editT8DisclosureNoticeChannelForm" :data-col="2">
		<k-form-item label="主键id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="公告表id">
        	<k-field-text v-model="formData.disclosureNoticeId"/>
     	</k-form-item>
		<k-form-item label="公告相关渠道id">
        	<k-field-text v-model="formData.disclosureNoticeChannelId"/>
     	</k-form-item>
		<k-form-item label="发布状态">
        	<k-field-text v-model="formData.noticeChannelPublicStatus"/>
     	</k-form-item>
		<k-form-item label="发布时间">
        	<k-field-text v-model="formData.channelPublicDate"/>
     	</k-form-item>
		<k-form-item label="创建日期">
        	<k-field-text v-model="formData.createDate"/>
     	</k-form-item>
		<k-form-item label="创建时间">
        	<k-field-text v-model="formData.createTime"/>
     	</k-form-item>
		<k-form-item label="更新日期">
        	<k-field-text v-model="formData.updateDate"/>
     	</k-form-item>
		<k-form-item label="更新时间">
        	<k-field-text v-model="formData.updateTime"/>
     	</k-form-item>
		<k-form-item label="创建人id">
        	<k-field-text v-model="formData.createUserId"/>
     	</k-form-item>
		<k-form-item label="更新人id">
        	<k-field-text v-model="formData.updateUserId"/>
     	</k-form-item>
		<k-form-item label="创建人姓名">
        	<k-field-text v-model="formData.createUserName"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8DisclosureNoticeChannel.updateT8DisclosureNoticeChannel" data-from="editT8DisclosureNoticeChannelForm"
	        :data-model="formData" data-target="t8DisclosureNoticeChannelGrid">
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
  import {assign} from "lodash";
  export default {
    name:"T8DisclosureNoticeChannel",
    data() {
      return {
        prodSearchParam: {
          prodCode:'',
          prodName:'',
          disclosureNoticeChannelId:'',
          noticeChannelPublicStatus:'',
          disclosureType: '',
          disclosureSonType:'',
          prodBaseDate:'',
          publicDateStart:'',
          publicDateEnd:'',
        },
        channelPublicDateRange:[],
        formData: {},
        selectRowData: {}
      };
    },
    watch: {
      channelPublicDateRange() {
        console.log("this.queryParamDateRange=:>",this.channelPublicDateRange);
        if(this.channelPublicDateRange!=null&&this.channelPublicDateRange!=''&&this.channelPublicDateRange!=undefined){
          this.$set(this.prodSearchParam, 'publicDateStart', this.channelPublicDateRange[0]);
          this.$set(this.prodSearchParam, 'publicDateEnd', this.channelPublicDateRange[1]);
        }else{
          this.$set(this.prodSearchParam, 'publicDateStart', null);
          this.$set(this.prodSearchParam, 'publicDateEnd', null);
        }
      }
    },
    methods: {
      renderDateTimeCreate(row) {
        return Tools.formatDateTime(row.updateDate, row.updateTime);
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    computed: {
      queryParam() {
        return {
          'disclosureNoticeChannelId':this.prodSearchParam.disclosureNoticeChannelId,
          'prodCode':this.prodSearchParam.prodCode,
          'prodName':this.prodSearchParam.prodName,
          'noticeChannelPublicStatus':this.prodSearchParam.noticeChannelPublicStatus,
          'disclosureType': this.prodSearchParam.disclosureType,
          'disclosureSonType': this.prodSearchParam.disclosureSonType,
          'prodBaseDate': this.prodSearchParam.prodBaseDate,
        }
      }
    },
  };
</script>
