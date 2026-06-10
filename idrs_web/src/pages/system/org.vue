<template>
  <div class="py-page">
    <k-form-search data-target="orgGrid" data-model-name="Org" data-label-width="80px">
    </k-form-search>
    <div class="py-page-container">
      <div class="table-top-btns">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="addOrgPopup" v-if="global.isShowAuthorityButton('Org.add')">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </div>
      <k-grid ref="orgGrid" @data-row-select="selectRow"  data-action='Org.find1'
              data-diffcondition="orgno,parentorgno" :data-show-tree="true" dataTreeId="orgno" data-operate-width="210px">
        <k-grid-column data-header="机构代码" data-name="orgno"></k-grid-column>
        <k-grid-column data-header="机构名称" data-name="orgname"></k-grid-column>
        <k-grid-column data-header="机构级别" data-name="orglevel" data-dict="orglevel"></k-grid-column>
        <k-grid-column data-header="状态" data-name="orgStatus" data-dict="org_status"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改机构" data-functype="POPUP" data-size="mini"
                data-target="editOrgPopup" v-if="global.isShowAuthorityButton('Org.update')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="Org.delete" data-size="mini"
                data-type="danger" data-target="orgGrid" :data-confirm="true" data-descript="删除机构">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加机构弹出框   -->
    <k-popup ref="addOrgPopup" data-title="新增">
      <k-form ref="addOrgForm" :data-col="2">
        <k-form-item label="机构级别">
          <k-field-select v-model="formData.orglevel"  data-dict="orglevel" :dataAllowblank="false"
                          @data-on-change="queryOrgWL" />
        </k-form-item>
        <k-form-item label="父级机构" v-show="formData.orglevel!=='100000'">
          <k-field-cascader style="width:100%" ref="parentAdd" v-model="formData.parentorgno"
                            data-diffcondition="orgno,parentorgno" :dataParams="{orglevel:'0'}"
                            data-graphql='{queryOrg(action:"findWL") {rows{orgno, orgname, parentorgno, orgid},results}}'
                            data-display-child="children" data-check-strictly data-show-num
                            :data-props="{expandTrigger: 'hover'}" data-size="medium"
                            data-placeholder="请选择父级机构" data-clearable data-fileterable data-display-field="orgname"
                            data-value-field="orgno" :data-max-length="32"  :data-allowblank="formData.orglevel=='100000'">
          </k-field-cascader>
        </k-form-item>
        <k-form-item label="机构代码">
          <k-field-text v-model="formData.orgno"  :data-max-length="20"  :dataAllowblank="false" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字"  @data-on-change="checkOrgno" />
        </k-form-item>
        <k-form-item label="机构名称">
          <k-field-text v-model="formData.orgname" :data-max-length="170"  :dataAllowblank="false" />
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="formData.orgStatus" data-dict="org_status" data-default-value="D" :dataAllowblank="false"  />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Org.add" data-target="orgGrid" data-from="addOrgForm"
                 :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改机构弹出框   -->
    <k-popup ref="editOrgPopup" data-title="修改">
      <k-form ref="editOrgForm" :data-col="2">
        <k-form-item label="机构级别">
                  <k-field-select v-model="formData.orglevel" data-dict="orglevel" data-clearable="false" :data-max-length="3" data-disabled="true"  />
        </k-form-item>
        <k-form-item label="父级机构" v-if="formData.parentorgno!=='ROOT'" >
          <k-field-cascader style="width:100%" v-model="formData.parentorgno" data-diffcondition="orgno,parentorgno" :dataParams="{orglevel:this.formData.orglevel}" data-graphql='{queryOrg(action:"findWL") {rows{orgno, orgname, parentorgno, orgid},results}}'
                            data-display-child="children" data-check-strictly data-show-num :data-props="{ expandTrigger: 'hover'}"
                            data-size="medium" data-placeholder="请选择父级机构" data-clearable data-fileterable data-display-field="orgname"
                            data-value-field="orgno" :data-max-length="32"  :data-allowblank="false">
          </k-field-cascader>
        </k-form-item>
        <k-form-item label="机构代码">
          <k-field-text v-model="formData.orgno" data-clearable="false" :data-max-length="20" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字"  @data-on-change="checkOrgno" data-disabled="true"  />
        </k-form-item>
        <k-form-item label="机构名称">
          <k-field-text v-model="formData.orgname" :data-allowblank="false" :data-max-length="170"  />
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="formData.orgStatus" :data-allowblank="false" data-dict="org_status" :data-max-length="8" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Org.update" data-target="orgGrid" data-from="editOrgForm"
                 :data-model="formData">
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
  import Tools from '@/utils/tools.js';
  import {
    assign
  } from "lodash";


  export default {
    name:"org",
    data() {
      return {
        formData: {},
        cascaderValue: '',
        selectRowData: {}
      };
    },
    mounted() {
      console.log('org---');
    },
    methods: {
      checkOrgnoExist(value){

      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      },
      dataBeforeLoad() {
        return {
          "excOrgno": "ROOT"
        }
      },
      checkOrgno(){
        if(!this.formData.orgno){
          return ;
        }
        this.httpUtil.comnQuery({
          action:"Org.checkOrgno",
          params:{orgno:this.formData.orgno}
        }).then(data => {
          if( !data.rows[0] || ( this.formData.orgid  && data.rows[0].orgid==this.formData.orgid)){
            return ;
          }
          Tools.alert("机构代码已存在！","danger");
        })
      },
      queryOrgWL(value){
        console.log("value=:>>>",value);
        this.httpUtil.comnQuery({
          action:"Org.queryOrgWL",
          params:{orglevel:"100000"}
        }).then(
          data => {
            if(data.rows.length>0&& this.formData.orglevel =="1"){
              Tools.alert("总行已存在！","danger");
              this.formData.orglevel="130000";
            }else if(data.rows.length===0){
              Tools.alert("总行不存在，需要添加总行！","danger");
              this.formData.orglevel="100000";
            }
            this.$refs.parentAdd.$set(this.$refs.parentAdd.params,'orglevel',this.formData.orglevel);
            this.$refs.parentAdd.loadData();
          }
        )
      }
    }
  };
</script>
