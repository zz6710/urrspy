<template>
  <div>
    <div style="min-height:225px;">
      <k-grid ref="t8ProdDocInfoGrid" @data-row-select="selectRow" :dataPopupAppendToBody="true">
        <k-grid-column data-header="文档类型" data-name="itemval" ></k-grid-column>
        <k-grid-column data-header="模板子版本" data-name="tempName"></k-grid-column>
        <k-grid-column data-header="文档版本" data-name="docVersion"></k-grid-column>
        <k-grid-column data-header="文档描述" data-name="docDesc"></k-grid-column>
        <k-grid-column data-header="关联模板操作时间" data-name="correlationTime"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple"  data-descript="预览文档模板信息" data-size="small"
                 :data-handler="previewPrintTempVersion"
                 data-functype="SUBMIT"
                 data-target="onlineEditPopup" v-model="scope.row.row">
            <md-icon>zoom_in</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="onlineEditPopup" data-width="90%"  :data-dialog-drag="true" :dataAppendToBody="true">
      <div class="edit">
        <div class="word">
          <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl" />
        </div>
      </div>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    model: {
      prop: 'T8ProdDocMods',
      event: 'input'
    },
    props:{
      T8ProdDocMods: {
        dataParams: [],
      },
    },
    data() {
      return {
        viewUrl: '',
        selectRowData: {},
        dataParams:[],
        T8ProdDocModsIn:{}
      };
    },
    mounted(){

    },
    methods: {
      uptDataParams(dataParams){
        for(let i = 0;i < dataParams.length; i++){
          this.$set(dataParams[i],'numId',i)
        }
        this.dataParams = dataParams;
        this.$set(this.$refs.t8ProdDocInfoGrid,'list',this.dataParams);
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.T8ProdDocModsIn = Object.assign({}, row)
      },

      previewPrintTempVersion(value){
        this.httpUtil.comnQuery({
          action:'T8OnlineWordValue.getMaxT8OnlineWordValueByT8PrintTempVersionId1',
          params: {t8PrintTempVersionId:value.t8PrintTempVersionId,}
        }).then(data => {
          this.$nextTick(()=>{
            if (data != null && data.rows.length > 0){
              let url = data.rows[0].viewUrl;
              window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
            }
          })
        }).catch({

        })
        return false;
      }
    },

    watch: {
      'T8ProdDocMods.dataParams': function (value) {
        this.uptDataParams(value);
      }
    },
    created() {
      this.$nextTick(() => {
        this.uptDataParams(this.T8ProdDocMods.dataParams);
      })
    }
  };
</script>
<style lang="scss" scoped>
  .edit{
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 600px;
    .word{
      width: 70%;
      iframe{
        width: 100%;
        height: 100%;
      }
    }
    .form{
      padding-left: 20px;
      width: 40%;
      overflow-y:auto;
      .form-item{
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        .form-item-span{
          margin-right: 5px;
          width: 100px;
          text-align: left;
        }
        .k-field-text{
          margin-left: 5px;
          width: 300px;
          height: 30px;
        }
      }
    }
  }

.add-btn-div{
  position: relative;
  z-index: 1;
}
.add-btn{
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76,175,80,0.14), 0 1px 10px 0 rgba(76,175,80,0.12), 0 2px 4px -1px rgba(76,175,80,0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}
</style>
