<template>
  <div>
    <dynamic-component v-if="render" :key="viewId"></dynamic-component>
    <!-- <div id="dynamic-component-mount"></div> -->
  </div>
</template>

<script>
  import Vue from 'vue'
  import { parseComponent } from "@/pages/design/code-viewer/utils/sfcParser/parser";
  import { genStyleInjectionCode } from "@/pages/design/code-viewer/utils/sfcParser/styleInjection";
  import { isEmpty, extend, generateId } from "@/pages/design/code-viewer/utils/util";
  import { addStylesClient } from "@/pages/design/code-viewer/utils/style-loader/addStylesClient";
  import generateCode from '@/pages/design//utils/generateCode.js';

  import {
    assign
  } from "lodash";

  export default {
    components: {
    },
    data() {
      return {
        viewId: null,
        dynamicId: null,
        source: "",
        render: false,

        dynamicComponent: {
          component: {
            name: "dynamicComponent",
            template: "<div>Hello Vue.js!</div>",
          },
        },
      };
    },
    created(){
      this.viewId = `vcv-${generateId()}`; // vue-code-view => vcv
      this.stylesUpdateHandler = addStylesClient(this.viewId, {});

      console.log("  $route.params.id  ", this.$route.params.id)

      // 获取路由id, 根据id查询表单信息
      this.dynamicId = this.$route.params.id;

      // 根据json生成组件代码
      let source = `<template><div class='demo'>I am async! 生成代码</div></template>
      <script>console.log('1122')<\/script>
      <style>.demo{color:red}</style>`
      // let source = "";

      // 
      // this.genComponent(source);
      this.getDynamicFormInfo();
    },
    computed: {

    },
    methods: {
      genComponent(source) {
        if(source){
          const { template, script, styles, customBlocks, errors } = parseComponent(source);
          const templateCode = template ? template.content.trim() : ``;
          let scriptCode = script ? script.content.trim() : ``;
          const { styleCode, styleArray } = genStyleInjectionCode(styles);
  
          // 构建组件
          const demoComponent = {};
  
          // script
          if (!isEmpty(scriptCode)) {
            const componentScript = {};
            scriptCode = scriptCode.replace(
              /export\s+default/,
              "componentScript ="
            );
            eval(scriptCode);
            extend(demoComponent, componentScript);
          }

          demoComponent.template = templateCode;
  
          // 组件 style
          this.stylesUpdateHandler(styleArray);
          // Vue.component('dynamic-component', function (resolve, reject) { 
          //   // setTimeout(function () {
          //   //   // 向 `resolve` 回调传递组件定义
          //     resolve({
          //       template: templateCode
          //     })
          //   // }, 1000)
          // })
          // var Profile = Vue.extend({
          //   template: templateCode,
          // })
          // 创建 Profile 实例，并挂载到一个元素上。
          // new Profile().$mount('#dynamic-component-mount')

          extend(this.dynamicComponent, {
            name: this.viewId,
            component: demoComponent,
          });

          const renderComponent = this.dynamicComponent.component;
          Vue.component("dynamic-component",Vue.extend(renderComponent))

          this.render = true;
          this.viewId = generateId();
          
        } else {
          this.renderComponet(" 空组件 ");
          // Vue.component('dynamic-component', function (resolve, reject) { 
          //   // setTimeout(function () {
          //     // 向 `resolve` 回调传递组件定义
          //     resolve({
          //       template: '<div>动态表单渲染失败</div>'
          //     })
          //   // }, 1000)
          // })
        }
      },
      renderComponet(template){
        Vue.component('dynamic-component', function (resolve, reject) { 
            // setTimeout(function () {
              // 向 `resolve` 回调传递组件定义
              resolve({
                template: `<div>${template}</div>`
              })
            // }, 1000)
          })
        this.render = true;
        this.viewId = generateId();
      },
      async getDynamicFormInfo(){
        await this.httpUtil.comnQuery({
            action: "LowCodeConfig.findConfigById",
            params: {id: this.dynamicId},
            mask: true,
          }).then(data => {
            if(data.success){
              if(data.returndata.json){
                this.source = generateCode(JSON.parse(data.returndata.json));
                this.genComponent(this.source);
              } else {
                this.renderComponet("空组件");
              }
            }
          });
      }
    }
  };
</script>

<style lang="scss" scoped>

</style>
