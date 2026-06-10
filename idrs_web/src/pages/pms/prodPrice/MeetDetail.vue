<template>
  <div>
    <k-grid ref="editAttachmentGrid"
            data-action="DocumentAttachment.findAttachments" :data-params="{parentId:meetId,attachmentType:'10'}"
            data-operate-column-position="end" :dataPopupAppendToBody="true" :data-autoload="false">
      data-align="center" data-operate-data-width="300px"
      data-operate-column="true" >
      <k-grid-column data-align="center" data-header="id" data-name="id" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="父级id" data-name="parentId" data-hidden="true"
                     :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
      <k-grid-column data-align="center" data-header="附件类型" data-name="attachment_type" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime" data-type="time"/>
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.fileName" data-confirm data-size="mini"
               class="md-info md-just-icon md-simple"
               data-target="prodInfoGrid"
               data-url="/download/server/PmsApp/documentCreatMeetingAttachment/downAttachment.json"
               data-descript="下载会议附件资料">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>
  </div>
</template>

<script>
export default {
  name: "MeetDetail.vue",
  props: {
    meetId: '',
  },
  data() {
    return {
      meet: {},//会议信息
    }
  },
  created() {
    this.$nextTick(() => {
      this.$refs.editAttachmentGrid.load({parentId: this.meetId, attachmentType: '10'});
    });
  },
  watch: {
    meetId(value) {
      if (value != '' && value != undefined) {
        this.$refs.editAttachmentGrid.load({parentId: value, attachmentType: '10'});
      } else {
        this.$refs.editAttachmentGrid.$data.list = [];
      }

    }
  },
  methods: {
    findMeetById(meetId) {
      this.httpUtil.comnQuery({
        action: 'QuotaMeeting.findQuotaMeetings',
        params: {
          id: meetId,
        },
      }).then(data => {
        if (data.rows.length > 0) {
          this.meet = data.rows[0];
        }
      });
    }
  },

}
</script>

<style scoped>

</style>
