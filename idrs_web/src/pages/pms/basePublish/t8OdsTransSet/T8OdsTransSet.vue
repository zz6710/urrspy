<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="t8OdsTransSetGrid">
        <k-form-item label="产品代码">
          <k-field-select v-model="searchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" />
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="searchParam.status" data-dict="t8_ods_tb_set_status"></k-field-select>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" slot="button"
               data-target="addT8OdsTransSetPopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>

        <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="SUBMIT" :data-model="formData"
               data-target="t8OdsTransSetGrid" ref="handTriggerButton" :data-handler="handTrigger">
          <md-icon md-src="/static/svg/add.svg" />手动触发</k-btn>

      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8OdsTransSetGrid" @data-row-select="selectRow" data-action="T8OdsTransSet.findT8OdsTransSets"
              @init="(id)=>{this.$kgrid = id}"  :data-checkbox="true" data-checkbox-id="id">
		<k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"></k-grid-column>
    <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"></k-grid-column>
		<k-grid-column data-align="center" data-header="执行条件" data-name="conditions"></k-grid-column>
		<k-grid-column data-align="center" data-header="执行顺序" data-name="execOrder"></k-grid-column>
		<k-grid-column data-align="center" data-header="执行类型" data-name="execType"></k-grid-column>
    <k-grid-column data-align="center" data-header="状态" data-name="status" data-dict="t8_ods_tb_set_status"></k-grid-column>
    <k-grid-column data-align="center" data-header="创建人姓名" data-name="crtUserName"></k-grid-column>
		<k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"></k-grid-column>
		<k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"></k-grid-column>

        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini"
                 data-target="viewT8OdsTransSetPopup">
            <md-icon>library_books</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改ODS数据转换配置" data-functype="POPUP" data-size="mini"
            data-target="editT8OdsTransSetPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8OdsTransSet.deleteT8OdsTransSet" data-size="mini"
               data-type="danger" data-target="t8OdsTransSetGrid" :data-confirm="true" data-descript="删除ODS数据转换配置">
          	<md-icon>close</md-icon>
    	    </k-btn>
          <k-field-bswitch data-on-value="N" data-off-value="P" v-model="scope.row.row.status" data-on-action="T8OdsTransSet.recoverStatus"
                           data-off-action="T8OdsTransSet.stopStatus" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"
                           data-off-confirm-info="停用" />
        </template>
      </k-grid>
    </div>

	<!--    添加ODS数据转换配置弹出框   -->
	<k-popup ref="addT8OdsTransSetPopup" data-title="新增">
    	<k-form ref="addT8OdsTransSetForm" :data-col="2">
        <k-form-item label="产品名称">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false" :disabled = "true"/>
        </k-form-item>
        <k-form-item label="执行条件">
          <k-field-text v-model="formData.conditions" />
        </k-form-item>
        <k-form-item label="执行顺序">
          <k-field-text v-model="formData.execOrder" data-validate-type="int" :dataMaxLength="2"/>
        </k-form-item>
        <k-form-item label="执行类型">
          <k-field-text v-model="formData.execType" />
        </k-form-item>

        <k-form-item label="查询字段" :dataCol="2">
          <k-field-text v-model="formData.selectColumns" inputType="textarea" :rows="6"/>
        </k-form-item>
        <k-form-item label="查询SQL语句" :dataCol="2">
          <k-field-text v-model="formData.selectSqls" inputType="textarea" :rows="10"/>
        </k-form-item>
        <k-form-item label="目标表删除SQL语句" :dataCol="2">
          <k-field-text v-model="formData.deleteSqls" inputType="textarea" />
        </k-form-item>
        <k-form-item label="目标表插入SQL语句" :dataCol="2">
          <k-field-text v-model="formData.insertSqls" inputType="textarea" :rows="10"/>
        </k-form-item>
        <k-form-item label="目标表更新SQL语句" :dataCol="2">
          <k-field-text v-model="formData.updateSqls" inputType="textarea" :rows="10"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea"/>
        </k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8OdsTransSet.addT8OdsTransSet" data-from="addT8OdsTransSetForm"
		               :data-model="formData" data-target="t8OdsTransSetGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改ODS数据转换配置弹出框   -->
	<k-popup ref="editT8OdsTransSetPopup" data-title="修改">
	  <k-form ref="editT8OdsTransSetForm" :data-col="2">
      <k-form-item label="产品名称">
        <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false" :disabled = "true"/>
      </k-form-item>
      <k-form-item label="执行条件">
        <k-field-text v-model="formData.conditions" />
      </k-form-item>
      <k-form-item label="执行顺序">
        <k-field-text v-model="formData.execOrder" data-validate-type="int" :dataMaxLength="2"/>
      </k-form-item>
      <k-form-item label="执行类型">
        <k-field-text v-model="formData.execType" />
      </k-form-item>

      <k-form-item label="查询字段" :dataCol="2">
        <k-field-text v-model="formData.selectColumns" inputType="textarea" :rows="6"/>
      </k-form-item>
      <k-form-item label="查询SQL语句" :dataCol="2">
        <k-field-text v-model="formData.selectSqls" inputType="textarea" :rows="10"/>
      </k-form-item>
      <k-form-item label="目标表删除SQL语句" :dataCol="2">
        <k-field-text v-model="formData.deleteSqls" inputType="textarea" />
      </k-form-item>
      <k-form-item label="目标表插入SQL语句" :dataCol="2">
        <k-field-text v-model="formData.insertSqls" inputType="textarea" :rows="10"/>
      </k-form-item>
      <k-form-item label="目标表更新SQL语句" :dataCol="2">
        <k-field-text v-model="formData.updateSqls" inputType="textarea" :rows="10"/>
      </k-form-item>
      <k-form-item label="备注" :dataCol="2">
        <k-field-text v-model="formData.remark" inputType="textarea" />
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8OdsTransSet.updateT8OdsTransSet" data-from="editT8OdsTransSetForm"
	        :data-model="formData" data-target="t8OdsTransSetGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <!--    ODS数据转换配置详情弹出框   -->
    <k-popup ref="viewT8OdsTransSetPopup" data-title="详情" :dataDialogDrag="true">
      <k-form ref="editT8OdsTransSetForm" :data-col="2">
        <k-form-item label="产品名称">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false" :disabled = "true"/>
        </k-form-item>
        <k-form-item label="执行条件">
          <k-field-text v-model="formData.conditions" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="执行顺序">
          <k-field-text v-model="formData.execOrder" data-validate-type="int"
                        :data-disabled="true"
                        :dataMaxLength="2"/>
        </k-form-item>
        <k-form-item label="执行类型">
          <k-field-text v-model="formData.execType" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="查询字段" :dataCol="2">
          <k-field-text v-model="formData.selectColumns" inputType="textarea" :data-disabled="true" :rows="6"/>
        </k-form-item>
        <k-form-item label="查询SQL语句" :dataCol="2">
          <k-field-text v-model="formData.selectSqls" inputType="textarea"
                        :data-disabled="true" :rows="10"/>
        </k-form-item>
        <k-form-item label="目标表删除SQL语句" :dataCol="2">
          <k-field-text v-model="formData.deleteSqls" inputType="textarea" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="目标表插入SQL语句" :dataCol="2">
          <k-field-text v-model="formData.insertSqls" inputType="textarea" :data-disabled="true" :rows="10"/>
        </k-form-item>
        <k-form-item label="目标表更新SQL语句" :dataCol="2">
          <k-field-text v-model="formData.updateSqls" inputType="textarea" :data-disabled="true" :rows="10"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea" :data-disabled="true"/>
        </k-form-item>
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
        searchParam:{}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      handTrigger(){
        const _this = this
        const list = _this.$kgrid.getSelected();
        if(list.length<=0){
          Tools.alert("请选择产品");
          return false;
        }
        console.log(list)
        _this.$refs.handTriggerButton.setIconStyle(0, []);
        /*if (list.length > 0) {*/
        this.httpUtil.comnUpdate({
          action: 'T8OdsSyncSet.odsToData',
          params: {list: JSON.stringify(list)},
          successAlert: true,
        }).then(data => {
          console.log(data)
          _this.$refs.handTriggerButton.setIconStyle(1, []);
          _this.$kgrid.setSelected([]);
        });
      }
    }
  };
</script>
