<template>
  <div>
    <div>
      <k-form-search data-model-name="Test4" data-target="test4Grid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addTest4Popup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="test4Grid" data-action="Test4.find">
        <k-grid-column data-header="登录名" data-name="name"></k-grid-column>
        <k-grid-column data-header="ss">
            <template slot-scope="scope">
                <span v-text="aa(scope.row.row)"></span>
            </template>
        </k-grid-column>
        <template slot="operate">
<!--          <k-btn class="md-info md-just-icon md-simple" data-descript="修改公告" data-functype="POPUP" data-size="mini"-->
<!--                 data-target="editOrgPopup" :data-handler="findAnnounceRoles">-->
<!--            <md-icon>edit</md-icon>-->
<!--          </k-btn>-->
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="Test4.delete" data-size="mini"
                 data-type="danger" data-target="test4Grid" data-descript="删除">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>
    <div>
      <!--    添加UserItem弹出框   -->
      <k-popup ref="addTest4Popup" data-title="新增">
        <k-form ref="addUserForm" :data-col="2">
          <k-form-item label="用户名">
            <k-field-text v-model="formData.name" :data-allowblank="false" :data-max-length="64"/>
          </k-form-item>
          <k-form-item label="id">
            <k-field-text v-model="formData.id" :data-allowblank="false" :data-max-length="64"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Test4.add" data-from="addUserForm"
                   :data-model="formData" data-target="test4Grid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
    </div>
  </div>
</template>

<script>
    export default {
        name: "test4",
        data(){
          return{
            formData:{

            }
          }
        },
        methods:{
          aa(row){
            return row.name
          }
        }
    }
</script>

<style scoped>

</style>
