<template>
  <div>
    <k-form-search-customize data-target="queryTable" v-model="printTemp">
      <k-form-item label="产品代码">
        <k-field-text v-model="printTemp.prodCode"  ></k-field-text>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="printTemp.prodName"  ></k-field-text>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="queryTable" data-action="T8ProdInfo.findProdInfoByByCodeAndDate"
            @data-row-select="selectRow" data-operate-width="200px" >
      <k-grid-column data-header="产品代码" data-name="prodCode"  />
      <k-grid-column data-header="产品名称" data-name="prodName" />
      <k-grid-column data-header="产品成立日" data-name="" />
      <k-grid-column data-header="产品到期日" data-name="" />
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_lifecycle" />
      <template slot="operate"  slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="新增账户信息" data-functype="POPUP" data-size="mini"
               data-target="addTable">
          <md-icon>add</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="accountTable"  id="accountTable" data-action="T810001.findAccountByProdCode"
             data-operate-width="200px" >
      <k-grid-column data-header="账户类型" data-name="accountType"  data-dict="t8_account_type"/>
      <k-grid-column data-header="开户行名称" data-name="openAccountName" />
      <k-grid-column data-header="资金账户名称" data-name="accountName" />
      <k-grid-column data-header="资金账号" data-name="accountCode" />
      <k-grid-column data-header="大额行号" data-name="bankAccNum" />
      <k-grid-column data-header="账户开户行" data-name="openAccountCode" />
      <k-grid-column data-header="开户行账户所在省"  data-name="district"/>
      <k-grid-column data-header="开户行账户所在城市" data-name="city" />
      <k-grid-column data-header="账户状态" data-name="accountStatus" data-dict="t8_account_status" />
      <template slot="operate"  slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改账户信息" data-functype="POPUP" data-size="mini"
                :data-handler="openEditBox">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="上传开户回执" data-functype="SUBMIT" data-acion="" data-size="mini">
          <md-icon >cloud_upload</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="下载开户回执" data-functype="SUBMIT" data-acion="" data-size="mini">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="启用" data-functype="SUBMIT" :data-handler="isOnEnable"
                      data-action="T810001.updateStatusOnEnable" data-target="accountTable" data-size="mini">
        <md-icon>lock_open</md-icon>
      </k-btn>
         <k-btn class="md-info md-just-icon md-simple" data-descript="停用"
                data-action="T810001.updateStatusOnStop" data-target="accountTable" data-functype="SUBMIT" data-size="mini">
        <md-icon>lock</md-icon>
      </k-btn>
       <k-btn class="md-info md-just-icon md-simple" data-descript="销户" :data-handler="isOnlogOut"
              data-action="T810001.updateStatusOnlogOut"  data-target="accountTable"
              data-functype="SUBMIT"  data-size="mini">
          <md-icon>stop</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addTable"  data-title="新增账户信息">
      <k-form ref="addForm" :data-col="2">
        <k-form-item label="账户类型">
          <k-field-select v-model="formData.accountType"  :data-allowblank="false"
                          data-dict="t8_account_type" ></k-field-select>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-model="formData" data-functype="SUBMIT"
                 data-from="addAccountInfoForm" :data-handler="setAccount">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="M81000101Table" data-title="添加账户信息">
      <AddT1 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000101Table.close();
     this.$refs.queryTable.load()
       }">
     </AddT1>
    </k-popup>

    <k-popup ref="M81000102Table" data-title="添加账户信息">
      <AddT2 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000102Table.close();
     this.$refs.queryTable.load()
       }">
      </AddT2>
    </k-popup>

    <k-popup ref="M81000103Table" data-title="添加账户信息">
      <AddT3 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000103Table.close();
     this.$refs.queryTable.load()
       }">
      </AddT3>
    </k-popup>


    <k-popup ref="M81000104Table" data-title="添加账户信息">
      <AddT4 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000104Table.close();
     this.$refs.queryTable.load()
       }">
      </AddT4>
    </k-popup>

    <k-popup ref="M81000105Table" data-title="修改账户信息">
      <EditT1 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000105Table.close();
     this.$refs.accountTable.load()
       }">
      </EditT1>
    </k-popup>

    <k-popup ref="M81000106Table" data-title="修改账户信息">
      <EditT2 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000106Table.close();
     this.$refs.accountTable.load()
       }">
      </EditT2>
    </k-popup>

    <k-popup ref="M81000107Table" data-title="修改账户信息">
      <EditT3 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000107Table.close();
     this.$refs.accountTable.load()
       }">
      </EditT3>
    </k-popup>

    <k-popup ref="M81000108Table" data-title="修改账户信息">
      <EditT4 v-model="formData" :updSuccess="()=>{
     this.$refs.M81000108Table.close();
     this.$refs.accountTable.load()
       }">
      </EditT4>
    </k-popup>

  </div>
</template>
<script>
  import AddT1 from "./M81000101";
  import AddT2 from "./M81000102";
  import AddT3 from "./M81000103";
  import AddT4 from "./M81000104";
  import EditT1 from "./M81000105";
  import EditT2 from "./M81000106";
  import EditT3 from "./M81000107";
  import EditT4 from "./M81000108";
  import { assign } from "lodash";
  import Tools from '@/utils/tools.js';
  import httpUtil from "@/frame/httpUtil";

  export default {
    components: {
      AddT1,
      AddT2,
      AddT3,
      AddT4,
      EditT1,
      EditT2,
      EditT3,
      EditT4,
    },
    data(){
      return {
        printTemp:{
          prodCode: '',
          prodName: ''
        },
        formData:{},
        selectRowData:{},
      }
    },
    methods:{
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
        this.$refs.accountTable.load({prodCode: row.prodCode})
      },
      setAccount(value){
        this.$refs.addTable.close();
        this.httpUtil.comnQuery({
          action: "T810001.findInfoByCodeAndType",
          params: value
        }).then(data => {
         if (!data.rows[0]) {
           this.pupopBox(value);
          } else {
            Tools.alert("改产品下已存在该类型账户！");
          }
        }).catch(e => {
          Tools.alert("发生错误！");
        })
      },
      pupopBox(value){
        if (value.accountType == '1' || value.accountType == '2'){
          //托管费账户、托管户
          this.$refs.M81000101Table.popup();
        }else if(value.accountType == '3' || value.accountType == '4'){
          //DVP账户、DVP费用账号
          this.$refs.M81000102Table.popup();
        }else if(value.accountType == '5'){
          //基金账户
          this.$refs.M81000103Table.popup();
        }else if(value.accountType == '6'){
          //券商账户
          this.$refs.M81000104Table.popup();
        }else {
          Tools.alert("暂不支持创建该账户");
        }
      },
      isOnEnable(rows){
        if (rows.accountStatus == '1' || rows.accountStatus == '3'){
          return true;
        } else {
          Tools.alert("改账户状态不允许修改!");
          return false
        }
      },
      isOnlogOut(row){
        if (row.accountType == '4') {
          return false
        }
        if (this.selectRowData.prodStatus=='7' || this.selectRowData.prodStatus=='8'){
          return true
        } else {
          Tools.alert("产品未到期!");
          return false;
        }
      },
      openEditBox(row){
        if (row.accountStatus == '2') {
          Tools.alert("账号状态为启用时不能进行修改");
          return;
        }
        this.formData = assign({}, row)
        if (row.accountType == '1' || row.accountType == '2'){
          //托管费账户、托管户
          this.$refs.M81000105Table.popup();
        }else if(row.accountType == '3' || row.accountType == '4'){
          //DVP账户、DVP费用账号
          this.$refs.M81000106Table.popup();
        }else if(row.accountType == '5'){
          //基金账户
          this.$refs.M81000107Table.popup();
        }else if(row.accountType == '6'){
          //券商账户
          this.$refs.M81000108Table.popup();
        }else {
          alert("暂不支持修改该类型账户");
        }
      },
      initload(value){
        this.$refs.accountTable.load({prodCode: value})
      },
      gridValueRender(rows){
        this.$nextTick(()=>{
          let districtName = '';
          this.httpUtil.comnQuery({
            action: "District.findById",
            params: rows
          }).then(data => {
            districtName =  data.rows[0].districtName;
          }).catch(e => {
            reject(e);
          })
          return districtName;
        })
      }
    }
  }
</script>
