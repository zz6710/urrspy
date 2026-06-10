
<template>
  <div class="py-page">
  <div class="py-page-container">
    <div>

      <k-form ref="searchForm" :data-col="2" :data-model="searchData">
        <k-form-item  label="表名">
          <k-field-select v-model="searchData.tableName" data-action="DownLoadFileInfo.findTableName" :dataRemote="true"
                          data-display-field="tableComment,tableName" data-value-field="tableName" @data-on-change="initColmnNameDict"/>
        </k-form-item>
        <k-btn class="md-info md-just-icon md-simple" data-descript="增加" :data-handler="addSearchColumn"
               data-type="danger" data-size="mini"
               data-target="GridF">
          <md-icon>add</md-icon>
        </k-btn>
      </k-form>

      <k-form ref="GridF" :data-col="4" :data-model="searchData1">
        <template v-for="(item, index) in searchColumn" >
          <div :key="index" style="display: flex;">
            <k-form-item label="表字段" >
              <k-field-select v-model="item.nameF" :data-data="columnNameDict"
                              data-display-field="columnComment,columnName" data-value-field="columnName"/>
            </k-form-item>
            <k-form-item label="符号">
              <k-field-select v-model="item.symbolF" data-dict="symbol" />
            </k-form-item>
            <k-form-item label="值">
              <k-field-text v-model="item.valueF" />
            </k-form-item>

              <k-btn class="md-info md-just-icon md-simple" data-descript="删除" @click="deleteSearchColumn(item,index)"
                  data-type="danger" data-size="mini"
                  data-target="searchForm">
              <md-icon>close</md-icon>
            </k-btn>
          </div>
        </template>
      </k-form>

      <k-form ref="limitForm" :data-col="2" :data-model="searchData2">
        <k-form-item label="起始行">
          <k-field-text v-model="searchData2.offset" data-validate-type="number" data-min-value="0"/>
        </k-form-item>
        <k-form-item label="条数">
          <k-field-text v-model="searchData2.limit"/>
        </k-form-item>
      </k-form>

    </div>
    <div class="k-form-search-footer" style="width:100%;text-align: center;">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="batchDownloadButton" data-from="searchForm" :data-handler="downLoadFile">
        <md-icon>cloud_download</md-icon>
        下载
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="RESET" data-type="warning" @click="reset" >
        <md-icon md-src="/static/svg/reset.svg"></md-icon>
        重置
      </k-btn>
      <slot></slot>
    </div>
  </div>
</div>
</template>

<script>
  import NonStandInfoModelCollection from "@/pages/pms/asset/nonStand/NonStandInfoModelCollection";
  import Tools from "@/utils/tools";

  export default {
    name:"DownLoadFile",
    data() {
      return {
        flag:false,
        formData: {},
        searchParam:{},
        searchColumn:[],
        searchData:{},
        searchData1:{
          nameF:'',
          symbolF:'',
          valueF:''
        },
        searchData2: {
          offset: '0',
          limit: '10000'
        },
        columnNameDict:[]
      };
    },
    methods: {
      reset(){
        this.searchColumn = []
        this.searchData.tableName = '';
        this.searchData2 = {
          offset: '0',
          limit: '10000'
        }
      },
      initColmnNameDict (flag) {
        if (flag !== 'add') {
          this.searchColumn.forEach(s => {
            s.nameF = ''
            s.symbolF = ''
            s.valueF = ''
          })
        }
        let name = this.searchData.tableName
        if (!name) {
          return;
        }
        this.httpUtil.comnQuery({
          action: "DownLoadFileInfo.findColumnName",
          params: {tableName: name}
        }).then(data => {
          this.columnNameDict = data.rows;
        }).catch({})
      },
      downLoadFile(){
        this.flag = true;
        this.httpUtil.download({
          url: "download/server/DpsApp/expoertExcel.json",
          params: {
            searchColumn: JSON.stringify(this.searchColumn),
            tableName: this.searchData.tableName,
            offset: this.searchData2.offset,
            limit: this.searchData2.limit,
          },
          callback: response => {
            Tools.alert("下载完成");
            this.$refs.batchDownloadButton.setIconStyle(1, []);
            this.flag = false;
          }
        })
      },
      deleteSearchColumn(item,index){
        this.searchColumn.splice(index,1)
      },
      addSearchColumn(){
        this.initColmnNameDict('add')
        this.searchColumn.push(
          {
            nameF: '',
            valueF: '',
            symbolF: ''
          }
        )
        // if (this.searchColumn.length == 0) {
        //   this.searchColumn = [1];
        // } else {
        //   this.searchColumn.length + 1;
        // }
      }
    },
    watch:{
      if(searchColumn){

      }
    }
  };
</script>
