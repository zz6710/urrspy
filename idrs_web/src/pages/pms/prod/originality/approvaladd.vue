<template>
  <div>
    <k-form ref="updateProdCreateMeetingForm" :data-col="2" data-total-width="888px">
      <k-form-item label="会议名称">
        <k-field-text v-model="value.meetingName" :data-allowblank="false" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="会议地点">
        <k-field-text v-model="value.meetingAddress" :data-allowblank="false" :data-max-length="250"/>
      </k-form-item>
      <k-form-item label="会议日期">
        <k-field-date v-model="value.meetingDate" data-type="date" :data-allowblank="false"
                      data-date-format="yyyy-MM-dd" data-value-format="yyyy-MM-dd"></k-field-date>
      </k-form-item>
      <k-form-item label="会议时间">
        <k-field-time v-model="value.meetingTime"
                      data-value-format="HH" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="与会人" :data-max-length="255">
        <k-field-select
          v-model="value.participant"
          data-multiple="true"
          data-action="User.findUsers"
          data-value-field="username"
          data-display-field="username"
          :data-allowblank="false"
        ></k-field-select>
      </k-form-item>
      <k-form ref="updateProdCreateMeetingForm2" v-for="(item, index) in prodMeetingItems" :key="index" :data-col="3"
              data-input-width="150px"
              data-label-width="100px" data-total-width="1100px">
        <!--<k-form-item label="会议类型">
          <k-field-select v-model="item.t8DecisionType" ref="t8DecisionType" data-dict="t8_decision_type" data-disabled="false"
                          :dataAllowblank='false' data-default-value="1"/>
        </k-form-item>
        <k-form-item label="产品代码" v-show="item.t8DecisionType=='1'">
          <k-field-select v-model="item.t8ProdInfoId" data-action="T8Dict.findMeetProds"
                          :data-params="{'t8DecisionType':'1','prodSonStatus':'2,3'}"
                          data-display-field="prodCode,prodName" data-value-field="t8ProdInfoId" :data-multiple="true"/>
        </k-form-item>
        <k-form-item label="产品代码" v-show="item.t8DecisionType=='2' || item.t8DecisionType=='3'">
          <k-field-select v-model="item.t8ProdInfoId" data-action="T8Dict.findMeetProds"
                          :data-params="{'t8DecisionType':'2','prodSonStatus':'2,3'}"
                          data-display-field="prodCode,prodName" data-value-field="t8ProdInfoId" :data-multiple="true"/>
        </k-form-item>-->
        <k-form-item label="会议类型">
          <k-field-select v-model="item.t8DecisionType" ref="t8DecisionType" data-dict="t8_decision_type" data-disabled="false"
                          :dataAllowblank='false' @data-on-change="selectProdCode(item,index)"/>
        </k-form-item>
        <k-form-item label="产品代码" data-input-width="360px">
          <k-field-select v-model="item.t8ProdInfoId" :data-data="prodInfo"
                          :data-params="{'prodSonStatus':'2,3'}" :data-disabled="item.t8DecisionType == 3 ? true:false"
                          data-display-field="prodCode,prodName" data-value-field="t8ProdInfoId" :data-multiple="true"/>
        </k-form-item>
        <k-form-item label="产品系列">
          <k-field-select v-model="item.t8ProdSeriesId" data-action="T8Dict.findSonSeriesInfos1"
                          data-display-field="seriesName" data-value-field="seriesCode" ref="t8ProdSeriesId"
                          @data-on-change="changeSeriesExplain(item)" :data-disabled="item.t8DecisionType == 1 || item.t8DecisionType == 2 ? true:false "/>
        </k-form-item>
        <k-form-item label="系列说明" v-if="item.t8ProdSeriesId != null && item.t8ProdSeriesId != undefined && item.t8ProdSeriesId !=''" data-input-width="360px">
          <k-field-text v-model="item.seriesExplain" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增"
               @click="() => prodMeetingItems.push({})">
          <md-icon>add</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
          <md-icon md-src="/static/svg/delete.svg"/>
        </k-btn>
      </k-form>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="editBtn"
               :data-model="value" data-target="prodInfoGrid" :data-handler="setParams"><md-icon md-src="/static/svg/confirm.svg"/>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"/>取消
        </k-btn>
        <!--<k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="addBut"
               data-from="addCustomerInfoForm" :data-model="value" data-target="customerInfoGrid"
               :data-handler="idCodeData">
          <md-icon md-src="/static/svg/confirm.svg"/>
          确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"/>
          取消
        </k-btn>-->
      </k-form-footer>
    </k-form>

        <!--<div style="margin: 0 auto; width: 200px">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="setParams">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </div>-->
<!--    data-action="T8ProdCreateMeeting.addProdCreateMeeting2"-->
    <!--<k-form dataLabelWidth="110px" dataInputWidth="300px">
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="editBtn"
                :data-model="value" data-target="prodInfoGrid" :data-handler="setParams"><md-icon md-src="/static/svg/confirm.svg"/>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"/>取消
        </k-btn>
        &lt;!&ndash;<k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="addBut"
               data-from="addCustomerInfoForm" :data-model="value" data-target="customerInfoGrid"
               :data-handler="idCodeData">
          <md-icon md-src="/static/svg/confirm.svg"/>
          确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"/>
          取消
        </k-btn>&ndash;&gt;
      </k-form-footer>
    </k-form>-->
  </div>
</template>

<script>
    import Tools from '@/utils/tools.js';

    export default {
        props: {
            updSuccess: Function
        },
        data() {
            return {
                prodMeetingItems: [
                    {"t8ProdInfoId": ''},
                    {"t8DecisionType": ''},
                    {"t8ProdSeriesId": ''},
                    {"seriesExplain": ''}
                    ],
                prodInfo:{},
            }
        },
        methods: {
            validateData() {
                return this.$refs.updateProdCreateMeetingForm2.validate();
            },
            setParams: function (value) {
                console.log(this.prodMeetingItems)
                let flag = false;
                let index= -1;
                for (let i = 0;i< this.prodMeetingItems.length;i++) {
                    if ((this.prodMeetingItems[i].t8ProdInfoId == null||this.prodMeetingItems[i].t8ProdInfoId==''||this.prodMeetingItems[i].t8ProdInfoId==undefined) && (this.prodMeetingItems[i].t8ProdSeriesId == null||this.prodMeetingItems[i].t8ProdSeriesId == ''||this.prodMeetingItems[i].t8ProdSeriesId == undefined)){
                       flag = true;
                       if(index=-1){
                           index=i+1;
                       }
                    }
                    if(!flag){
                        flag = !this.$refs.updateProdCreateMeetingForm2[i].validate()
                    }
                }
                console.log("flag=:>>>",flag);
                if(flag){
                    this.$nextTick(()=>{
                        this.$refs.editBtn.setIconStyle(1, []);
                        Tools.alert("第"+index+"行，请选择产品或者系列！","danger");
                        return false;
                    });
                }else{

                    value["t8CreateRelations"] = JSON.stringify(this.prodMeetingItems);
                    this.httpUtil.comnUpdate({
                        action: 'T8ProdCreateMeeting.addProdCreateMeeting2',
                        params: value,
                        successAlert: true,
                    }).then(data => {
                        this.$refs.editBtn.setIconStyle(1, [])
                        this.$emit("closeAddPopup", data);
                    });
                }


                /*let a = value.t8ProdInfoId.split(",");
                let t8ProdCreateMeetingProds = [];
                if (a.length > 1) {
                    for (let i = 0; i < a.length; i++) {
                        let t8ProdCreateMeetingProd = {};
                        this.$set(t8ProdCreateMeetingProd, "t8ProdInfoId", a[i]);
                        t8ProdCreateMeetingProds.push(t8ProdCreateMeetingProd);
                    }
                } else {
                    let T8ProdCreateMeetingProd = {};
                    this.$set(T8ProdCreateMeetingProd, "t8ProdInfoId", prodMeetingItems.t8ProdInfoId);
                    t8ProdCreateMeetingProds.push(T8ProdCreateMeetingProd);
                }
                value["t8ProdCreateMeetingProds"] = JSON.stringify(this.t8ProdCreateMeetingProds);*/

            },
            check(link) {
                if (link.length < 8) {
                    return '插入失败,链接最小长度为8'
                } else {
                    return true
                }
            },

            deleteEvent(index) {
                if (this.prodMeetingItems.length > 1) {
                    this.prodMeetingItems.splice(index, 1);
                }
            },

            //根据 决议类型 查询 产品代码
            selectProdCode(value,index) {
                this.httpUtil.comnQuery({
                    action: "T8Dict.findMeetProds",
                    params: {
                        t8ProdInfoId:this.t8ProdInfoId,
                        t8DecisionType:value.t8DecisionType,
                    }
                }).then(data => {
                    this.prodInfo = data.rows;
                    this.$set(value, "t8ProdInfoId" ,'');
                    this.$set(value, "t8ProdSeriesId" ,'');
                    this.$set(value, "seriesExplain" ,'');
                });

            },
            changeSeriesExplain(value) {
                this.$set(value,'seriesExplain','');
            },

        },
        //重置页面展示;
        created() {
            this.prodMeetingItems = [{}]
        },
        computed: {
            value() {
                return this.$attrs.value
            }
        }
    };
</script>
