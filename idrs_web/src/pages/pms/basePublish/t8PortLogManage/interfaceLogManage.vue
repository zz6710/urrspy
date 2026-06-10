<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="InterfaceLogInfo" v-model="queryParam" data-label-width="65px" data-target="portLogGridList">

        <k-form-item label="接口类型">
          <k-field-select v-model="searchParam.portType" data-dict="interface_type"/>
        </k-form-item>

        <k-form-item label="接口名称">
          <k-field-text v-model="searchParam.portName" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="处理状态">
          <k-field-select v-model="searchParam.fileState" data-dict="port_process_status"/>
        </k-form-item>

        <k-form-item label="数据日期">
          <k-field-date v-model="queryParamDealDateRange" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item>

        <k-form-item label="同步日期">
          <k-field-date v-model="queryParamCrtDateRange" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" />
        </k-form-item>
       
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-target="portLogGridList"
               :data-export-name="'接口日志信息'">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
        </div>
      </div>
      <k-grid ref="portLogGridList" @data-row-select="selectRow" data-action="PortLogInfoModel.findPortLogInformation"
              @init="(grid)=>{this.$kgrid = grid}" data-checkbox-id="id">
        <k-grid-column data-align="center" data-header="id"     data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据日期" data-name="dealDate"></k-grid-column>
        <k-grid-column data-align="center" data-header="接口类型" data-name="portType" data-dict="interface_type"></k-grid-column>
        <k-grid-column data-align="center" data-header="接口名称" data-name="portName"></k-grid-column>
        <k-grid-column data-align="center" data-header="处理状态" data-name="fileState" data-dict="port_process_status"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据量"  data-name="totalNum"></k-grid-column>
        <k-grid-column data-align="center" data-header="日志信息"  data-name="execMessage"></k-grid-column>
        <k-grid-column data-align="center" data-header="同步日期" data-name="crtDate" data-render="dateTransfer"></k-grid-column>
        <k-grid-column data-align="center" data-header="开始时间" data-name="crtTime" data-render="timeTransferStart"></k-grid-column>
        <k-grid-column data-align="center" data-header="结束时间" data-name="updTime" data-render="timeTransferEnd"></k-grid-column>
        <k-grid-column data-align="center" data-header="处理结果" data-name="execMessage" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="接口方向" data-name="portDir" data-dict="interface_dir" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="接口代码" data-name="portCode" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="开始日期" data-name="crtDate" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="结束日期" data-name="updDate" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="处理人" data-name="userName"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP"  data-size="mini" class="btn-custom-text"
                 data-descript="详情"
                 data-target="portLogDetailPopup">
            详情
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    接口日志详情弹出框   -->
    <k-popup ref="portLogDetailPopup" data-title="详情">
      <k-form ref="portLogDetailForm" :data-col="2">

        <k-form-item label="id">
          <k-field-display v-model="portLogInfoData.id"/>
        </k-form-item>

        <k-form-item label="接口代码">
          <k-field-display v-model="portLogInfoData.portCode" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="接口名称">
          <k-field-display v-model="portLogInfoData.portName" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="接口类型">
          <k-field-display v-model="portLogInfoData.portType" data-dict="interface_type"/>
        </k-form-item>

        <k-form-item label="接口方向">
          <k-field-display v-model="portLogInfoData.portDir" data-dict="interface_dir"/>
        </k-form-item>

        <k-form-item label="处理日期">
          <k-field-display v-model="portLogInfoData.dealDate" data-validate-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>

        <k-form-item label="处理状态">
          <k-field-display v-model="portLogInfoData.fileState" data-dict="port_process_status"/>
        </k-form-item>

        <k-form-item label="处理结果">
          <k-field-display v-model="portLogInfoData.execMessage" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="开始日期">
          <k-field-display v-model="portLogInfoData.crtDate" data-type="date" />
        </k-form-item>

        <k-form-item label="开始时间">
          <k-field-display v-model="portLogInfoData.crtTime" data-validate-type="text"/>
        </k-form-item>

        <k-form-item label="结束日期">
          <k-field-display v-model="portLogInfoData.updDate" data-type="date"/>
        </k-form-item>

        <k-form-item label="结束时间">
          <k-field-display v-model="portLogInfoData.updTime" data-validate-type="text"/>
        </k-form-item>

      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name:"InterfaceLogInfo",
    data() {
      return {
        $kgrid: null,
        selectRowData: {},
        searchParam:{},
        portLogInfoData:{},
        queryParamDealDateRange: [],
        queryParamCrtDateRange: [],
      };
    },
    computed: {
      queryParam () {
        return {
          'startDealDate': this.queryParamDealDateRange ? this.queryParamDealDateRange[0] : null,
          'endDealDate': this.queryParamDealDateRange ? this.queryParamDealDateRange[1] : null,
          'startCrtDate': this.queryParamCrtDateRange ? this.queryParamCrtDateRange[0] : null,
          'endCrtDate': this.queryParamCrtDateRange ? this.queryParamCrtDateRange[1] : null,
          'portType': this.searchParam.portType,
          'portName': this.searchParam.portName,
          'fileState': this.searchParam.fileState,
        }
      }
    },
    created() {
      if(this.$route.query!=null && this.$route.query !== '' && this.$route.query!==undefined){
        let portType=this.$route.query.portType;
        let portName=this.$route.query.portName;
        let fileState=this.$route.query.fileState;
        let dealDate=this.$route.query.dealDate;
        this.$set(this.searchParam,"portType",portType);
        this.$set(this.searchParam,"portName",portName);
        this.$set(this.searchParam,"fileState",fileState);
        this.$set(this.searchParam,"startDealDate",dealDate);
        this.$set(this.searchParam,"endDealDate",dealDate);
        if(dealDate!==undefined&&dealDate!==null&&dealDate!==''){
          this.queryParamDealDateRange[0]=dealDate;
          this.queryParamDealDateRange[1]=dealDate;
        }
        this.$nextTick(() => {
          this.$refs.portLogGridList.load(this.searchParam);
        });
      }
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.portLogInfoData = Object.assign({}, row);
      },
      dateTransfer(row) {
        return Tools.formatDate(row.crtDate);
      },
      timeTransferStart(row) {
        return Tools.formatTimeLong(row.crtTime);
      },
      timeTransferEnd(row) {
        return Tools.formatTimeLong(row.updTime);
      },
     }
  };
</script>
