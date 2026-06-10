<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="reportMenuInfoGrid">

        <k-form-item label="报表菜单id">
          <k-field-select v-model="searchParam.menuId"/>
        </k-form-item>

        <k-form-item label="菜单名称">
          <k-field-select v-model="searchParam.menuName"/>
        </k-form-item>

      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="reportMenuInfoGrid" @data-row-select="selectRow" :data-handler="queryReportMenu"
              @init="(id)=>{this.$kgrid = id}" :data-checkbox="true" data-checkbox-id="id">
        <k-grid-column data-align="center" data-header="模块id" data-name="moduleId" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="报表菜单id" data-name="menuId"></k-grid-column>
        <k-grid-column data-align="center" data-header="菜单名称" data-name="menuName"></k-grid-column>
        <k-grid-column data-align="center" data-header="上级菜单id" data-name="upperId"></k-grid-column>
        <k-grid-column data-align="center" data-header="页面URL" data-name="url"></k-grid-column>
        <k-grid-column data-align="center" data-header="菜单图片样式" data-name="iconCls" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="菜单图片文件" data-name="icon" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="加载顺序" data-name="loadOrder"></k-grid-column>
        <k-grid-column data-align="center" data-header="页面配置ID" data-name="pageId"></k-grid-column>
        <k-grid-column data-align="center" data-header="菜单状态" data-name="status" data-dict=""></k-grid-column>
        <k-grid-column data-align="center" data-header="快捷调用代码" data-name="fastCode" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="输入类型" data-name="funcType" data-dict="" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="备注" data-name="remark" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="菜单类型" data-name="menuType" data-hidden="true" data-dict=""></k-grid-column>
        <k-grid-column data-align="center" data-header="报表报送模板路径" data-name="reportUrl" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="控件类型" data-name="objType" data-hidden="true" data-dict=""></k-grid-column>
        <k-grid-column data-align="center" data-header="导入报表时初始化的SQL" data-name="initSql" data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple" :data-handler="selectRow"
                 data-target="editReportMenuPopup" data-descript="修改报表菜单信息">
            <md-icon>edit</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--   修改报表信息弹出框   -->
    <k-popup ref="editReportMenuPopup" data-title="编辑报表菜单信息">
      <k-form ref="editReportMenuForm" :data-col="2">

        <k-form-item label="模块id" vf-show="false">
          <k-field-text v-model="reportMenuData.moduleId"/>
        </k-form-item>

        <k-form-item label="报表菜单id">
          <k-field-text v-model="reportMenuData.menuId"/>
        </k-form-item>

        <k-form-item label="菜单名称">
          <k-field-text v-model="reportMenuData.menuName"/>
        </k-form-item>

        <k-form-item label="上级菜单id">
          <k-field-text v-model="reportMenuData.upperId"/>
        </k-form-item>

        <k-form-item label="页面URL">
          <k-field-text v-model="reportMenuData.url"/>
        </k-form-item>

        <k-form-item label="菜单图片样式" vf-show="false">
          <k-field-text v-model="reportMenuData.iconCls"/>
        </k-form-item>

        <k-form-item label="图片路径">
          <k-field-text v-model="reportMenuData.icon"/>
        </k-form-item>

        <k-form-item label="加载顺序">
          <k-field-text v-model="reportMenuData.loadOrder"/>
        </k-form-item>

        <k-form-item label="页面配置ID">
          <k-field-text v-model="reportMenuData.pageId"/>
        </k-form-item>

        <k-form-item label="菜单状态">
          <k-field-select v-model="reportMenuData.status" data-dict=""/>
        </k-form-item>

        <k-form-item label="快捷调用代码" vf-show="false">
          <k-field-text v-model="reportMenuData.fastCode"/>
        </k-form-item>

        <k-form-item label="输入类型">
          <k-field-text v-model="reportMenuData.funcType" data-dict=""/>
        </k-form-item>

        <k-form-item label="备注">
          <k-field-text v-model="reportMenuData.remark"/>
        </k-form-item>

        <k-form-item label="菜单类型">
          <k-field-select v-model="reportMenuData.menuType" data-dict=""/>
        </k-form-item>

        <k-form-item label="报表报送模板路径">
          <k-field-text v-model="reportMenuData.reportUrl"/>
        </k-form-item>

        <k-form-item label="控件类型">
          <k-field-text v-model="reportMenuData.objType" data-dict=""/>
        </k-form-item>

        <k-form-item label="导入报表时初始化的SQL">
          <k-field-text v-model="reportMenuData.initSql"/>
        </k-form-item>

      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name:"reportMenuMaintain",
    data() {
      return {
        selectRowData: {},
        searchParam:{},
        reportMenuData:{},
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.reportMenuData = Object.assign({}, row);
      },
      queryReportMenu(){//新增指标配置信息
        this.httpUtil.ajax({
          url:"/server/form/RptApp/reportMenuMaintain/queryReportMenu.action",
          params: this.searchParam
        }).then(res=>{
          this.$refs.reportMenuInfoGrid.load(res);
          Tools.alert(reg.returnmsg);
        })
      },
     }
  };
</script>
