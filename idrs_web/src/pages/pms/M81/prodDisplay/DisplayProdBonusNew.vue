<template>
  <div>
    	<k-form class="my-form " ref="addT8ProdBonusNewForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
			<k-form-item label="产品信息主表id" v-show="false">
	        	<k-field-text v-model="T8ProdBonus.t8ProdInfoId"/>
	     	</k-form-item>
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="T8ProdBonus.prodCode" :data-disabled="true"/>
      </k-form-item>
			<k-form-item label="分红方式">
	        	<k-field-select v-model="T8ProdBonus.bonusType" data-dict="t8_bonus_type" :data-disabled="true" :dataAllowblank="false"/>
	     	</k-form-item>
			<k-form-item label="分红频率" v-if="T8ProdBonus.bonusType != '5'">
				<k-field-select v-model="T8ProdBonus.bonusModel" data-dict="t8_prod_share_bonus_frequency" :dataAllowblank="false" :data-disabled="true"/>
			</k-form-item>
			<k-form-item label="产品成立日" v-if="T8ProdBonus.bonusModel == 4" key="establishDate">
				<k-field-date v-model="T8ProdBonus.establishDate" :dataAllowblank="false" data-type="date" :data-disabled="true" ></k-field-date>
			</k-form-item>

			<k-form-item label="首次分红日" v-if="T8ProdBonus.bonusModel == 5" key="startDate">
				<k-field-date v-model="T8ProdBonus.startDate" :dataAllowblank="false" data-type="date" :data-disabled="true"></k-field-date>
			</k-form-item>
			<div class="fc-button-group" style="float:left; padding: 0px 0px 20px 140px;" v-if="T8ProdBonus.bonusModel == 5 || T8ProdBonus.bonusModel == 4">
				<span>间隔</span>
				<span style="color: red">*</span>
				<k-field-text v-model="T8ProdBonus.cycleMonth" style="width: 65px;" data-validate-type="number"
							  data-min-value="1" data-max-value="100" :data-max-length="3" key="cycleMonth" :data-disabled="true"></k-field-text>
				<span>月，</span>
				<span style="color: red">*</span>
				<k-field-select v-model="T8ProdBonus.day" :data-data="openMonthlyOptions" style="width: 80px;"
								data-display-field="label" data-value-field="value" key="cycleDay" :data-disabled="true"/>
				<span>分红</span>
			</div>
			<div  class="fc-button-group1" style="float:left; padding: 0px 0px 20px 140px;" v-if="T8ProdBonus.bonusModel == 6">
				<span>分红日</span>
				<template>
					<k-btn class="md-info md-just-icon md-simple" data-descript="分红日设置" :data-handler="()=> this.showListWorkDay = true"
						   data-functype="POPUP" data-size="big" data-target="workdayPopup">
						<!--						<md-icon md-src="/static/svg/workday.svg" />-->
						<md-icon>add_to_queue</md-icon>
					</k-btn>
				</template>
			</div>
			<k-form-item label="分红频率说明" :data-col="2">
	        	<k-field-text v-model="T8ProdBonus.bonusFrequency" :data-max-length="4000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
			<k-form-item label="收益分配说明" :data-col="2">
	        	<k-field-text v-model="T8ProdBonus.distributeIncomeDesc" :data-max-length="4000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>

    	</k-form>

	  <!-- 工作日弹出框 -->
	  <div class="popClass">
		  <k-popup @data-closed="()=>{this.selectPgmno='';this.$refs.shareBonusDay.hackReset=false}"
				   @data-opened="()=>{this.$refs.shareBonusDay.hackReset=true;this.selectPgmno='1'}"
				   ref="workdayPopup" data-width="80%">
			  <div style="overflow: auto;padding-top: 20px">
				  <ShareBonusDay ref="shareBonusDay" :pgmno="selectPgmno" :t8ProdInfoId="T8ProdBonus.t8ProdInfoId" :isShowSave="false"></ShareBonusDay>
			  </div>
		  </k-popup>
	  </div>

  </div>
</template>

<script>
  import ShareBonusDay from "@/pages/pms/M81/prodInfoGD/ShareBonusDay.vue"

  export default {
    components: {ShareBonusDay},
    computed: {},
    model: {
      prop: 'T8ProdBonus',
      event: 'input'
    },
    props:{
      assemblyMenuType:'',
      T8ProdBonus: {

      },
      prodCode: {
        type: String,
        default: ''
      },
      t8ProdInfoId: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        selectRowData: {},
		  selectPgmno: '',
		  openMonthlyOptions: [
			  {value: '01',  label: '1号' },
			  {value: '02',  label: '2号' },
			  {value: '03',  label: '3号' },
			  {value: '04',  label: '4号' },
			  {value: '05',  label: '5号' },
			  {value: '06',  label: '6号' },
			  {value: '07',  label: '7号' },
			  {value: '08',  label: '8号' },
			  {value: '09',  label: '9号' },
			  {value: '10', label: '10号'},
			  {value: '11', label: '11号'},
			  {value: '12', label: '12号'},
			  {value: '13', label: '13号'},
			  {value: '14', label: '14号'},
			  {value: '15', label: '15号'},
			  {value: '16', label: '16号'},
			  {value: '17', label: '17号'},
			  {value: '18', label: '18号'},
			  {value: '19', label: '19号'},
			  {value: '20', label: '20号'},
			  {value: '21', label: '21号'},
			  {value: '22', label: '22号'},
			  {value: '23', label: '23号'},
			  {value: '24', label: '24号'},
			  {value: '25', label: '25号'},
			  {value: '26', label: '26号'},
			  {value: '27', label: '27号'},
			  {value: '28', label: '28号'},
		  ],
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.T8ProdBonus = Object.assign({}, row)
      },

      validateData() {
        return this.$refs.addT8ProdBonusNewForm.validate();
      }
    }
  };
</script>
