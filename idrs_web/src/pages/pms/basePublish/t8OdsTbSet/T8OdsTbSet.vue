<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="t8OdsTbSetGrid">
        <k-form-item label="源database">
          <k-field-select v-model="searchParam.database"   :data-data="databaseList" data-display-field="database" data-value-field="database"></k-field-select>
        </k-form-item>
        <k-form-item label="配置状态">
          <k-field-select v-model="searchParam.status" data-dict="t8_ods_tb_set_status"></k-field-select>
        </k-form-item>
        <k-form-item label="是否增量同步">
          <k-field-select v-model="searchParam.incrementFlag" data-dict="1yes0no"></k-field-select>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" slot="button"
               data-target="addT8OdsTbSetPopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8OdsTbSetGrid" @data-row-select="selectRow" data-action="BaseEtlSet.findT8OdsSyncSets"
              data-fixed="right"
              data-operate-width="400px">
        <k-grid-column data-align="center" data-header="源database" data-name="database" ></k-grid-column>
        <k-grid-column data-align="center" data-header="源系统描述" data-name="sysdesc"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据源编号" data-name="sharding"></k-grid-column>
        <k-grid-column data-align="center" data-header="源表名" data-name="tablename" data-width="200"></k-grid-column>
        <k-grid-column data-align="center" data-header="系统对应入库表" data-name="pmsTablename" data-width="200"></k-grid-column>
        <k-grid-column data-align="center" data-header="排序" data-name="sqlOrder" data-width="260" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="查询条件" data-name="selectCondition" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="每日执行时间" data-name="execTime" data-type="time" ></k-grid-column>
        <k-grid-column data-align="center" data-header="是否增量同步" data-name="incrementFlag" data-dict="1yes0no"></k-grid-column>
        <k-grid-column data-align="center" data-header="日期条件" data-name="dateCondition" :data-hidden="true" data-type="date"></k-grid-column>
        <k-grid-column data-align="center" data-header="结束日期条件" data-name="dateEndCondition" :data-hidden="true" data-type="date"></k-grid-column>
        <k-grid-column data-align="center" data-header="条数约束" data-name="limits" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="配置状态" data-name="status" data-dict="t8_ods_tb_set_status"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建人名称" data-name="crtUserName" :data-hidden="true"></k-grid-column>

        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP" data-size="mini"
                 data-target="viewT8OdsTbSetPopup">
            <!--            <md-icon>library_books</md-icon>-->
            详情
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="修改数仓同步表维护"  data-functype="POPUP" data-size="mini"
                 data-target="editT8OdsTbSetPopup">
            <!--            <md-icon>edit</md-icon>-->
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="启用"  data-functype="SUBMIT"
                 :data-disabled="scope.row.row.status == 'N'"
                 data-action="BaseEtlSet.recoverStatus" :data-confirm="true"
                 data-target="t8OdsTbSetGrid"
                 data-size="mini">
            <!--            <md-icon>lock_open</md-icon>-->
            启用
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="停用"
                 :data-disabled="scope.row.row.status=='P'"
                 data-action="BaseEtlSet.stopStatus" :data-confirm="true"
                 data-target="t8OdsTbSetGrid" data-functype="SUBMIT" data-size="mini"
          >
            <!--            <md-icon>lock</md-icon>-->
            停用
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT"  data-action="BaseEtlSet.deleteT8OdsSyncSet" data-size="mini"
                 data-type="danger" data-target="t8OdsTbSetGrid" :data-confirm="true" data-descript="删除数仓同步表维护">
            <!--          	<md-icon>close</md-icon>-->
            删除
          </k-btn>

          <!--          <k-field-bswitch data-on-value="N" data-off-value="P" v-model="scope.row.row.status" data-on-action="T8OdsSyncSet.recoverStatus"-->
          <!--                           data-off-action="T8OdsSyncSet.stopStatus" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"-->
          <!--                           data-off-confirm-info="停用" />-->
        </template>
      </k-grid>
    </div>

    <!--    添加数仓同步表维护弹出框   -->
    <k-popup ref="addT8OdsTbSetPopup" data-title="新增">
      <k-form ref="addT8OdsTbSetForm" :data-col="2">
        <k-form-item label="源database">
          <k-field-text v-model="formData.database" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="源系统描述">
          <k-field-text v-model="formData.sysdesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据源编号">
          <k-field-text v-model="formData.sharding" :data-allowblank="false" :data-max-length="2"/>
        </k-form-item>
        <k-form-item label="源表名">
          <k-field-text v-model="formData.tablename" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="系统对应入库表">
          <k-field-text v-model="formData.pmsTablename" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="最早同步时间" >
          <k-field-time v-model="formData.execTime" data-type="time" />
        </k-form-item>
        <k-form-item label="条数约束">
          <k-field-text v-model="formData.limits" :data-max-length="10"/>
        </k-form-item>
        <k-form-item label="是否增量同步" >
          <k-field-select v-model="formData.incrementFlag"  data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="sql语句字段" :dataCol="2">
          <k-field-text v-model="formData.tableCloumns" inputType="textarea" :rows="4" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="排序" :dataCol="2">
          <k-field-text v-model="formData.sqlOrder" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="查询条件" :dataCol="2">
          <k-field-text v-model="formData.selectCondition" inputType="textarea" :rows="4"/>
        </k-form-item>
        <k-form-item label="日期条件" v-show="false">
          <k-field-date v-model="formData.dateCondition" data-type="date" />
        </k-form-item>
        <k-form-item label="结束日期条件" v-show="false">
          <k-field-date v-model="formData.dateEndCondition" data-type="date" />
        </k-form-item>

        <k-form-item label="增量标识" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.incrementKey"  :data-allowblank="formData.incrementFlag!='1'"/>
        </k-form-item>
        <k-form-item label="增量判断条件" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.uniqueKey"  :data-allowblank="formData.incrementFlag!='1'"/>
        </k-form-item>
        <k-form-item label="增量更新sql" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.incrementUpdateSql" inputType="textarea" :rows="4" :data-allowblank="formData.incrementFlag!='1'"/>
        </k-form-item>

        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea" :rows="4"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseEtlSet.addT8OdsSyncSet" data-from="addT8OdsTbSetForm"
                 :data-model="formData" data-target="t8OdsTbSetGrid" :data-after-success="updateDataBaseList">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改数仓同步表维护弹出框   -->
    <k-popup ref="editT8OdsTbSetPopup" data-title="修改" :dataDialogDrag="true">
      <k-form ref="editT8OdsTbSetForm" :data-col="2">
        <k-form-item label="源database">
          <k-field-text v-model="formData.database" :data-allowblank="false" :data-max-length="2"/>
        </k-form-item>
        <k-form-item label="源系统描述">
          <k-field-text v-model="formData.sysdesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数据源编号">
          <k-field-text v-model="formData.sharding" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="源表名">
          <k-field-text v-model="formData.tablename" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="系统对应入库表">
          <k-field-text v-model="formData.pmsTablename" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="最早同步时间" >
          <k-field-time v-model="formData.execTime" data-type="time" />
        </k-form-item>
        <k-form-item label="条数约束">
          <k-field-text v-model="formData.limits" :data-max-length="10"/>
        </k-form-item>
        <k-form-item label="是否增量同步" >
          <k-field-select v-model="formData.incrementFlag"  data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="sql语句字段" :dataCol="2">
          <k-field-text v-model="formData.tableCloumns" inputType="textarea" :rows="4" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="排序" :dataCol="2">
          <k-field-text v-model="formData.sqlOrder" inputType="textarea" :rows="4"/>
        </k-form-item>
        <k-form-item label="查询条件" :dataCol="2">
          <k-field-text v-model="formData.selectCondition" inputType="textarea" :rows="4"/>
        </k-form-item>
        <k-form-item label="日期条件" v-show="false">
          <k-field-date v-model="formData.dateCondition" data-type="date" />
        </k-form-item>
        <k-form-item label="结束日期条件" v-show="false">
          <k-field-date v-model="formData.dateEndCondition" data-type="date" />
        </k-form-item>
        <k-form-item label="增量标识" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.incrementKey"  :data-allowblank="formData.incrementFlag!='1'"/>
        </k-form-item>
        <k-form-item label="增量判断条件" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.uniqueKey"  :data-allowblank="formData.incrementFlag!='1'"/>
        </k-form-item>
        <k-form-item label="增量更新sql" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.incrementUpdateSql" inputType="textarea" :rows="4" :data-allowblank="formData.incrementFlag!='1'"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea" :rows="4"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseEtlSet.updateT8OdsSyncSet" data-from="editT8OdsTbSetForm"
                 :data-model="formData" data-target="t8OdsTbSetGrid" :data-after-success="updateDataBaseList">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    数仓同步表详情弹出框   -->
    <k-popup ref="viewT8OdsTbSetPopup" data-title="详情" :dataDialogDrag="true">
      <k-form ref="editT8OdsTbSetForm" :data-col="2">
        <k-form-item label="源database">
          <k-field-text v-model="formData.database" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="源系统描述">
          <k-field-text v-model="formData.sysdesc" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="数据源编号">
          <k-field-text v-model="formData.sharding" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="源表名">
          <k-field-text v-model="formData.tablename" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="系统对应入库表">
          <k-field-text v-model="formData.pmsTablename" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="最早同步时间" >
          <k-field-time v-model="formData.execTime" data-type="time" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="条数约束">
          <k-field-text v-model="formData.limits" :data-max-length="10" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否增量同步" >
          <k-field-select v-model="formData.incrementFlag"  data-dict="1yes0no" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="sql语句字段" :dataCol="2">
          <k-field-text v-model="formData.tableCloumns" inputType="textarea" :rows="4" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="排序" :dataCol="2">
          <k-field-text v-model="formData.sqlOrder" inputType="textarea" :data-disabled="true" :rows="4"/>
        </k-form-item>
        <k-form-item label="查询条件" :dataCol="2">
          <k-field-text v-model="formData.selectCondition" inputType="textarea" :rows="4" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="查询条件" :dataCol="2">
          <k-field-text v-model="formData.selectCondition" inputType="textarea" :rows="4" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="结束日期条件" v-show="false">
          <k-field-date v-model="formData.dateEndCondition" data-type="date" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="增量标识" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.incrementKey"  :data-allowblank="formData.incrementFlag!='1'" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="增量判断条件" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.uniqueKey"  :data-allowblank="formData.incrementFlag!='1'" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="增量更新sql" :dataCol="2" v-if="formData.incrementFlag=='1'">
          <k-field-text v-model="formData.incrementUpdateSql" inputType="textarea" :rows="4" :data-allowblank="formData.incrementFlag!='1'" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea" :rows="4" :data-disabled="true"/>
        </k-form-item>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
  name:'T8OdsTbSet',
  data() {
    return {
      databaseList:{},
      formData: {},
      selectRowData: {},
      searchParam:{}
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    getDataBase(){
      this.httpUtil.comnQuery({
        action: "BaseEtlSet.getDataBase",
        params: {},
      }).then(data => {
        console.log(data.rows)
        this.databaseList = data.rows;
      }).catch({})
    },
    updateDataBaseList(){
      this.getDataBase();
    }
  },
  created() {
    this.getDataBase();
  }
};
</script>
<!--<style scoped>
>>> .md-ripple {
  padding: 4px 15px !important;
}

</style>-->
