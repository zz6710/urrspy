<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="QueryData" v-model="searchParam" data-target="indexConfigGrid" dataLabelWidth="100px">
        <k-form-item label="备份目标库">
          <k-field-text v-model="searchParam.targetDb" data-placeholder="备份目标库" />
        </k-form-item>
        <k-form-item label="备份目标表名">
          <k-field-text v-model="searchParam.targetTable" data-placeholder="备份目标表名" data-dict="rpt_validate_type"/>
        </k-form-item>
        <k-form-item label="备份仓库名">
          <k-field-text v-model="searchParam.bakTable" data-placeholder="备份仓库名" data-dict="bak_table"/>
        </k-form-item>
        <k-form-item label="最新备份日期">
          <k-field-date v-model="searchParam.newDate" data-placeholder="最新备份日期" data-type="date" data-date-format="yyyyMMdd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.indexConfigAdd={}" slot="button"
          data-target="addIndexConfigPopup" >
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </div>
      <k-grid ref="indexConfigGrid" @data-row-select="selectRow" data-operate-width="300px" data-action="SysBakCollectionPO.getBakCollectionList"
              @init="(id)=>{this.$kgrid = id}" :data-checkbox="false" data-checkbox-id="id">
        <k-grid-column data-align="left" data-header="备份目标库" data-name="targetDb" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="备份目标表名" data-name="targetTable" ></k-grid-column>
        <k-grid-column data-align="left" data-header="仓库表命名排序号" data-name="bakSort" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="仓库表名" data-name="bakTable" ></k-grid-column>
        <k-grid-column data-align="left" data-header="仓库数据量" data-name="bakNum" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="最新备份日期" data-name="newDate" data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope" system-aligen>
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="btn-custom-text" :data-handler="selectRow"
          data-target="editIndexConfigPopup" data-descript="修改归档配置信息">
          修改
        </k-btn>
        <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SysBakCollectionPO.deleteBakCollection"
          :data-confirm="true" data-size="mini" data-type="danger" data-target="indexConfigGrid" data-descript="删除">
          删除
        </k-btn>
          <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP" data-size="mini"
          :data-handler="selectRow" data-target="ReportIndexConfigPopup">
          详情
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!--    归档集合新增弹出框   -->
    <k-popup ref="addIndexConfigPopup" data-title="新增">
      <k-form ref="addIndexConfigForm" :data-col="2">
        <k-form-item label="备份目标库">
          <k-field-text v-model="indexConfigAdd.targetDb" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备份目标表名">
          <k-field-text v-model="indexConfigAdd.targetTable" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表命名排序号" >
          <k-field-text v-model="indexConfigAdd.bakSort" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备份配置id" >
          <k-field-text v-model="indexConfigAdd.bakConfigId" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表名（手动建表）">
          <k-field-text v-model="indexConfigAdd.bakTable" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addIndexConfigPopup" @click="addBakCollection"
                 :data-model="indexConfigAdd" data-target="indexConfigGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

 <!--   数据归档修改弹出框   -->
    <k-popup ref="editIndexConfigPopup" data-title="校验指标修改">
      <k-form ref="editIndexConfigForm" :data-col="2">
      <k-form-item label="备份目标库">
          <k-field-text v-model="indexConfig.targetDb" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备份目标表名">
          <k-field-text v-model="indexConfig.targetTable" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表命名排序号">
          <k-field-text v-model="indexConfig.bakSort" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备份配置id" >
          <k-field-text v-model="indexConfig.bakConfigId" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表名（手动建表）">
          <k-field-text v-model="indexConfig.bakTable" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表名" >
          <k-field-text v-model="indexConfig.bakTable" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表数据量" >
          <k-field-text v-model="indexConfig.bakNum" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表数据起始日期">
          <k-field-text v-model="indexConfig.startDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="仓库表数据结束日期">
          <k-field-text v-model="indexConfig.endDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="最新备份日期">
          <k-field-text v-model="indexConfig.newDate" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="创建时间">
          <k-field-text v-model="indexConfig.createTime" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="修改时间">
          <k-field-text v-model="indexConfig.updateTime" :data-allowblank="false"/>
        </k-form-item>
      </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="editIndexConfigPopup" @click="updateBakCollection"
                 :data-model="indexConfig" data-target="indexConfigGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--   归档仓库详情   -->
    <k-popup ref="ReportIndexConfigPopup" data-title="归档备份详情">
      <k-form :data-col="3">
        <k-form-item label="备份目标库">
          <k-field-display v-model="indexConfig.targetDb"/>
        </k-form-item>
        <k-form-item label="备份目标表名">
          <k-field-display v-model="indexConfig.targetTable" />
        </k-form-item>
        <k-form-item label="仓库表命名排序号">
          <k-field-display v-model="indexConfig.bakSort" />
        </k-form-item>
        <k-form-item label="备份配置id" >
          <k-field-display v-model="indexConfig.bakConfigId"/>
        </k-form-item>
        <k-form-item label="仓库表名" >
          <k-field-display v-model="indexConfig.bakTable"/>
        </k-form-item>
        <k-form-item label="仓库表数据量" >
          <k-field-display v-model="indexConfig.bakNum"/>
        </k-form-item>
        <k-form-item label="仓库表数据起始日期">
          <k-field-display v-model="indexConfig.startDate"/>
        </k-form-item>
        <k-form-item label="仓库表数据结束日期">
          <k-field-display v-model="indexConfig.endDate"/>
        </k-form-item>
        <k-form-item label="最新备份日期">
          <k-field-display v-model="indexConfig.newDate"/>
        </k-form-item>
        <k-form-item label="创建时间">
          <k-field-display v-model="indexConfig.createTime"/>
        </k-form-item>
        <k-form-item label="修改时间">
          <k-field-display v-model="indexConfig.updateTime"/>
        </k-form-item>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name:"BakCollection",
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
      this.getBakCollectionList();
    },
    methods: {
      //查询归档配置
      getBakCollectionList() {
        this.httpUtil.comnQuery({
          action: "SysBakCollectionPO.getBakCollectionList",
          params: null
        }).then(data => {
          this.reportTableDict = data.rows;
        }).catch({})
      },
      selectRow(row) {
        this.indexConfig = Object.assign({}, row);
      },
      //新增集合信息
      addBakCollection() {
        this.httpUtil.comnQuery({
          action: "SysBakCollectionPO.addBakCollection",
          params: this.indexConfigAdd
        }).then(data => {
          this.$refs.addIndexConfigPopup.close();
          this.$refs.indexConfigGrid.load();
          Tools.alert(data.returnmsg);
          this.$refs.QueryData.query();
        })
      },
      //更新集合信息
      updateBakCollection(){
          this.httpUtil.comnQuery({
             action: "SysBakCollectionPO.updateBakCollection",
             params: this.indexConfig
          }).then(data => {
             this.$refs.editIndexConfigPopup.close();
             Tools.alert(data.returnmsg);
             this.$refs.QueryData.query();
          })
      },
     }
  };
</script>
