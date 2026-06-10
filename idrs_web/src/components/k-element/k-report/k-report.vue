<template>
  <div class="k-report" v-bind:class="{'menu-mini': dataMenuResize && autoPosition}" ref="rpt">

    <iframe v-if="showReport" :id="dataMenuId" allowtransparenc="true" frameborder="0" z-index="-1"  :style="itemStyle" :width="dataWidth"
            :src="encodeURI(encodeURI(dataHtml + '?edit='+ dataEdit + '&menuid=' + dataMenuId + '&swkrntpomzqa=' + dataSwkrntpomzqa))"></iframe>
  </div>
</template>

<script>
  import eventBus from '@/utils/eventBus'

  export default {
    name: "KReport",
    props: {
      dataWidth: {
        type: String,
        default: '120%'
      },
      dataHeight: {
        type: String,
        default: '120%'
      },
      dataHtml: {
        type: String,
        default: ''
      },
      dataMenuId: {
        type: String,
        default: 'report'
      },
      dataEdit: {
        type: Boolean,
        default: false
      },
      dataSwkrntpomzqa: {
        type: String,
        default: ''
      },
      dataMenuResize: {
        type: Boolean,
        defalult: false
      }
    },
    data() {
      return {
        showReport: false,
        autoPosition: false
      }
    },
    computed: {
      itemStyle() {
        let ret = {}
        let btnEl = document.getElementById('report-btn')
        let height = window.innerHeight - document.getElementsByClassName('report-div')[0].offsetTop - (btnEl ? btnEl.offsetHeight : 0 )- 64
        ret.height = `${height}px`
        ret.position = "absolute;"
        ret.left = 0;
        ret.top = 0;
        ret['z-index'] = -1;
        return ret
      }
    },
    mounted() {
      let $this = this
      window.addEventListener('resize', function () {
        let btnEl = document.getElementById('report-btn')
        let height = window.innerHeight - document.getElementsByClassName('report-div')[0].offsetTop - (btnEl ? btnEl.offsetHeight : 0) - 64
        if (document.getElementById($this.dataMenuId)) {
          document.getElementById($this.dataMenuId).style.height = `${height}px`
        }
      })
      window.addEventListener("message", (e) => {
        if (e.data.xml) {
          this.$emit("saveReportXml", e.data)
        }
      }, false)

      eventBus.$on('updateReportPosition', item => {
        if(item == null){
          this.autoPosition = this.$sidebar.isMinimized
          if(this.$sidebar.isMinimized){  // 缩小以后

          }
        } else {
          this.autoPosition = item;
        }
      });
    },
    methods: {
      loadHandler() {
        this.showReport = true
      },
      closeHandler() {
        this.showReport = false
      },
      saveXmlHandler() {
        let report = document.getElementById(this.dataMenuId);
        report.contentWindow.saveEvent()
      },
      loadDataHandler(params) {
        this.showHandler()
        let report = document.getElementById(this.dataMenuId);
        report.contentWindow.loadDataEvent(params)
      },
      hideHandler() {
        let report = document.getElementById(this.dataMenuId);
        report.contentWindow.hideEvent()
      },
      showHandler() {
        let report = document.getElementById(this.dataMenuId);
        report.contentWindow.showEvent()
      },
    },

    watch: {
      $route(data) {
        // if (this.$route.params.id == 'M07PY01') {
        //   window.console.log('refs1:', this.$refs)
        //   this.showHandler()
        // } else {
        //   this.hideHandler()
        // }
      }
    }
  }
</script>

<style scoped>
.menu-mini {
  margin-left: 200px;
}
</style>
