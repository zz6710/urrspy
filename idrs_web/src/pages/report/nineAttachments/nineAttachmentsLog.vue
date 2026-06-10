
<template>
  <div>

    <k-form-search-customize data-model-name="NineAttachmentsLog" data-target="NineAttachmentsLogGrid" v-model="queryParam">
      <k-form-item label="报送日期" data-label-width="150px">
        <k-field-date v-model="prodSearchParam.reportSendDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
      </k-form-item>
      <k-form-item label="文件获取日期" data-label-width="150px">
        <k-field-select v-model="prodSearchParam.getFileDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCd" data-action="ProdInfoOds.findProdInfoOds" ref="prodCodeId"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" />
      </k-form-item>
      <k-form-item label="附件报送状态" data-label-width="150px">
        <k-field-select v-model="prodSearchParam.status" data-dict="wp_send_type"/>
      </k-form-item>
    </k-form-search-customize>
    <div>
      <k-grid ref="NineAttachmentsLogGrid" @data-row-select="selectRow" data-action="NineAttachmentsLog.findNineAttachmentsLogs"
              data-operate-column-position="first" data-operate-width="170px" :data-autoload="false">
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCd" data-width="250" ></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodNm"  data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="压缩文件名" data-name="zipNm" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="压缩文件路径" data-name="zipDir" data-width="250" data-hidden="true">></k-grid-column>
        <k-grid-column data-align="left" data-header="直连文件名" data-name="directZipNm" data-width="250" data-hidden="true">></k-grid-column>
        <k-grid-column data-align="left" data-header="直连文件路径" data-name="directZipDir" data-width="250" data-hidden="true">></k-grid-column>
        <k-grid-column data-align="left" data-header="报送日期" data-name="reportSendDate" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送时间" data-name="reportSendTime" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="文件获取日期" data-name="getFileDate" data-width="200" ></k-grid-column>
        <k-grid-column data-align="left" data-header="文件获取时间" data-name="getFileTime" data-width="200" ></k-grid-column>
        <k-grid-column data-align="left" data-header="附件报送状态" data-name="status" data-width="140" data-dict="nineAttachments_status"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn :ref="'NineAttachmentsLogDown_' + scope.row.row.row_index" slot="button" style="width: 120px" class="md-success" data-target="NineAttachmentsLogGrid"
                 data-descript="文件下载" data-size="small" @click="downZipFile(scope.row.row)">
            <md-icon>cloud_download</md-icon>
            文件下载
          </k-btn>

        </template>
      </k-grid>
    </div>

  </div>
</template>
import {assign} from "lodash";
import openWindow from "../../../utils/openWindow";
import Tools from "@/utils/tools";
<script>
import KFieldSelect from "@/components/k-element/k-field-select/k-field-select";
export default {
  name: "NineAttachmentsLog",
  components: {KFieldSelect},
  data () {
    return {
      uploadBeginDate: '',
      uploadQueryDate: '',
      activeName: '1',
      prodSearchParam: {
        tableName: '',
        dealDate:'',
        id:'',
      },
      formData: {},
      beginDate:'',
      directedData:{},
      nowDate:'',
    }
  },

  computed: {
    queryParam () {
      return {
        ...this.prodSearchParam
      }
    }
  },
  methods: {
    selectRow(row, column, event) {
      this.formData = Object.assign({}, row)

    },
    downZipFile(row) {
      const buttonRef = 'NineAttachmentsLogDown_' + row.row_index;
      console.log("buttonRef: ");
      console.log(buttonRef);
      this.$refs[buttonRef].setLoading(true);

      this.httpUtil.download({
        url: '/download/server/RptApp/nineAttachments/download.json',
        params: row,
        callback: () => {
          this.$refs[buttonRef].setLoading(false);
        }
      })
    },

  }

}
</script>

