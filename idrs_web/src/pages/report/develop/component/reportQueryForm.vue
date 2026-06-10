<template>
  <div>
    <k-form-search-customize :data-target="dataTarget" v-model="queryParam" @loadDataForTable="loadDataForTable">
      <div v-for="(item,index) in itemList" :key="index">
        <k-form-item :label="item.labelDiscrible">
          <component :is="item.cssClass" v-bind="item.options" :key="index"
                     v-model="queryParam[item.cssName]" @data-on-focus="focusItem"></component>
        </k-form-item>
      </div>
    </k-form-search-customize>
  </div>
</template>

<script>
  import httpUtil from "@/frame/httpUtil";
  import emitter from "@/components/k-element/common/k-emitter.js";

  export default {
    name: "ReportQueryForm",
    mixins: [emitter()],
    props: {
      dataForTable: {
        type: String,
        default: ''
      },
      dataTarget: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        queryParam: {},
        itemList: [],
      }
    },
    created() {
      this.getXmlHtml();
      if(this.$route.query!=null) {
        let params ={};
        let holdingDate = this.$route.query.holdingDate;
        if(holdingDate != null){
          this.$set(this.queryParam, "settle_date", holdingDate);
          params.settle_date = holdingDate;
          let timer = setTimeout(()=>{
            this.$nextTick(() => {
              this.loadDataForTable(params);
            });
          },2000)
        }
      }
    },
    mounted() {
      // this.focusItem()
    },
    methods: {
      getXmlHtml() {
        httpUtil.comnQuery({
          action: 'ReportCss.findHtmlInfo',
          params: {
            forTable: this.dataForTable,
          }
        }).then(res => {
          res.rows.map(row => {
            let options = {};
            Object.keys(row).forEach(key => {
              let reg = /^data/g;
              if (key.match(reg) && row[key] && row[key] !== '') {
                if (key === "dataMaxLength" || key === "dataMinLength") {
                  options[key] = parseInt(row[key])
                } else {
                  options[key] = row[key]
                }
                // 临时解决dataMaxValue和dataMinValue不兼容问题
                if (key === "dataMaxValue" || key === "dataMinValue") {
                  delete options[key]
                }
              }
            });
            options['id'] = row.id;
            row.options = options
          });
          this.itemList = res.rows
          let target = this.getParentRef(this.dataTarget);
          if (target && target.$options.name === "KReport") {
            target.loadHandler()
          } else {
            console.error("data-target不存在或data-target不是KReport组件.")
          }
        })
      },
      focusItem() {
        let target = this.getParentRef(this.dataTarget);
        if (target && target.$options.name === "KReport") {
          target.hideHandler()
        } else {
          console.error("data-target不存在或data-target不是KReport组件.")
        }
      },
      // blurItem(){
      //   let target = this.getParentRef(this.dataTarget);
      //   if(target && target.$options.name === "KReport"){
      //     target.showHandler()
      //   } else {
      //     console.error("data-target不存在或data-target不是KReport组件.")
      //   }
      // },
      loadDataForTable(data) {
        this.$emit("loadFormData", data)
      }
    }
  }
</script>

<style scoped>

</style>
