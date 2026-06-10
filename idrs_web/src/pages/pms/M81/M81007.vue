<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="queryParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="queryParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="queryParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品形态">
        <k-field-select v-model="queryParam.prodMode" data-dict="t8_prod_create_type"></k-field-select>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="queryParam.prodStatus" data-dict="t8_prod_status"></k-field-select>
      </k-form-item>
      <k-form-item label="产品经理">
        <k-field-select v-model="queryParam.prodManageId" :data-params="{roleId:'3'}"
                        data-action="User.getUserByRoleId2" data-display-field="username"
                        data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="投资经理">
        <k-field-select v-model="queryParam.productInvManager" :data-params="{roleId:'14'}"
                        data-action="User.getUserByRoleId2" data-display-field="username"
                        data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="份额分类">
        <k-field-select v-model="queryParam.isShareSort" data-dict="1yes0no"></k-field-select>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="queryParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.prodCreateInfo={}"
             data-target="prodCreateInfoPopup"
             v-show="showIntiCreate" v-if="global.isShowAuthorityButton('T8ProdInfo.addT8ProdInfo')">
        <md-icon md-src="/static/svg/add.svg" />初创
      </k-btn>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.prodInfoCopy={}"
             data-target="prodCopyPopup"
             v-if="global.isShowAuthorityButton('T8ProdInfo.prodCopy')">
        <md-icon>content_copy</md-icon>
        克隆
      </k-btn>


    </k-form-search-customize>

    <k-grid ref="prodInfoGrid" data-action="T8ProdInfo.findTaProdInfo1" @data-row-select="selectRow"
            data-operate-column-position="end" data-align="center" data-operate-data-width="300px"
            data-operate-column="true" :dataAutoload="true">
      <k-grid-column data-header="id" data-hidden="true" data-name="id"/>
      <k-grid-column data-header="产品经理" data-name="prodManageName"/>
      <k-grid-column data-header="投资经理" data-name="productInvManager"/>
      <k-grid-column data-header="份额分类" data-name="isShareSort" data-dict="1yes0no"/>
      <k-grid-column data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-header="产品形态" data-name="prodMode" data-dict="t8_prod_create_type"/>
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
      <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status"/>
      <k-grid-column data-header="产品模型" data-name="prodModeId" data-hidden="true"/>
      <k-grid-column data-header="基本信息" data-name="prodinfo" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.prodinfo == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.prodinfo == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.prodinfo == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="托管信息" data-name="truteeinfo" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.truteeinfo == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.truteeinfo == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.truteeinfo == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="周期信息" data-name="prodcalendar" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.prodcalendar == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.prodcalendar == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.prodcalendar == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="销售信息" data-name="limitinfo" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.limitinfo == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.limitinfo == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.limitinfo == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="产品分红" data-name="prodbonus" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.prodbonus == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.prodbonus == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.prodbonus == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="产品投资" data-name="prodinvest" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.prodinvest == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.prodinvest == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.prodinvest == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="产品估值" data-name="prodvaluation" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;"
             v-if="props.row.row.prodvaluation == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.prodvaluation == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.prodvaluation == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="产品费用" data-name="prodfee" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.prodfee == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.prodfee == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.prodfee == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="交易费用" data-name="feedeal" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.feedeal == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.feedeal == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.feedeal == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="业绩报酬" data-name="performanceinfo" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;"
             v-if="props.row.row.performanceinfo == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.performanceinfo == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.performanceinfo == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="产品文档" data-name="proddocinfo" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;" v-if="props.row.row.proddocinfo == 3"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.proddocinfo == 2"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.proddocinfo == null"></div>
        </template>
      </k-grid-column>
      <k-grid-column data-header="份额分类" data-name="prodsharesort" data-align="center">
        <template slot-scope="props">
          <i class="el-icon-success" style="color: #00d400;font-size: 20px;"
             v-if="props.row.row.prodsharesort == 3 && props.row.row.isShareSort == 1"></i>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #007BFF;"
            v-if="props.row.row.prodsharesort == 2 && props.row.row.isShareSort == 1"></div>
          <div
            style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"
            v-if="props.row.row.prodsharesort == null || props.row.row.isShareSort == 0"></div>
        </template>
      </k-grid-column>

      <template slot="operate" slot-scope="props">
        <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP"
               data-size="mini" data-target="M8ProdGroupUserPopup" data-descript="用户设置"
               v-show="showSetUser" v-if="global.isShowAuthorityButton('T8ProdUser.addT8ProdUser2')">
          <md-icon>account_box</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="组件配置" v-show="showSetAssemble"
               @click="popupAddTemplate(props.row.row)"
               data-functype="POPUP" data-size="mini" v-if="global.getProdIfUser(props.row.row.id) &&
               global.isShowAuthorityButton('T8ProdSelectAssembly.addAssemblyProdInfoList')">
          <md-icon>settings_input_component</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="流程设置" v-show="showSetFlow"
               :data-handler="openProdProcess"
               data-size="mini" v-if="global.getProdIfUser(props.row.row.id)&&
               global.isShowAuthorityButton('T8ProdFlow.saveProcessTask')">
          <md-icon>brightness_high</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" v-show="props.row.row.prodStatus<6" data-descript="创设进度"
               :data-handler="openProdProgressRecord"
               data-functype="POPUP" data-size="mini" data-target="prodProgressPOPUP"
               v-if="global.getProdIfUser(props.row.row.id) && global.isShowAuthorityButton('T8ProdInfo.prodCreationProgress')">
          <md-icon>double_arrow</md-icon>
        </k-btn>

        <k-btn data-functype="PAGE" data-size="mini" class="md-info md-just-icon md-simple"
               :data-model="props.row.row" @click="toEditProdInfo(props.row.row)"
               :prodInfoId="props.row.row.id" data-descript="产品信息详情">
          <md-icon>library_books</md-icon>
        </k-btn>

        <!--        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"-->
        <!--               data-action="T8ProdInfo.deleteProdInfoForProdCode" data-size="mini"-->
        <!--               data-type="danger" data-target="prodInfoGrid" :data-confirm="true"-->
        <!--               data-descript="删除" v-if="global.isShowAuthorityButton('T8ProdInfo.deleteProdInfoForProdCode')"-->
        <!--            >-->
        <!--          <md-icon>close</md-icon>-->
        <!--        </k-btn>-->

      </template>
    </k-grid>

    <div>
      <k-popup ref="prodProgressPOPUP" data-title="产品创设进度" :dataDialogDrag="true">
        <div style="height: 400px; width: 880px; margin: auto;">
          <span v-for="item in prodProgressRecord" :key="item.assembly_id">
            <!-- 已完成 -->
            <div style="width: 200px;height: 90px;float: left;margin-left: 20px;margin-top: 20px;"
                 v-if="item.state == 3">
              <div
                style="width: 200px; height: 30px;background-color: #49BE71;border-radius: 5px 5px 0px 0px;text-align: center;">
                <i class="el-icon-success"
                   style="font-size: 20px;float: left;color: #ffffff;margin-top: 5px;margin-left: 10px;"></i>
                <div @click="popupEdit(item.assembly_id)" style="float:right;">
                  <i class="el-icon-edit-outline"
                     style="font-size: 20px;float: right;color: #ffffff;margin-top: 5px;margin-right: 10px;cursor:pointer;"></i>
                </div>
                <div style="line-height: 30px;">
                  <font color="white">{{item.assembly_desc}}</font>
                </div>
              </div>
              <div style="width: 200px; height: 30px;">
                <div
                  style="float: left;width: 80px; height: 30px;background-color: #e8e8e8;text-align: center;font-weight: 500;line-height: 30px;">操作人员</div>
                <div
                  style="float: left;width: 120px; height: 30px;background-color: #ffffff;text-align: left;border: 1px solid #e8e8e8;line-height: 30px;">{{item.crt_user}}</div>
                <div
                  style="float: left;width: 80px; height: 30px;background-color: #e8e8e8;text-align: center;border-radius: 0px 0px 0px 5px;font-weight: 500;line-height: 20px;">操作时间</div>
                <div
                  style="float: left;width: 120px; height: 30px;background-color: #ffffff;text-align: left;border-radius: 0px 0px 5px 0px;font-size: 12px;border: 1px solid #e8e8e8;line-height: 20px;">{{item.crt_date}} {{item.crt_time}}</div>
              </div>
            </div>
            <!-- 待录入 -->
            <div style="width: 200px;height: 90px;float: left;margin-left: 20px;margin-top: 20px;"
                 v-if="item.state == 2">
              <div
                style="width: 200px; height: 30px;background-color: #467DD6;border-radius: 5px 5px 0px 0px;text-align: center;">
                <div style="font-size: 20px;float: left;color: #ffffff;margin-top: 5px;margin-left: 10px;">
                  <div
                    style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: #467DD6; border: solid 2px; border-color: white;"></div>
                </div>
                <div @click="popupEdit(item.assembly_id)" style="float:right;">
                  <i class="el-icon-edit-outline"
                     style="font-size: 20px;float: right;color: #ffffff;margin-top: 5px;margin-right: 10px;cursor:pointer;"></i>
                </div>
                <div style="line-height: 30px;">
                  <font color="white">{{item.assembly_desc}}</font>
                </div>
              </div>
              <div style="width: 200px; height: 30px;">
                <div
                  style="float: left;width: 80px; height: 30px;background-color: #e8e8e8;text-align: center;font-weight: 500;line-height: 30px;">操作人员</div>
                <div
                  style="float: left;width: 120px; height: 30px;background-color: #ffffff;text-align: left;border: 1px solid #e8e8e8;line-height: 30px;">{{item.crt_user}}</div>
                <div
                  style="float: left;width: 80px; height: 30px;background-color: #e8e8e8;text-align: center;border-radius: 0px 0px 0px 5px;font-weight: 500;line-height: 20px;">操作时间</div>
                <div
                  style="float: left;width: 120px; height: 30px;background-color: #ffffff;text-align: left;border-radius: 0px 0px 5px 0px;font-size: 12px;border: 1px solid #e8e8e8;line-height: 20px;">{{item.crt_date}} {{item.crt_time}}</div>
              </div>
            </div>
            <!-- 不录入 -->
            <div style="width: 200px;height: 90px;float: left;margin-left: 20px;margin-top: 20px;"
                 v-if="item.state == 1">
              <div
                style="width: 200px; height: 30px;background-color: #b9b9b9;border-radius: 5px 5px 0px 0px;text-align: center;">
                <div style="font-size: 20px;float: left;color: #ffffff;margin-top: 5px;margin-left: 10px;">
                  <div
                    style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: #b9b9b9; border: solid 2px; border-color: white;"></div>
                </div>
                <i class="el-icon-edit-outline"
                   style="font-size: 20px;float: right;color: #ffffff;margin-top: 5px;margin-right: 10px;"></i>
                <div style="line-height: 30px;">
                  <font color="white">{{item.assembly_desc}}</font>
                </div>
              </div>
              <div style="width: 200px; height: 30px;">
                <div
                  style="float: left;width: 80px; height: 30px;background-color: #e8e8e8;text-align: center;font-weight: 500;line-height: 30px;">操作人员</div>
                <div
                  style="float: left;width: 120px; height: 30px;background-color: #ffffff;text-align: left;border: 1px solid #e8e8e8;line-height: 30px;">{{item.crt_user}}</div>
                <div
                  style="float: left;width: 80px; height: 30px;background-color: #e8e8e8;text-align: center;border-radius: 0px 0px 0px 5px;font-weight: 500;line-height: 20px;">操作时间</div>
                <div
                  style="float: left;width: 120px; height: 30px;background-color: #ffffff;text-align: left;border-radius: 0px 0px 5px 0px;font-size: 12px;border: 1px solid #e8e8e8;line-height: 20px;">{{item.crt_date}} {{item.crt_time}}</div>
              </div>
            </div>
          </span>
        </div>
      </k-popup>


      <k-popup ref="prodCreateInfoPopup" data-title="产品初创信息" :dataDialogDrag="true">
        <k-form ref="prodCreateInfoFrom" :data-col="2">
          <k-form-item label="产品管理人">
            <k-field-text v-model="prodCreateInfo.managerCode" :data-allowblank="false" data-disabled
                          :data-default-value="'光大理财有限责任公司'"/>
          </k-form-item>
          <k-form-item label="产品系列">
            <k-field-select v-model="prodCreateInfo.prodSeries" data-action="T8ProdInfo.getProdSeries"
                            data-display-field="seriesName" data-value-field="seriesCode"
                            :data-allowblank="false" @data-on-change="selectDataProdSeries(prodCreateInfo.prodSeries)"/>
          </k-form-item>
          <k-form-item label="产品子系列">
            <k-field-select v-model="prodCreateInfo.prodSonSeries" :data-data="prodSeriesSon"
                            data-display-field="seriesName" data-value-field="seriesCode"
                            :data-allowblank="true"></k-field-select>
          </k-form-item>
          <k-form-item label="产品形态">
            <k-field-select id="prod_mode" v-model="prodCreateInfo.prodMode"
                            data-dict="t8_prod_create_type"
                            :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="组件模型名称">
            <k-field-select id="originality" v-model="prodCreateInfo.prodModeId"
                            data-action="T8ProdModeInfo.findT8ProdModeInfos"
                            data-display-field="prodModeName" data-value-field="prodMode" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="产品品牌">
            <k-field-select v-model="prodCreateInfo.prodBrand" :data-allowblank="false" data-dict="t8_prod_brand"
                            data-default-value="1"/>
          </k-form-item>
          <k-form-item label="产品代码">
            <k-field-text v-model="prodCreateInfo.prodCode" data-validate-type="codeLetterLine" :data-max-length="20"
                          @data-on-blur="isExistsProd(prodCreateInfo.prodCode)" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="prodCreateInfo.prodName" :data-max-length="128"
                          @data-on-blur="isExistsProd(prodCreateInfo.prodName)" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="募集方式">
            <k-field-select v-model="prodCreateInfo.raiseType" :data-allowblank="false" data-dict="t8_raise_type"/>
          </k-form-item>
          <k-form-item label="是否关联创意">
            <k-field-select v-model="prodCreateInfo.isOriginality" :data-allowblank="false" data-dict="t8_prod_isok"/>
          </k-form-item>
          <k-form-item label="创意名称" v-if="prodCreateInfo.isOriginality == '1'">
            <k-field-select id="originality" v-model="prodCreateInfo.originalityId"
                            data-action="T8ProdInfo.getOriginality"
                            data-display-field="originalityName" data-value-field="originalityId"
                            :data-allowblank="prodCreateInfo.isOriginality == '0'"/>
          </k-form-item>
          <k-form-item label="是否份额分类">
            <k-field-radio v-model="prodCreateInfo.isShareSort" data-dict="1yes0no" :dataAllowblank="false"/>
          </k-form-item>

          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdInfo.addT8ProdInfo"
                   data-from="prodCreateInfoFrom" :data-handler="openProdProcess"
                   :data-model="prodCreateInfo" data-target="prodInfoGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>


    </div>


    <k-popup ref="prodProcessPopup" data-title="流程设置" :dataDialogDrag="true">
      <k-form ref="prodProcessForm" :data-col="1">
        <k-field-text v-model="prodProcessForm.prod_code" v-show="false"/>
        <k-form-item label="是否系列过会">
          <k-field-radio v-model="prodCreateInfo.isSeries" data-dict="1yes0no" data-default-value="0" @data-on-change="changeDefaultValue" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="产品系列" v-if="prodCreateInfo.isSeries == '1'">
          <k-field-select v-model="prodProcessForm.t8ProdSeriesId" data-action="T8Dict.findSonSeriesInfos"
                          data-display-field="seriesName" data-value-field="seriesCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="系列说明"
                     v-if="prodCreateInfo.isSeries == '1'" data-input-width="300px">
          <k-field-text v-model="prodProcessForm.seriesExplain" inputType="textarea" :rows="2" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="流程任务:">
          <k-field-select v-model="prodProcessForm.wf_flow_template_id" data-action="TaskFuncConfig.queryProcessTaskMod"
                          data-value-field="id" data-display-field="name" data-default-value="ff733c5eb9c047aba933ffdd7fbe78a5" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <div style="height:40px;margin:0 auto;padding-top: 10px;">
          <span style="color:red;font-size: 16px;">流程开启后,产品系列不支持修改,请悉知！！！</span>
        </div>

      </k-form>
      <div style="text-align: right;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="saveProdProcess"><md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </div>
    </k-popup>

    <k-popup ref="addtemplatePopup" data-title="组件配置" data-width="75%" class="popClass" :dataDialogDrag="true">
      <addtemplateComp :modValue="selectRowData"/>
    </k-popup>
    <k-popup ref="M8ProdGroupUserPopup" data-title="添加产品用户组" data-width="60%" class="addTaskPopup">
      <M8ProdGroupUser :t8ProdInfoId="formData.id"/>
    </k-popup>


    <k-popup ref="prodCopyPopup" data-title="产品克隆" :dataDialogDrag="true">
      <k-form ref="prodCopyForm" :data-col="2">
        <k-form-item label="克隆产品">
          <k-field-select v-model="prodInfoCopy.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="新产品代码">
          <k-field-text v-model="prodInfoCopy.prodCodeCopy" data-validate-type="codeLetterLine" :data-max-length="20"
                        @data-on-blur="isExistsProd(prodInfoCopy.prodCodeCopy)" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="新产品名称">
          <k-field-text v-model="prodInfoCopy.prodNameCopy" :data-max-length="128"
                        @data-on-blur="isExistsProd(prodInfoCopy.prodNameCopy)" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="PAGE" @click="popupEditCopy(prodInfoCopy)"
                 :data-model="prodInfoCopy">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>


</template>
<script>

    import Tools from '@/utils/tools.js';
    import {assign} from "lodash";
    import addtemplateComp from "./prodInfoAddAssembly";
    import M8ProdGroupUser from "./M8ProdGroupUser";

    export default {
        name: 'M81007',
        components: {addtemplateComp, M8ProdGroupUser},
        data() {
            return {
                prodSeriesSon: [],
                prodInfoCopy: {},
                queryParam: {},
                ProgressRecordPopup: {},
                prodProgressRecord_params: {},

                prodProcessForm: {
                    wf_flow_template_id: '',
                    isSeries: '',
                    prod_code: '',
                    t8ProdSeriesId:'',
                    seriesExplain:''
                },
                prodProgressRecord: [],
                prodSeries: '',
                prodRiskRat: {
                    prodInfoId: "",//产品ID
                    prodName: "",//产品名称
                    t8_risk_template_id: "",//模板ID
                    $RatGrid: null,//风险评分表格对象
                },
                prodCreateInfo: {},
                formData: {},
                selectRowData: {},
                prodCode: "",
                prodMode: "",
                showSetUser: true, //是否显示用户设置
                showIntiCreate: true,//是否显示初创按钮
                showSetAssemble: true,//是否显示设置组件
                showSetFlow: true,//是否展示设置流程
                showProcess: true,//是否展示创设进度按钮
            };
        },
        methods: {
            changeDefaultValue(value){
              //console.log("this.prodProcessForm.prodCode=:>>>",this.prodProcessForm.prod_code);
              if('0'!==value){
                this.httpUtil.comnQuery({
                  action: "T8ProdInfo.isExistsSeries",
                  params: {
                    prodCode: this.prodProcessForm.prod_code,
                    t8ProdSeriesId: this.prodProcessForm.t8ProdSeriesId
                  },
                }).then(data => {
                  if (data.rows.length > 0) {
                    this.prodProcessForm.wf_flow_template_id='37b8bce237b345edb35051c6511ee7e8';
                    this.prodCreateInfo.isSeries='1';
                    this.prodProcessForm.t8ProdSeriesId = data.rows[0].prodSonSeries;
                    this.prodProcessForm.seriesExplain = data.rows[0].seriesExplain;
                  }else{
                      if (this.prodProcessForm.t8ProdSeriesId == '' || this.prodProcessForm.t8ProdSeriesId == null || this.prodProcessForm.t8ProdSeriesId == undefined){
                          Tools.alert("该产品未绑定子系列,不可变更!", "danger");
                          this.prodCreateInfo.isSeries='0';
                          this.prodProcessForm.wf_flow_template_id='ff733c5eb9c047aba933ffdd7fbe78a5';
                      }else{
                          Tools.alert("该产品绑定子系列未上会通过!", "danger");
                          this.prodCreateInfo.isSeries='0';
                          this.prodProcessForm.wf_flow_template_id='ff733c5eb9c047aba933ffdd7fbe78a5';
                      }
                  }
                });
              }else{
                this.prodCreateInfo.isSeries='0';
                this.prodProcessForm.wf_flow_template_id='ff733c5eb9c047aba933ffdd7fbe78a5';
              }


              //console.log("wf_flow_template_id",this.prodProcessForm.wf_flow_template_id);
            },
            isExistsProd(code) {
                this.httpUtil.comnQuery({
                    action: "T8ProdInfo.isExistsProdCount",
                    params: {
                        prodCode: code, prodName: code
                    },
                }).then(data => {
                    if (data.rows.length > 0) {
                        Tools.alert("产品存在，请核对!", "danger");
                        this.$set(this.prodCreateInfo, 'prodName', '');
                        this.$set(this.prodCreateInfo, 'prodCode', '');

                        this.$set(this.prodInfoCopy, 'prodCodeCopy', '');
                        this.$set(this.prodInfoCopy, 'prodNameCopy', '');
                        return false;
                    }
                });
            },


            toEditProdInfo(row) {
                let pathUrl = '/main/pms/M81/prodDisplay/M81001display';
                this.$router.push({
                    path: pathUrl,
                    query: {
                        prodMode: row.prodMode, prodInfoId: row.id, prodCode: row.prodCode,
                        assemblyMenuType: '1', menuName: 'M81007', isShareSort: row.isShareSort
                    },
                });

            },

            popupEdit(assembly_id) {
                let row = this.ProgressRecordPopup;
                let pathUrl = '/main/pms/M81/prodInfoGD/M81001add';
                this.$router.push({
                    path: pathUrl,
                    query: {
                        prodMode: row.prodMode,
                        prodModeId: row.prodModeId,
                        findProdCode: row.prodCode,
                        assembly_id: assembly_id,
                        findProdName: row.prodCode,
                        findProdMode: row.prodMode,
                        prodInfoId: row.id,
                        prodCode: row.prodCode,
                        assemblyMenuType: '1',
                        menuName: 'M81007',
                        isShareSort: row.isShareSort,
                        modelId:row.modelId,
                    },
                });
            },


            popupEditCopy(val) {
                if (this.$refs.prodCopyForm.validate()) {
                    this.httpUtil.comnQuery({
                        action: "T8ProdInfo.findTaProdInfo",
                        params: {
                            prodCode: val.prodCode
                        },
                    }).then(data => {
                        let row = data.rows[0];
                        let pathUrl = '/main/pms/M81/prodInfoGD/M81001add';
                        this.$refs.prodCopyPopup.close();
                        this.$router.push({
                            path: pathUrl,
                            query: {
                                prodMode: row.prodMode, prodCode: row.prodCode, prodModeId: row.prodModeId,
                                prodCodeCopy: val.prodCodeCopy, prodNameCopy: val.prodNameCopy, assembly_id: 'prodInfo',
                                prodInfoId: row.id, assemblyMenuType: '1', menuName: 'M81007Copy',prodCreateInfo:row.prodCreateInfo

                            },
                        });
                    });
                }
            },

            //维护组件
            popupAddTemplate: function (value) {
                this.selectRowData = value;
                this.$refs.addtemplatePopup.popup();

            },

            //打开选择窗口
            openProdProcess(params) {
                this.httpUtil.comnQuery({
                    action: "T8ProdInfo.getProdProcessOpen",
                    params: {
                        prodCode: params.prodCode
                    },
                }).then(data => {
                    if (data.returndata.count > 0) { //已经开启流程直接调转到流程页面

                        this.$router.push({
                            path: "/main/flow/flowTemplateItem",
                            query: {
                                id: params.modelId,
                                prod_code: params.prodCode
                            }
                        })
                    } else {
                        //产品代码
                        this.prodProcessForm.prod_code = params.prodCode;
                        this.prodProcessForm.wf_flow_template_id = params.modelId;
                        this.prodProcessForm.t8ProdSeriesId = params.prodSonSeries;
                        this.$refs.prodProcessPopup.popup();
                    }
                    console.log(this.prodProcessForm.t8ProdSeriesId);
                });

            },
            //打开产品创设进度展示
            openProdProgressRecord(params) {
                this.ProgressRecordPopup = params;
                this.prodProgressRecord_params = params;
                this.httpUtil.comnQuery({
                    action: "T8ProdInfo.getProdProgressRecord",
                    params: params,
                    mask: true
                }).then(data => {
                    this.prodProgressRecord = data.rows;
                    if (params.isShareSort !== '' && params.isShareSort !== undefined) {
                        //用来单独判断产品创设进度能否点击份额分类组件
                        if (params.isShareSort == 0) {
                            for (let j = 0; j < this.prodProgressRecord.length; j++) {
                                if (this.prodProgressRecord[j].assembly_id == "ProdShareSort") {
                                    this.prodProgressRecord[j].state = "1";
                                    // this.$delete(this.prodProgressRecord,j);
                                }
                            }
                        }
                    }
                    //console.log(this.prodProgressRecord)
                });
                return true;
            },

            //保存流程任务模板
            saveProdProcess() {
                if (!this.prodProcessForm.wf_flow_template_id) {
                    Tools.alert("请先选择流程任务！！！")
                    return false;
                }
                this.httpUtil.comnQuery({
                    action: "T8ProdFlow.saveProcessTask",
                    params: this.prodProcessForm,
                    mask: true
                }).then(data => {
                    this.$refs.prodProcessPopup.close();
                    //打开编辑流程任务页面
                    this.$router.push({
                        path: "/main/flow/flowTemplateItem",
                        query: {
                            id: data.returndata.wf_flow_template_id,
                            prod_code: this.prodProcessForm.prod_code
                        }
                    })
                });
                return true;
            },


            selectRow(row, column, event) {
                const _this = this
                _this.formData = assign({}, row)
            },

            //根据系列查询子系列
            selectDataProdSeries(val) {
                this.httpUtil.comnQuery({
                    action: "T8ProdInfo.getProdSonSeries",
                    params: {
                        parentCode: val
                    }
                }).then(data => {
                    this.$set(this.prodCreateInfo, 'prodSonSeries', '');
                    this.prodSeriesSon = data.rows

                });

            },

        },
        created() {
            this.global.getProdUser('');
            this.$nextTick(() => {
                //接收产品运营导航跳转传参，加载表格数据 rennannan
                this.$refs.prodInfoGrid.load({
                    prodCode: this.$route.query.prod_code,
                    prodName: this.$route.query.findProdName,
                    prodMode: this.$route.query.findProdMode,
                    prodStatus: this.$route.query.findProdStatus
                });
                //获取需要隐藏按钮属性  rennannan 20210320   将接收到的属性值置为false达到隐藏按钮的效果
                this.global.getHideButtons(this);
            })
        },

    }

</script>

<style>
  .el-icon-color {
    color: #FF8C00;
  }
</style>
<style lang="scss" scoped>
  ::v-deep .step-content {
    display: flex;
    flex-direction: column;
    align-items: center;

    ::v-deep .el-step {
      cursor: pointer;
      width: 100px;

      .el-step__line {
        display: block !important;
      }

      .el-step__title {
        width: 100px;
      }
    }
  }

  ::v-deep .dropdown-menu {
    margin-top: 10px;
    right: auto;
  }

  ::v-deep .k-card {
    z-index: 0;
  }

  .el-table__expanded-cell {
    background-color: #F9F9F9 !important;
  }

  .el-table__expanded-cell:hover {
    background-color: #F9F9F9 !important;
  }

  .tool {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: -15px;
    margin-bottom: -20px;
    padding-top: 60px;
  }

  .row-tools {
    background: #FFFFFF;
    box-shadow: 0 8px 12px 0 rgba(0, 0, 0, 0.06);
    border-radius: 24px;
    height: 45px;
    //display: inline-block;
    align-items: center;
    //margin-top: 20px;
    text-align: center;
    //margin-left: -690px;
    position: absolute;
    left: 0;
    margin-top: -45px;
    margin-left: 30px;
  }

  .tools-text {
    font-family: PingFangSC-Regular;
    font-size: 10px;
    letter-spacing: 0;
    /*font-weight: 500;*/
    /*    margin-top: 15px;*/
    color: #707E8F;
  }

  .tool-item {
    margin-left: 25px;
    margin-right: 25px;
    margin-top: -1px;
    float: left;
  }

  .step-tools {
    margin-top: 60px;
    margin-bottom: 20px;
    align-items: center;
    display: inline-block;
  }

  .tool-item .md-icon {
    width: 15px;
    height: 15px;
    margin-top: -1px;
    margin-bottom: 6px;
  }

  .test {
    width: 0;
    height: 0;
    border-top: 70px solid transparent;
    border-right: 140px solid #6bbf20;
    border-bottom: 70px solid transparent;
  }

  .steps {
    display: flex;
    flex-direction: row;
    align-items: center;
    overflow-x: auto;
  }

  .step {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-left: 2px;
    padding-right: 4px;
  }

  .my-line {
    background-image: linear-gradient(90deg, #7FC7FF 0%, #35A7EF 100%);
    border-radius: 0 0 0 0;
    width: 156px;
    height: 6px;
    margin-top: 12px;
  }

  .my-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: -12px;
  }

  .my-number-content {
    top: 0;
    left: -15px;
    text-align: center;
    display: inline-block;
    height: 18px;
    width: 18px;
    color: #ffffff;
    background-color: #b9b9b9;
    line-height: 18px;
    border-radius: 50%;
    text-align: center;
    /*  border:1px solid;*/
    background-color: #4CA7EE;

  }

  .my-number {
    font-family: Arial-BoldMT;
    font-size: 12px;
    color: #ffffff;
    letter-spacing: 0;
    z-index: 2;
    /*margin-top: -2px;*/
    font-weight: 500;
  }

  .my-title {
    font-weight: 500;
    color: #3B4858;
    margin-top: 4px;
    font-size: 14px;
    font-family: PingFangSC-Medium;
  }

  .my-desc {
    height: 24px;
    color: #999999;
    margin-top: 1px;
  }

  .last-step-content {
    display: flex;
    flex-direction: row;
  }

  .my-delta {
    height: 10px;
    width: 0px;
    position: absolute;
    margin-left: 155px;
    margin-top: 8px;
    border-bottom: 10px solid #4CA7EE;
    border-right: 16px solid transparent;
  }

  .back-line-content {
    margin-bottom: -18px;
    margin-left: -250px;
    margin-right: -450px;
  }

  .my-back-line {
    background: #EDEDED;
    border-radius: 0px 0px 0px 0px;
    width: 100%;
    height: 6px;
    margin-top: -25px;
  }

  .popover-container {
    display: flex;
    flex-direction: column;
    margin-left: 10px;
  }

  .template {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .template-desc {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .template-btn {
    display: flex;
    flex-direction: row;
    margin-left: 10px;
  }

  .module {
    border: 1px solid #41A0EB;
    margin-left: 10px;
    padding: 1px 15px;
    border-radius: 2px;
    color: #41A0EB;
  }

  .task {
    display: flex;
    flex-direction: row;
    margin-top: 20px;
  }

  .task-item {

    display: inline-block;
    max-width: 420px;
    border-radius: 2px;
    border-radius: 2px;
    //width: 100%;
    height: 203px;
    //margin: 0 auto;
    margin: 0 5px 0 5px;
    text-align: left;
  }

  .task-box {
    margin-left: 7.5px;
    margin-right: 7.5px;
    width: 89.4px;
    height: 23px;
    position: relative;
    display: inline-block;
    // display: flex;
    // flex-direction: column;
    // height: 200px;
    //box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.14);
    border-radius: 2px;
    text-align: center;
    line-height: 25px;
    margin-top: 10px;
    background-repeat: no-repeat;
  }

  .task-desc {
    font-family: PingFangSC-Regular;
    font-size: 12px;
    color: #FFFFFF;
  }

  .el-popover .el-popper {
    top: 180px;
  }

  .tool-item:hover {
    cursor: pointer;
  }

  .tool-item:hover ::v-deep .md-icon svg > path {
    fill: #41A0EB;
  }

  .tool-item:hover span {
    color: #41A0EB;
  }

  .tool-disable,
  .tool-disable:hover {
    cursor: default;
  }

  .tool-disable ::v-deep .md-icon svg > path,
  .tool-disable:hover ::v-deep .md-icon svg > path {
    fill: #cccccc;
  }

  .tool-disable span,
  .tool-disable:hover span {
    color: #cccccc;
  }

  .prodModeCursor :hover {
    cursor: pointer;
  }

  .el-icon-circle-plus:before {
    margin-left: -15px;
    padding-right: 10px;
    //margin-top: 10px;
    padding-top: 3px;
  }

  .popClass ::v-deep .el-dialog {
    padding-top: 24px;
    margin-top: 30PX !important;
    margin-right: 3%;
  }

</style>
