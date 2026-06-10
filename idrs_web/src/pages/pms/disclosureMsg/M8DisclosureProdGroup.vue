<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ObjectGrid" v-model="prodSearchParam">
        <k-form-item label="产品组名称">
          <k-field-text v-model="prodSearchParam.groupId" />
        </k-form-item>
        <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" style="width: 120px;"
               data-target="addPopup">
          新增自定义产品组
        </k-btn>
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" :dataData="dataList" data-action="">
        <k-grid-column data-header="产品组名称" data-name="name"/>
        <k-grid-column data-header="产品组用途" data-name="purpose"/>
        <k-grid-column data-header="产品数量" data-name="prodCount"/>
        <k-grid-column data-header="状态" data-name="status"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="btn-custom-plain" data-descript="修改">
            修改
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="md-danger" data-descript="停用">
            停用
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="descPopup"
                 class="btn-custom-plain" data-descript="详情">
            详情
          </k-btn>
        </template>
      </k-grid>


      <k-popup ref="descPopup" data-title="详情">
        <k-grid ref="t8ObjectGrid" :dataData="dataList3" :data-operate-column="false" style="width: 100%;">
          <k-grid-column data-header="产品代码" data-name="prodCode"/>
          <k-grid-column data-header="产品名称" data-name="prodName"/>
        </k-grid>
      </k-popup>


      <k-popup ref="addPopup" data-title="新建信披产品组">
        <span style="background-color: yellow;float: right;">说明：信披产品组指一个公告中包含多个产品信息。</span>
        <br/>
        <br/>
        <k-form ref="addForm" :data-col="2">
          <k-form-item label="信披产品组名称" :data-col="2">
            <k-field-text v-model="formData.name"/>
          </k-form-item>
          <k-form-item label="信披产品组用途" :data-col="2">
            <k-field-text v-model="formData.purpose" input-type="textarea" :rows="3"/>
          </k-form-item>
          <span>选泽产品清单：</span>
          <k-btn data-functype="POPUP" class="btn-custom-primary" style="max-height: 30px; margin-top: -5px;"
                 data-target="addPopup2" :data-handler="openPopup2">
            <md-icon>add</md-icon>
          </k-btn>
          <k-grid ref="t8ObjectGrid" :dataData="dataList2" style="width: 100%;">
            <k-grid-column data-header="管理人" data-name="adminUser"/>
            <k-grid-column data-header="产品代码" data-name="prodCode"/>
            <k-grid-column data-header="产品名称" data-name="prodName"/>
            <k-grid-column data-header="产品状态" data-name="status"/>
            <template slot="operate" slot-scope="scope">
              <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                     class="md-danger md-just-icon md-simple" data-descript="删除">
                <md-icon>close</md-icon>
              </k-btn>
            </template>
          </k-grid>

          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                    :data-handler="saveRule"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

      <k-popup ref="addPopup2" data-title="添加产品">
        <k-form ref="addForm" :data-col="2">
          <k-form-item label="管理人">
            <k-field-select v-model="formData.adminUser" :data-data="modelData"
              data-display-field="text" data-value-field="value"/>
          </k-form-item>
          <k-form-item label="产品代码">
            <k-field-select ref="prod_code" v-model="formData.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                            data-display-field="prodCode,prodName" data-value-field="prodCode"
                            @data-on-change="prod_code_change"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="formData.prodName" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品状态">
            <k-field-text v-model="formData.status" :data-disabled="true"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                    :data-handler="addProd"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

    </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "M8DisclosureProdGroup",
  data() {
    return {
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        adminUser:'',
        prodCode: '',
        prodName: '',
        status:'',
      },
      modelData:[
        {text:'1',value:"王XX"},
        {text:'2',value:"112"},
        {text:'3',value:"12"},
        {text:'4',value:"L3"},
        {text:'5',value:"123"},
      ],
      dataList:{
        rows:[
          {name:'同利系列净值公告',purpose:'披露净值',prodCount:'8',status:'正常'},
        ]
      },
      dataList2:{
        rows:[

        ]
      },
      dataList3:{
        rows:[
          {prodCode:'TL001',prodName:'同利001'},
          {prodCode:'TL002',prodName:'同利002'},
          {prodCode:'TL003',prodName:'同利003'},
          {prodCode:'TL004',prodName:'同利004'},
          {prodCode:'TL005',prodName:'同利005'},
          {prodCode:'TL006',prodName:'同利006'},
          {prodCode:'TL007',prodName:'同利007'},
          {prodCode:'TL008',prodName:'同利008'},
        ]
      }
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    addProd(params){
      this.dataList2.rows.push(params);
      Tools.alert("添加成功","success");
      this.$refs.addPopup2.close();
      return false;
    },
    saveRule(params){
      Tools.alert("保存成功","success");
      this.$refs.addPopup.close();
      return false;
    },
    prod_code_change(val){
      for (var i = 0; i < this.$refs.prod_code.rows.length; i++) {
        if(this.$refs.prod_code.rows[i].prodCode == val){
          this.formData.prodName=this.$refs.prod_code.rows[i].prodName;
          break;
        }
      }
      this.formData.status="已成立";
    },
    openPopup2(){
      this.formData.adminUser="";
      this.formData.prodCode="";
      this.formData.prodName="";
      this.formData.status="";
    }
  }
}
</script>

<style scoped>

</style>
