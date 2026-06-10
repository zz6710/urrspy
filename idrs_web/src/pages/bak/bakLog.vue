<template>
  <div class="py-page">
    <div>
      <k-form-search-customize v-model="searchParam" data-target="indexConfigGrid">
        <k-form-item label="归档操作描述">
          <k-field-text v-model="searchParam.operateDesc" data-placeholder="归档操作描述" />
        </k-form-item>

        <k-form-item label="操作记录类型">
          <k-field-select v-model="searchParam.type" data-dict="bak_record_type"  data-placeholder="操作记录类型" />
        </k-form-item>

        <k-form-item label="操作日期">
          <k-field-date v-model="searchParam.operateDate" data-date-format="yyyyMMdd"  data-placeholder="操作日期" />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="indexConfigGrid" @data-row-select="selectRow"  data-operate-width="100px" data-action="SysBakLogPO.getBakLogList"
              @init="(id)=>{this.$kgrid = id}" :data-checkbox="false" data-checkbox-id="id">
        <k-grid-column data-align="left" data-header="归档操作描述" data-name="operateDesc"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作记录类型" data-name="type" data-dict="bak_record_type" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="operateDate" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="用时(秒)" data-name="duration" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建时间" data-name="createTime" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="更新时间" data-name="updateTime" data-width="180"></k-grid-column>
        <template slot="operate" slot-scope="scope" system-aligen>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SysBakConfigPO.deleteBakConfig"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="indexConfigGrid" data-descript="删除">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name:"bakConfig",
    data() {
      return {
        searchParam:{},
        indexConfig:{},
        bakCollection:{},
        reportTableDict:{},
        indexConfigAdd:{}
      };
    },
    created() {
      this.getBakConfigList();
    },
    methods: {
      //查询归档配置
      getBakConfigList() {
        this.httpUtil.comnQuery({
          action: "SysBakConfigPO.getBakConfigList",
          params: null
        }).then(data => {
          this.reportTableDict = data.rows;
        }).catch({})
      },
      selectRow(row) {
        this.indexConfig = Object.assign({}, row);
      },
      selectCollectionRow() {
        this.httpUtil.comnQuery({
          action: "SysBakConfigPO.getBakCollectionList",
          params: this.indexConfig
        }).then(data => {
          this.bakCollection = data;
        }).catch({})
      },
     }
  };
</script>
