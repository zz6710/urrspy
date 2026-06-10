<template>
  <md-card class="head-div " id="mainBody">
    <div class="share-container">
      <div class="pd-button" @click="submit">
        <div v-if="isActive" class="dot"></div>
        <md-icon md-src="/static/images/create/save5.svg" class="pd-icon-20"/>
        <div class="pd-text">保存</div>
      </div>
    </div>
    <!-- <md-card-header class="md-card-header-text md-card-header-green">
      <div class="card-icon" :style="iconStyle">
        <md-icon md-src="/static/svg/flow.svg"></md-icon>
      </div>
    </md-card-header> -->
    <div class="iconText">
      <div class="circleIcon text">
        <div class="circle"
             style="background-color: #FBBB56"></div>
        系统清算
      </div>
    </div>
    <!--展示div-->
    <div class="mainBody">
      <!--时序框-->
      <div class="bigTitle" style="position: absolute">时序</div>
      <div class="clear timeBox">
        <!--T+0-->
        <div class="plusTime firstPlus" @click="plusTime(true,0)"></div>
        <!--        <draggable :options="{animation:500}" v-model="firstDay" handle=".moverArrow" group="clearGroup">
                  <transition-group>-->
        <div class="row1" v-for="(item,index) in firstDay " :key="item.id">
          <div class="time">
            <div style="display: inline-block;position: relative">
              <el-time-picker @change="change(item)" v-model="firstDay[index].time" :ref="'itemF'+index"
                              @focus="timeRange(true,index)"
                              format="HH:mm" value-format="HH:mm"
                              :picker-options="{selectableRange: startTime + ' - '+ endTime}"></el-time-picker>
              <img class="garbage" @click="deleteTime(true,index)" src="/static/images/clear/normal.png"/>
            </div>
            <div class="plusTime plusTimeItem" @click="plusTime(true,index+1)"></div>
          </div>
          <div class="ClearGroupEditList">
            <div class="plusGloup" @click="addClearGroup(false,index,0,true)" slot="reference"></div>
            <div class="middleLine"></div>

            <ClearGroupEdit class="item1" v-for="(child,i) in item.rows" :minuGroup="minuGroup" :available="true"
                            :addClearGroup="addClearGroup" :memberPopup="memberPopup" :taskPopup="taskPopup"
                            :clearGroup="child" :isFirstDay="true" :x="i" :y="index" :editGroup="editGroup"
                            :key="child.taskGroup"></ClearGroupEdit>
          </div>
        </div>
        <!--          </transition-group>
                </draggable>-->
        <!--零点线-->
        <div class="line">
          <div class="lineOne">零点线</div>
          <div class="lineTwo"></div>
          <hr class="lineThree">
          <div class="lineFour"></div>
          <div class="linefive"></div>
        </div>
        <!--T+1-->
        <div class="plusTime firstPlus" @click="plusTime(false,0)"></div>
        <div class="row1" v-for="(item,index) in secondDay " :key="item.id">
          <div class="time">
            <div style="display: inline-block;position: relative">
              <el-time-picker @change="change(item)" v-model="item.time" :ref="'itemS'+index" format="HH:mm"
                              @focus="timeRange(false,index)" value-format="HH:mm"
                              :picker-options="{selectableRange: startTime + ' - '+ endTime,format: 'HH:mm'}"></el-time-picker>
              <img class="garbage" @click="deleteTime(false,index)" src="/static/images/clear/normal.png"/>
            </div>
            <div class="plusTime plusTimeItem" @click="plusTime(false,index+1)"></div>
          </div>
          <div class="ClearGroupEditList">
            <div class="plusGloup" @click="addClearGroup(false,index,0,false)" slot="reference"></div>
            <div class="middleLine"></div>
            <ClearGroupEdit class="item1" v-for="(child,i) in item.rows" :minuGroup="minuGroup" :available="true"
                            :addClearGroup="addClearGroup" :memberPopup="memberPopup" :taskPopup="taskPopup" :editGroup="editGroup"
                            :clearGroup="child" :isFirstDay="false" :x="i" :y="index" :key="child.taskGroup"></ClearGroupEdit>
          </div>
        </div>
      </div>

      <!--非时序框-->
      <div class="clear notTimeBox">
        <div class="bigTitle">非时序</div>
        <div class="notTimeList">
          <ClearGroupEdit class="item3" v-for="(child,index) in noTimeList" :clearGroup="child" :minuGroup="minuGroup"
                          :memberPopup="memberPopup" :taskPopup="taskPopup" :available="true" :editGroup="editGroup"
                          :noTime="true" :key="child.taskGroup"></ClearGroupEdit>
          <div class="plusGloup noTimePlus" @click="addNoTimeClearGroup(true)" slot="reference"></div>
        </div>
      </div>
      <div class="G6" id="mountNode"></div>
    </div>

    <k-popup ref="addClearGroup" data-title="添加清算组">
      <k-form ref="addForm" :data-col="1">
        <k-form-item label="清算任务类型">
          <k-field-select v-model="editTasktype" data-placeholder="请选择清算组类型"  data-dict="task_type" data-default-value="1" data-disabled/>
        </k-form-item>
        <k-form-item label="所属模块">
          <k-field-select v-model="taskModel" data-dict="task_model" :data-allowblank="false"/></k-form-item>

        <K-form-item label="清算批次名称">
          <k-field-text :data-allowblank="false" @data-on-blur="checkTaskGloupName" v-model="editGroupName"
                        :data-max-length="64"/>
        </K-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-handler="execPlusClearGroup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="addTaskPopup" data-title="任务配置" data-width="950px" class="addTaskPopup">
      <addTaskComp :editActive4="editActive1" :available="true" :closePopupMethod="closePopup" :groupValue="clearGroup"/>
    </k-popup>

    <k-popup ref="memberPopup" data-title="批次成员" data-width="838px">
      <memberComp :editActive4="editActive1" :available="true" :closePopupMethod="closePopup" :groupValue="clearGroup"/>
    </k-popup>

    <k-popup ref="editClearGroup" data-title="修改清算组">
      <k-form ref="editForm" :data-col="1">
        <k-form-item label="清算任务类型">
          <k-field-select v-model="editTasktype" data-placeholder="请选择清算组类型"  data-dict="task_type" data-default-value="1" data-disabled/>
        </k-form-item>
        <k-form-item label="所属模块">
          <k-field-select v-model="taskModel" data-dict="task_model" :data-allowblank="false"/></k-form-item>
        <K-form-item label="清算批次名称">
          <k-field-text :data-allowblank="false" @data-on-blur="checkTaskGloupName" v-model="editGroupName"
                        :data-max-length="64"/>
        </K-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-handler="execEditClearGroup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </md-card>
</template>

<script>
import ClearGroupEdit from "@/pages/batch/TA5015-ClearGroupEdit";
import {Graph, Node, Shape, Edge, EdgeView} from '@antv/x6';
import moment from 'moment';
import Tools from "@/utils/tools";
import memberComp from "./TA5005Member";
import addTaskComp from "./TA5005AddTask";
import JSZip from "jszip"

export default {
  name: "TA5015Edit",
  components: {
    ClearGroupEdit,
    memberComp,
    addTaskComp
  },
  data() {
    return {
      taskModel : '', //添加根据模块反显任务   axin  20220720
      clearGroupName: '',
      selectDate: '',
      startTime: '00:00:00',
      endTime: '20:59:00',
      firstDay: [],
      secondDay: [],
      noTimeList: [],
      init: false,
      lock: false,
      formData: {},
      editGroupName: '',
      editTasktype: '1',
      dictParams: '',
      clearNames: [],
      deleteLock: false,
      map: {},
      isActive: false,
      devGraph: '',
      clearGroup: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      transaction: {
        callback: false,
        firstDay: '',
        secondDay: ''
      },
      focusEvent: {
        execute: false,
        name: ''
      }
    }
  },
  methods: {
    memberPopup(clearGroup){
      this.clearGroup = clearGroup;
      this.$refs.memberPopup.popup();
    },
    taskPopup(clearGroup) {
      this.clearGroup = clearGroup;
      this.$refs.addTaskPopup.popup();
    },
    editGroup(clearGroup) {
      this.clearGroup = clearGroup;
      this.clearGroupName = clearGroup.taskGroupName;
      this.editTasktype = clearGroup.execTaskType;
      this.taskModel = clearGroup.taskModel;
      this.editGroupName = clearGroup.taskGroupName;
      this.$refs.editClearGroup.popup();
    },
    closePopup: function (popupName) {
      this.$refs[popupName].close();
    },
    execEditClearGroup(){
      let ret = this.$refs.editForm.validate();
      if (!ret) {
        return false;
      }
      this.minuGroupName(this.clearGroup.taskGroupName,this.editGroupName);
      this.clearGroup.taskGroupName = this.editGroupName;
      this.clearGroup.taskModel = this.taskModel;
      this.editActive1();
      this.$refs.editClearGroup.close();
      this.init = true;
      this.initTaskAndMemberChange(this.clearGroup);
    },
    change(item) {
      if (!this.isActive) {
        this.isActive = true;
      }
      //将该时间段的shouldexectime设置为新值
      var rows = item.rows;
      var shouldExecTime = item.time.replace(":", "") + "00";
      rows.forEach(res => {
        res.shouldExecTime = shouldExecTime;
      });
    },
    initG6() {
      // 高亮
      const magnetAvailabilityHighlighter = {
        name: 'stroke',
        args: {
          attrs: {
            fill: '#fff',
            stroke: 'red',
          },
        },
      }
      let graph = new Graph({
        container: document.getElementById('mountNode'),
        width: document.getElementById("mainBody").clientWidth - 2,
        height: (this.firstDay.length + this.secondDay.length) * 131 + 130,
        grid: false,
        interacting: {
          nodeMovable: false
        },
        highlighting: {
          magnetAvailable: magnetAvailabilityHighlighter,
          magnetAdsorbed: {
            name: 'stroke',
            args: {
              attrs: {
                fill: '#fff',
                stroke: '#31d0c600',
              },
            },
          },
        },
        connecting: {
          snap: true,
          allowBlank: false,
          allowLoop: false,
          highlight: true,
          allowEdge: false,
          connector: 'rounded',
          connectionPoint: 'boundary',
          router: {
            name: 'metro'
          },
          createEdge() {
            return new Shape.Edge({
              attrs: {
                line: {
                  stroke: '#00BCD4',
                  strokeWidth: 2,
                  targetMarker: {
                    name: 'classic',
                    size: 7,
                  },
                },
              },
            })
          },
          //去除左入链接桩和上入链接桩的点击事件
          validateMagnet({magnet}) {
            return (magnet.getAttribute('port-group') !== 'inT' && magnet.getAttribute('port-group') !== 'inL');
          },
          validateConnection({sourceMagnet, targetMagnet}) {
            if (!targetMagnet || !sourceMagnet) {
              return false
            }
            //source的port组
            var gS = sourceMagnet.getAttribute('port-group');
            //target的组别
            var gT = targetMagnet.getAttribute('port-group');

            // 只能从输出链接桩创建连接
            if (!sourceMagnet || sourceMagnet.getAttribute('port-group') === 'inT' || sourceMagnet.getAttribute('port-group') === 'inL') {
              return false
            }
            // 只能连接到输入链接桩
            if (targetMagnet.getAttribute('port-group') !== 'inT' && targetMagnet.getAttribute('port-group') !== 'inL') {
              return false
            }

            //source的行数
            var yS = sourceMagnet.getAttribute('port').split(',')[2];
            //source的自然日
            var dS = sourceMagnet.getAttribute('port').split(',')[4];
            //target的行数
            var yT = targetMagnet.getAttribute('port').split(',')[2];
            //target的自然日
            var dT = targetMagnet.getAttribute('port').split(',')[4];

            //设置一些标志位
            var flag1 = true;
            var flag2 = true;

            //如果是点击group为outR的链接桩,则只能选择同行的rect
            if (gS === 'outR') {
              if (gT === 'inT') {
                return false;
              }
              //判断是否属于同一行
              flag1 = yS === yT;
              //判断是否属于同一自然日
              flag2 = dS === dT;
              if (!flag1 || !flag2) {
                return false;
              }
            } else {
              if (gT === 'inL') {
                return false;
              }
              //如果点击group为outB的链接桩,则只能连接下面行的rect
              if (parseInt(yS) >= parseInt(yT) && parseInt(dS) >= parseInt(dT)) {
                return false;
              }
            }
            //source的清算组名
            var gNT = targetMagnet.getAttribute('port').split(',')[0];
            //target的列数
            var xS = sourceMagnet.getAttribute('port').split(',')[3];
            //判断是否循环依赖
            var preTaskGroup = "";
            if (dS === "0") {
              preTaskGroup = (this.firstDay[(parseInt(yS))].rows[(parseInt(xS))]).preTaskGroup;
            } else {
              preTaskGroup = (this.secondDay[(parseInt(yS))].rows[(parseInt(xS))]).preTaskGroup;
            }
            if (preTaskGroup !== 'null' && preTaskGroup.indexOf(gNT) !== -1) {
              return false;
            }
            //source的清算组名
            var gNS = sourceMagnet.getAttribute('port').split(',')[0];
            //target的列数
            var xT = targetMagnet.getAttribute('port').split(',')[3];
            //去除重复加依赖
            if (dT === "0") {
              preTaskGroup = (this.firstDay[(parseInt(yT))].rows[(parseInt(xT))]).preTaskGroup;
            } else {
              preTaskGroup = (this.secondDay[(parseInt(yT))].rows[(parseInt(xT))]).preTaskGroup;
            }
            if (preTaskGroup !== 'null' && preTaskGroup.indexOf(gNS) !== -1) {
              return false;
            }
            return flag1 && flag2;
          }
        }
      })
      graph.firstDay = this.firstDay;
      graph.secondDay = this.secondDay;
      //鼠标悬浮箭头上事件
      graph.on('edge:mouseenter', ({edge}) => {
        edge.addTools([
          {
            name: 'button-remove',
            args: {
              distance: -30,
            },
          },
        ])
      })
      //鼠标离开箭头事件
      graph.on('edge:mouseleave', ({edge}) => {
        edge.removeTools();
      })
      //添加箭头事件
      graph.on('edge:connected', ({isNew, edge}) => {
        if (isNew) {
          this.lock = true;
          var arr1 = edge.source.port.split(',');
          var arr = edge.target.port.split(',');
          var sourceId = edge.source.cell;
          var taskGroup = {};
          if (arr[4] === '0') {
            taskGroup = this.firstDay[parseInt(arr[2])].rows[parseInt(arr[3])];
          } else {
            taskGroup = this.secondDay[parseInt(arr[2])].rows[parseInt(arr[3])];
          }
          var sourceClearGroup = {};
          if (arr1[4] === '0') {
            sourceClearGroup = this.firstDay[parseInt(arr1[2])].rows[parseInt(arr1[3])];
          } else {
            sourceClearGroup = this.secondDay[parseInt(arr1[2])].rows[parseInt(arr1[3])];
          }
          if (taskGroup.lastTaskGroup.indexOf(sourceId) === -1) {
            //开启事务
            this.transaction.callback = false;
            this.transaction.firstDay = JSON.stringify(this.firstDay);
            this.transaction.secondDay = JSON.stringify(this.secondDay);
            taskGroup.lastTaskGroup = (taskGroup.lastTaskGroup === 'null' || taskGroup.lastTaskGroup == "") ? sourceId : (taskGroup.lastTaskGroup + ',' + sourceId);
            this.addPreDeploy(sourceId + ((sourceClearGroup.preTaskGroup === '' || sourceClearGroup.preTaskGroup === 'null') ? '' : (',' + sourceClearGroup.preTaskGroup)), taskGroup);
            //若发生重复的,则回滚
            this.transaction.callback= false;//去掉存在环限制
            if (this.transaction.callback) {
              this.firstDay = JSON.parse(this.transaction.firstDay);
              this.secondDay = JSON.parse(this.transaction.secondDay);
              this.transaction.callback = false;
              Tools.alert("存在环!!!请检查后,再添加该前置关系", "danger");
              this.initMap();
              this.reloadG6();
            } else {
              var count = 0;
              if (this.map[sourceId] === undefined) {
                this.map[sourceId] = [];
              } else {
                for (var i = 0; i < this.map[sourceId].length; i++) {
                  if (this.map[sourceId][i].taskGroup === taskGroup.taskGroup) {
                    count++;
                  }
                }
              }
              if (count === 0) {
                this.map[sourceId].push(taskGroup);
              }
              graph.firstDay = this.firstDay;
              graph.secondDay = this.secondDay;
            }
          }
          if (!this.isActive) {
            this.isActive = true;
          }
          this.lock = false;
        }
      })
      //删除箭头事件
      graph.on('edge:removed', ({edge}) => {
        if (!this.deleteLock) {
          var arr1 = edge.source.port.split(',');
          var sourceId = edge.source.cell;
          if (edge.target.port !== undefined && !this.lock) {
            var arr = edge.target.port.split(',');
            var taskGroup = {};
            if (arr[4] === '0') {
              taskGroup = this.firstDay[parseInt(arr[2])].rows[parseInt(arr[3])];
            } else {
              taskGroup = this.secondDay[parseInt(arr[2])].rows[parseInt(arr[3])];
            }
            var sourceClearGroup = {};
            if (arr1[4] === '0') {
              sourceClearGroup = this.firstDay[parseInt(arr1[2])].rows[parseInt(arr1[3])];
            } else {
              sourceClearGroup = this.secondDay[parseInt(arr1[2])].rows[parseInt(arr1[3])];
            }
            var temp = this.minusString((taskGroup.lastTaskGroup), sourceId);
            taskGroup.lastTaskGroup = temp;
            if (this.map[sourceId].length === 1) {
              this.map[sourceId] = undefined;
            } else {
              for (var i = 0; i < this.map[sourceId].length; i++) {
                if (this.map[sourceId][i].taskGroup === taskGroup.taskGroup) {
                  this.map[sourceId] = this.arrayMinu(i, this.map[sourceId]);
                }
              }
            }
            var arr2 = [];
            if (sourceClearGroup.preTaskGroup !== '' && sourceClearGroup.preTaskGroup !== 'null') {
              arr2 = sourceClearGroup.preTaskGroup.split(',');
            }
            arr2.push(sourceId);
            this.deleteDeploy(arr2, taskGroup);
            graph.firstDay = this.firstDay;
            graph.secondDay = this.secondDay;
          }
        }
      });


      //第一天节点布置
      for (var i = 0; i < this.firstDay.length; i++) {
        for (var j = 0; j < this.firstDay[i].rows.length; j++) {
          var temp = this.firstDay[i].rows[j];
          graph.addNode({
              id: temp.taskGroup,
              x: 234 + j * 256,
              y: 79 + 130 * i,
              width: 148,
              height: 81,
              attrs: {
                body: {
                  fill: '#ffffff00',
                  stroke: '#ffff0000',
                },
              },
              ports: {
                groups: {
                  outR: {
                    position: {
                      name: 'absolute',
                      args: {x: 136, y: 35}
                    },
                    attrs: {
                      circle: {
                        r: 6,
                        magnet: true,
                        stroke: '#31d0c6',
                        strokeWidth: 2,
                        fill: '#fff'
                      }
                    }
                  },
                  inT: {
                    position: {
                      name: 'absolute',
                      args: {x: 70, y: 8},
                    },
                    attrs: {
                      circle: {
                        r: 6,
                        magnet: true,
                        stroke: 'red',
                        strokeWidth: 2,
                        fill: '#fff',
                        style: {
                          visibility: 'hidden',
                        }
                      }
                    }
                  },
                  inL: {
                    position: {
                      name: 'absolute',
                      args: {x: 8, y: 35},
                    },
                    attrs: {
                      circle: {
                        r: 6,
                        magnet: true,
                        stroke: 'red',
                        strokeWidth: 2,
                        fill: '#fff',
                        style: {
                          visibility: 'hidden',
                        }
                      }
                    }
                  },
                  outB: {
                    position: {
                      name: 'absolute',
                      args: {x: 70, y: 69},
                    },
                    attrs: {
                      circle: {
                        r: 6,
                        magnet: true,
                        stroke: '#31d0c600',
                        strokeWidth: 2,
                        fill: '#fff'
                      }
                    }
                  }
                },
                items: [
                  {
                    id: temp.taskGroup + ',R,' + i + ',' + j + ',' + 0,
                    group: 'outR',
                    markup: [
                      {
                        tagName: 'path',
                        selector: 'path',
                      },
                    ],
                    zIndex: 3,
                    attrs: {
                      path: {
                        d: 'M12,0 a 12 12 0 0 0 -12 -12 l0 24 a 12 12 0 0 0 12 -12 ',
                        magnet: true,
                        fill: '#044B9400',
                        stroke: '#31d0c600',
                      }
                    }
                  },
                  {
                    id: temp.taskGroup + ',L,' + i + ',' + j + ',' + 0,
                    group: 'inL'
                  },
                  {
                    id: temp.taskGroup + ',T,' + i + ',' + j + ',' + 0,
                    group: 'inT'
                  },
                  {
                    id: temp.taskGroup + ',B,' + i + ',' + j + ',' + 0,
                    group: 'outB',
                    markup: [
                      {
                        tagName: 'path',
                        selector: 'path',
                      },
                    ],
                    attrs: {
                      path: {
                        d: 'M0,12 a 12 12 0 0 0 12 -12 l -24 0 a 12 12 0 0 0 12 12',
                        magnet: true,
                        fill: '#044B9400',
                        stroke: '#00000000',
                      }
                    }
                  },
                ]
              }
            }
          )
        }
      }
      for (var i = 0; i < this.firstDay.length; i++) {
        for (var j = 0; j < this.firstDay[i].rows.length; j++) {
          var temp = this.firstDay[i].rows[j];
          if (temp.lastTaskGroup !== "null" && temp.lastTaskGroup !== undefined && temp.lastTaskGroup !== "") {
            var arr = temp.lastTaskGroup.split(",");
            var y1 = graph.getCellById(temp.taskGroup).port.ports[0].id.split(',')[2];
            for (var z = 0; z < arr.length; z++) {
              var y2 = graph.getCellById(arr[z]).port.ports[0].id.split(',')[2];
              var isFD = graph.getCellById(arr[z]).port.ports[0].id.split(',')[4];
              var i1 = graph.getCellById(arr[z]).port.ports[0].id.split(',')[3];
              graph.addEdge({
                source: {
                  cell: arr[z],
                  port: (arr[z] + ',' + (y1 === y2 ? 'R' : 'B') + ',' + y2 + ',' + i1 + ',' + 0)
                },
                target: {
                  cell: temp.taskGroup,
                  port: (temp.taskGroup + ',' + (y1 === y2 ? 'L' : 'T') + ',' + i + ',' + j + ',' + 0)
                },
                router: {
                  name: 'metro'
                },
                attrs: {
                  line: {
                    stroke: '#00BCD4',
                  },
                },
              })
            }
          }
        }
      }
      /*第二天节点布置*/
      for (var i = 0; i < this.secondDay.length; i++) {
        for (var j = 0; j < (this.secondDay[i].rows).length; j++) {
          temp = this.secondDay[i].rows[j];
          var len = (this.firstDay.length - 1) * 130 + 109 + 175;
          graph.addNode({
            id: temp.taskGroup,
            x: 234 + j * 256,
            y: len + 130 * i,
            width: 148,
            height: 81,
            attrs: {
              body: {
                fill: '#ffffff00',
                stroke: '#ffff0000',
              },
            },
            ports: {
              groups: {
                outR: {
                  position: {
                    name: 'absolute',
                    args: {x: 136, y: 35}
                  },
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#31d0c600',
                      strokeWidth: 2,
                      fill: '#fff'
                    }
                  }
                },
                inT: {
                  position: {
                    name: 'absolute',
                    args: {x: 70, y: 8},
                  },
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: 'red',
                      strokeWidth: 2,
                      fill: '#fff',
                      style: {
                        visibility: 'hidden',
                      }
                    }
                  }
                },
                inL: {
                  position: {
                    name: 'absolute',
                    args: {x: 8, y: 35},
                  },
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: 'red',
                      strokeWidth: 2,
                      fill: '#fff',
                      style: {
                        visibility: 'hidden',
                      }
                    }
                  }
                },
                outB: {
                  position: {
                    name: 'absolute',
                    args: {x: 70, y: 69},
                  },
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#31d0c600',
                      strokeWidth: 2,
                      fill: '#fff'
                    }
                  }
                }
              },
              items: [
                {
                  id: temp.taskGroup + ',R,' + i + ',' + j + ',' + 1,
                  group: 'outR',
                  markup: [
                    {
                      tagName: 'path',
                      selector: 'path',
                    },
                  ],
                  attrs: {
                    path: {
                      d: 'M12,0 a 12 12 0 0 0 -12 -12 l0 24 a 12 12 0 0 0 12 -12 ',
                      magnet: true,
                      fill: '#044B9400',
                      stroke: '#31d0c600',
                    }
                  }
                },
                {
                  id: temp.taskGroup + ',L,' + i + ',' + j + ',' + 1,
                  group: 'inL'
                },
                {
                  id: temp.taskGroup + ',T,' + i + ',' + j + ',' + 1,
                  group: 'inT'
                },
                {
                  id: temp.taskGroup + ',B,' + i + ',' + j + ',' + 1,
                  group: 'outB',
                  markup: [
                    {
                      tagName: 'path',
                      selector: 'path',
                    },
                  ],
                  attrs: {
                    path: {
                      d: 'M0,12 a 12 12 0 0 0 12 -12 l -24 0 a 12 12 0 0 0 12 12',
                      magnet: true,
                      fill: '#044B9400',
                      stroke: '#31d0c600',
                    }
                  }
                },
              ]
            }
          });
        }
      }
      for (var i = 0; i < this.secondDay.length; i++) {
        for (var j = 0; j < this.secondDay[i].rows.length; j++) {
          var temp = this.secondDay[i].rows[j];
          if (temp.lastTaskGroup !== "null" && temp.lastTaskGroup !== undefined && temp.lastTaskGroup !== "") {
            var arr = temp.lastTaskGroup.split(",");
            var y1 = graph.getCellById(temp.taskGroup).port.ports[0].id.split(',')[2];
            var isFD1 = graph.getCellById(temp.taskGroup).port.ports[0].id.split(',')[4];
            for (var z = 0; z < arr.length; z++) {
              var y2 = graph.getCellById(arr[z]).port.ports[0].id.split(',')[2];
              var isFD = graph.getCellById(arr[z]).port.ports[0].id.split(',')[4];
              var i1 = graph.getCellById(arr[z]).port.ports[0].id.split(',')[3];
              graph.addEdge({
                source: {
                  cell: arr[z],
                  port: (arr[z] + ',' + ((y1 === y2 && isFD1 === isFD) ? 'R' : 'B') + ',' + y2 + ',' + i1 + ',' + isFD)
                },
                target: {
                  cell: temp.taskGroup,
                  port: (temp.taskGroup + ',' + ((y1 === y2 && isFD1 === isFD) ? 'L' : 'T') + ',' + i + ',' + j + ',' + 1)
                },
                router: {
                  name: 'metro'
                },
                attrs: {
                  line: {
                    stroke: '#00BCD4',
                  },
                },
              })
            }
          }
        }
      }
      this.devGraph = graph;
    },
    //检查清算组名是否存在
    checkTaskGloupName() {
      if (this.editGroupName === undefined || this.editGroupName.trim() === '') {
        return;
      }
      if (this.editGroupName == this.clearGroupName) {
        return;
      }
      if (this.clearNames.indexOf(this.editGroupName) !== -1) {
        Tools.alert("该清算组名称已经存在,请重新输入", "danger");
        this.editGroupName = '';
      }
    },
    //处理清算组的方法
    execPlusClearGroup(data) {
      let ret = this.$refs.addForm.validate();
      var execTaskType = this.editTasktype;
      var taskGroupName = this.editGroupName;
      let taskModel = this.taskModel;
      if (!ret) {
        return false;
      }
      this.lock = true;
      this.httpUtil.comnQuery({
        action: 'Ta5015.getSequence',
        params: {}
      }).then(res => {
        if (data !== {}) {
          if (!this.isActive) {
            this.isActive = true;
          }
          var temp = {};
          if (this.formData.flag) {
            this.clearGroup = {};
            this.clearGroup = {
              execTaskType: execTaskType,
              lastTaskGroup: "",
              preTaskGroup: "",
              runningType: "1",
              shouldExecTime: '999999',
              taskGroup: res.rows[0],
              taskGroupName: taskGroupName,
              taskModel : taskModel
            }
            this.clearNames.push(taskGroupName);
            this.noTimeList.push(this.clearGroup);
            temp = this.clearGroup;
          } else {
            let arr = [];
            var y = this.formData.y;
            var x = this.formData.x;
            this.clearGroup = {};
            this.clearNames.push(taskGroupName);
            if (this.formData.isFD) {
              var tempTime = this.firstDay[y].time.replace(":", "") + "00";
              this.taskGroup = {
                execTaskType: execTaskType,
                lastTaskGroup: "",
                preTaskGroup: "",
                runningType: "1",
                shouldExecTime: tempTime,
                taskGroup: res.rows[0],
                taskGroupName: taskGroupName,
                taskModel : taskModel
              }
              arr = this.firstDay[y].rows;
              this.firstDay[y].rows = this.arrayPlus(x, this.firstDay[y].rows, this.taskGroup);
              temp = this.firstDay[y].rows[x];
            } else {
              var tempTime = this.secondDay[y].time.replace(":", "") + "00";
              this.taskGroup = {
                execTaskType: execTaskType,
                lastTaskGroup: "",
                preTaskGroup: "",
                runningType: "0",
                shouldExecTime: tempTime,
                taskGroup: res.rows[0],
                taskGroupName: taskGroupName,
                taskModel : taskModel
              }
              arr = this.secondDay[y].rows;
              this.secondDay[y].rows = this.arrayPlus(x, this.secondDay[y].rows, this.taskGroup);
              temp = this.secondDay[y].rows[x];
            }
            this.initMap();
            //重新加载画布
            this.reloadG6();
          }
          // if (temp.execTaskType !== '1' && temp.execTaskType !== '2') {
          // if ('2,3,4,5,6'.indexOf(temp.execTaskType) >= 0) {
          //   this.httpUtil.comnQuery({
          //     action: 'TaClearGroupMember.findTaClearGroupMembers',
          //     params: temp
          //   }).then(result => {
          //     temp.member = result.rows;
          //   });
          // }
          if (temp.execTaskType !== '2') {
            this.httpUtil.comnQuery({
              action: 'KbatchTaskInfo.findKbatchTaskInfoWithFlag',
              params: {taskModel: temp.taskModel,taskType: temp.execTaskType, taskGroup: temp.taskGroup}
            }).then(data => {
              temp.taskInfos = [];
              temp.taskInfos = data.rows;
            });
            this.httpUtil.comnQuery({
              action: 'KbatchTaskSet.findKbatchTaskSet',
              params: {taskGroup: temp.taskGroup}
            }).then(data => {
              temp.existTaskInfos = [];
              temp.existTaskInfos = data.rows;
            });
          }
          this.lock = false;
          // })
        }
      })
      this.$refs.addClearGroup.close();
    },
    //弹出添加清算任务框
    addClearGroup(flag, y, x, isFD) {
      this.formData = {};
      this.formData.flag = flag;
      this.formData.y = y;
      this.formData.x = x;
      this.taskModel = '';
      this.clearGroupName = '';
      this.editGroupName = '';
      this.editTasktype = '';
      // this.formData.taskType=undefined;
      this.formData.isFD = isFD;
      this.$refs.addClearGroup.popup();
    },
    //重新加载G6画布
    reloadG6() {
      if (this.devGraph !== '') {
        this.deleteLock = true;
        this.devGraph.clearCells();
        this.deleteLock = false;
      }
      this.initG6();
    },
    //数据根据索引除去对应对象的工具方法
    arrayMinu(i, arr) {
      if (i === 0) {
        arr.shift();
      } else if (i === arr.length) {
        arr.pop();
      } else {
        arr = arr.slice(0, i).concat(arr.slice(i + 1, arr.length));
      }
      return arr;
    },
    //数据根据索引对应的位置插入对象的工具方法
    arrayPlus(i, arr, obj) {
      if (i === 0) {
        arr.unshift(obj);
      } else if (i === arr.length) {
        arr.push(obj);
      } else {
        arr = arr.slice(0, i).concat(obj, arr.slice(i, arr.length));
      }
      return arr;
    },
    //删除清算组
    minuGroup: function (isFD, y, x, isNoTime, taskGroup) {
      Tools.confirm(() => {
        if (!this.isActive) {
          this.isActive = true;
        }
        this.lock = true;
        let p;
        if (p = this.isExistMemberAndTask(taskGroup)) {
          p.then(res => {
            if (res) {
              Tools.alert("删除失败,清算组[" + taskGroup.taskGroupName + "]存在任务或者成员,请清理后操作.", 'danger');
            } else {
              if (isNoTime) {
                this.noTimeList = this.arrayMinu(this.noTimeList.indexOf(taskGroup), this.noTimeList);
                this.clearNames = this.arrayMinu(this.clearNames.indexOf(taskGroup.taskGroupName), this.clearNames);
              } else {
                if (isFD) {
                  //获取临时clearGroup对象
                  var temp = this.firstDay[y].rows[x];
                  //去除前置批次
                  var arr = [];
                  if (temp.preTaskGroup !== '') {
                    arr = temp.preTaskGroup.split(',');
                  }
                  arr.push(temp.taskGroup);
                  var taskGroupName = '';
                  if (this.map[temp.taskGroup] !== undefined && this.map[temp.taskGroup].length > 0) {
                    var arr2 = this.map[temp.taskGroup];
                    for (var i = 0; i < arr2.length; i++) {
                      var temp2 = this.minusString(arr2[i].lastTaskGroup, temp.taskGroup);
                      arr2[i].lastTaskGroup = temp2;

                      this.deleteDeploy(arr, arr2[i]);

                    }
                  }
                  //给clearGroup赋值
                  this.clearGroup = {
                    execTaskType: temp.execTaskType,
                    label: temp.taskGroupName,
                    taskGroup: temp.taskGroup
                  }
                  taskGroupName = this.firstDay[y].rows[x].taskGroupName;
                  this.firstDay[y].rows = this.arrayMinu(x, this.firstDay[y].rows);
                  this.minuGroupName(taskGroupName);
                } else {
                  //获取临时clearGroup对象
                  var temp = this.secondDay[y].rows[x];
                  //去除前置批次
                  var arr = temp.preTaskGroup.split(',');
                  arr.push(temp.taskGroup);
                  if (this.map[temp.taskGroup] !== undefined && this.map[temp.taskGroup].length > 0) {
                    var arr2 = this.map[temp.taskGroup];
                    for (var i = 0; i < arr2.length; i++) {
                      var temp2 = this.minusString(arr2[i].lastTaskGroup, temp.taskGroup);
                      arr2[i].lastTaskGroup = temp2;
                      if (temp.lastTaskGroup !== '') {
                        this.deleteDeploy(arr, arr2[i]);
                      }
                    }
                  }
                  //给clearGroup赋值
                  this.clearGroup = {
                    execTaskType: temp.execTaskType,
                    label: temp.taskGroupName,
                    taskGroup: temp.taskGroup
                  }
                  taskGroupName = this.secondDay[y].rows[x].taskGroupName;
                  this.secondDay[y].rows = this.arrayMinu(x, this.secondDay[y].rows);
                  this.minuGroupName(taskGroupName);
                }
                //加入到未选择的列表中
                this.initMap();
                this.reloadG6();
              }
            }
          })
        }
        this.lock = false;
      }, "确定删除[" + taskGroup.taskGroupName + "]?")
    },
    //判断清算组是否存在成员和任务
    isExistMemberAndTask(taskGroup) {
      if (taskGroup != undefined) {
        let p = new Promise((resolve => {

          //当产品模型改造需求代码合并时,false改为true
            if (taskGroup.execTaskType == '2') {
              this.httpUtil.comnQuery({
                action: 'TaClearTaskInfo.queryIsExistItemByTaskGroup',
                params: {taskGroup: taskGroup.taskGroup}
              }).then(res => {
                if (res.rows.length > 0) {
                  resolve(true);
                } else {
                  resolve(false);
                }
              })
            } else {
              if (taskGroup.existTaskInfos != undefined && taskGroup.existTaskInfos.length > 0) {
                resolve(true);
              } else {
                if (taskGroup.member != undefined && taskGroup.member.length > 0) {
                  for (var i = 0; i < taskGroup.member.length; i++) {
                    if (taskGroup.member[i].isGroupMember == '1') {
                      resolve(true);
                      return;
                    }
                  }
                }
                resolve(false);
              }
            }
          }
        ))
        return p;
      }
    },
    minuGroupName(oldName, newName) {
      this.clearNames = this.arrayMinu(this.clearNames.indexOf(oldName), this.clearNames);
      this.clearNames.push(newName);
    },
    //增加执行时间
    plusTime(isFD, y) {
      if (!this.isActive) {
        this.isActive = true;
      }
      this.lock = true;
      this.startTime = '00:00:00';
      this.endTime = '23:59:00';
      if (isFD) {
        if (y !== this.firstDay.length) {
          this.endTime = moment(this.firstDay[y].time + ":00", "HH:mm:ss").subtract(1, 'minute').format("HH:mm:ss");
        } else {
          if (this.firstDay.length > 0 && (this.firstDay[y - 1].time === '23:59' || this.firstDay[y - 1].time === '23:59:00')) {
            Tools.alert("此处无时间添加", "danger");
            return;
          }
        }
        if (y !== 0) {
          this.startTime = moment(this.firstDay[y - 1].time + ":00", "HH:mm:ss").add(1, 'minute').format("HH:mm:ss");
        } else {
          if (this.firstDay.length > 0 && (this.firstDay[y].time === '00:00' || this.firstDay[y].time === '00:00:00')) {
            Tools.alert("此处无时间添加", "danger");
            return;
          }
        }
        //算出startTime与endTime的差值
        var DValue = parseInt(moment(this.startTime, "HH:mm:ss").format("HHmmss")) - parseInt(moment(this.endTime, "HH:mm:ss").format("HHmmss"));
        //如果startTime比endTime多一分钟,禁止添加时间  如 startTime:14:00:00 endTime:13:59:00 判断DValue的正负即可
        if (DValue > 0) {
          Tools.alert("此处无时间添加", "danger");
          return;
        }
        var timeObj = {
          time: this.startTime,
          rows: []
        };
        this.firstDay = this.arrayPlus(y, this.firstDay, timeObj);
        //由于vue渲染是异步的,会误报 focus is not a function  所以得在updated方法中触发fucus
        this.focusEvent.execute = true;
        this.focusEvent.name = 'itemF' + y;
      } else {
        if (y !== this.secondDay.length) {
          this.endTime = moment(this.secondDay[y].time + ":00", "HH:mm:ss").subtract(1, 'minute').format("HH:mm:ss");
        } else {
          if (this.secondDay.length > 0 && (this.secondDay[y - 1].time === '23:59' || this.secondDay[y - 1].time === '23:59:00')) {
            Tools.alert("此处无时间添加", "danger");
            return;
          }
        }
        if (y !== 0) {
          this.startTime = moment(this.secondDay[y - 1].time + ":00", "HH:mm:ss").add(1, 'minute').format("HH:mm:ss");
        } else {
          if (this.secondDay.length > 0 && (this.secondDay[y].time === '00:00' || this.secondDay[y].time === '00:00:00')) {
            Tools.alert("此处无时间添加", "danger");
            return;
          }
        }
        //算出startTime与endTime的差值
        var DValue = parseInt(moment(this.startTime, "HH:mm:ss").format("HHmmss")) - parseInt(moment(this.endTime, "HH:mm:ss").format("HHmmss"));
        //如果startTime比endTime多一分钟,禁止添加时间  如 startTime:14:00:00 endTime:13:59:00 判断DValue的正负即可
        if (DValue > 0) {
          Tools.alert("此处无时间添加", "danger");
          return;
        }
        var timeObj = {
          time: this.startTime,
          rows: []
        };
        this.secondDay = this.arrayPlus(y, this.secondDay, timeObj);
        this.focusEvent.execute = true;
        this.focusEvent.name = 'itemS' + y;
      }
      this.reloadG6();
      this.lock = false;
    },
    //检验时间范围
    timeRange(isFD, y) {
      this.startTime = "00:00:00";
      this.endTime = "23:59:00";
      if (isFD) {
        if ((y + 1) !== this.firstDay.length) {
          this.endTime = moment(this.firstDay[y + 1].time + ":00", "HH:mm:ss").subtract(1, 'minute').format("HH:mm:ss");
        }
        if (y !== 0) {
          this.startTime = moment(this.firstDay[y - 1].time + ":00", "HH:mm:ss").add(1, 'minute').format("HH:mm:ss");
        }
      } else {
        if ((y + 1) !== this.secondDay.length) {
          this.endTime = moment(this.secondDay[y + 1].time + ":00", "HH:mm:ss").subtract(1, 'minute').format("HH:mm:ss");
        }
        if (y !== 0) {
          this.startTime = moment(this.secondDay[y - 1].time + ":00", "HH:mm:ss").add(1, 'minute').format("HH:mm:ss");
        }
      }
    },
    //删除时间事件
    deleteTime(isFD, y) {
      Tools.confirm(() => {
        if (!this.isActive) {
          this.isActive = true;
        }
        this.lock = true;
        let checkArr;
        let taskGroups = "(";
        if (isFD) {
          checkArr = this.firstDay[y].rows;
        } else {
          checkArr = this.secondDay[y].rows;
        }

        //是否允许删除的计数器,当不满足时,该计数器加一.最后判断计数器是否大于零,如果大于零则不允许删除
        let count = 0;
        checkArr.forEach(res => {
          //如果是产品类清算组则拼装参数,如果不是产品类则检查是否存在成员和任务
          //当产品模型改造需求代码合并时,false改为true
          if ( res.execTaskType == '2') {
            if (taskGroups != "(") {
              taskGroups = taskGroups + ",";
            }
            taskGroups =  taskGroups + "'" + res.taskGroup + "'";
          } else {
            if (res.existTaskInfos != undefined && res.existTaskInfos.length > 0) {
              count++;
            } else {
              if (res.member != undefined && res.member.length > 0) {
                for (var i = 0; i < res.member.length; i++) {
                  if (res.member[i].isGroupMember == '1') {
                    count++;
                  }
                }
              }
            }
          }
        })
        taskGroups = taskGroups + ")";
        if (count > 0) {
          Tools.alert("删除失败,该行内存在清算组任务或者成员,请清理后操作.", 'danger');
        } else if ( taskGroups !== "()") {
          this.httpUtil.comnQuery({
            action: 'TaClearTaskInfo.queryIsExistItemByTaskGroups',
            params: {taskGroups: taskGroups}
          }).then(res => {
            if (res.rows.length > 0) {
              Tools.alert("删除失败,该行内存在清算组任务或者成员,请清理后操作.", 'danger');
            } else {
              this.deleteRow(isFD, y);
            }
          })
        } else {
          this.deleteRow(isFD, y);
        }

        this.lock = false;
      }, "确定删除该整行数据?")
    },
    //删除行的函数
    deleteRow(isFD, y) {
      if (isFD) {
        var arr = this.firstDay[y].rows;
        for (var j = 0; j < arr.length; j++) {
          var temp = arr[j];
          var str = arr.map(res => {
            return res.taskGroup;
          }).join(',');
          var arr1 = [];
          if (temp.preTaskGroup !== '') {
            var arr1 = temp.preTaskGroup.split(',');
          }
          arr1.push(temp.taskGroup);
          if (this.map[temp.taskGroup] !== undefined && this.map[temp.taskGroup].length > 0) {
            var arr2 = this.map[temp.taskGroup];
            for (var i = 0; i < arr2.length; i++) {
              var temp2 = this.minusString(arr2[i].lastTaskGroup, temp.taskGroup);
              arr2[i].lastTaskGroup = temp2;
              this.deleteDeploy(arr1, arr2[i], str);
            }
          }
        }
        for (var i = 0; i < arr.length; i++) {
          this.clearGroup = {
            execTaskType: arr[i].execTaskType,
            label: arr[i].taskGroupName,
            taskGroup: arr[i].taskGroup
          }
        }
        this.firstDay = this.arrayMinu(y, this.firstDay);
      } else {
        var arr = this.secondDay[y].rows;
        for (var j = 0; j < this.secondDay[y].rows.length; j++) {
          var temp = this.secondDay[y].rows[j];
          var str = arr.map(res => {
            return res.taskGroup;
          }).join(',');
          var arr1 = [];
          if (temp.preTaskGroup !== '') {
            arr1 = temp.preTaskGroup.split(',');
          }
          arr1.push(temp.taskGroup);
          if (this.map[temp.taskGroup] !== undefined && this.map[temp.taskGroup].length > 0) {
            var arr2 = this.map[temp.taskGroup];
            for (var i = 0; i < arr2.length; i++) {
              var temp2 = this.minusString(arr2[i].lastTaskGroup, temp.taskGroup);
              arr2[i].lastTaskGroup = temp2;
              this.deleteDeploy(arr1, arr2[i], str);
            }
          }
        }
        for (var i = 0; i < arr.length; i++) {
          this.clearGroup = {
            execTaskType: arr[i].execTaskType,
            label: arr[i].taskGroupName,
            taskGroup: arr[i].taskGroup
          }
        }
        this.secondDay = this.arrayMinu(y, this.secondDay);
      }
      this.initMap();
      this.reloadG6();
    },
    //一个字符串减去另一字符串的方法 比如 str1:'qw,er,t'  str2:'er'  返回: 'qw,t'
    minusString(str1, str2) {
      var i = str1.indexOf(str2);
      if (str2 === str1) {
        return '';
      }
      if (i === 0) {
        return str1.substring(str2.length + 1, str1.length);
      } else if (i > 0) {
        return str1.substring(0, i - 1) + str1.substring(i + str2.length, str1.length);
      } else {
        return str1;
      }
    },
    //递归遍历添加前置批次
    addPreDeploy(sourceId, targetClearGroup) {
      var arr1 = sourceId.split(',');
      for (var j = 0; j < arr1.length; j++) {
        if (targetClearGroup.preTaskGroup.indexOf(arr1[j]) !== -1) {
          this.transaction.callback = true;
        }
      }
      if (targetClearGroup.preTaskGroup === '' || targetClearGroup.preTaskGroup === 'null') {
        targetClearGroup.preTaskGroup = sourceId;
      } else {
        targetClearGroup.preTaskGroup += ',' + sourceId;
      }
      //递归在pre字段加上sourceId
      if (this.map[targetClearGroup.taskGroup] !== undefined) {
        var arr = this.map[targetClearGroup.taskGroup];
        for (var i = 0; i < arr.length; i++) {
          this.addPreDeploy(sourceId, arr[i]);
        }
      }
    },
    //删除依赖
    deleteDeploy(arr1, targetClearGroup, str) {
      if (!this.isActive) {
        this.isActive = true;
      }
      if (str === undefined) {
        for (var j = 0; j < arr1.length; j++) {
          if (targetClearGroup.preTaskGroup === '' || targetClearGroup.preTaskGroup === 'null') {
            return;
          } else {
            var b = targetClearGroup.taskGroup == "000000000191";
            targetClearGroup.preTaskGroup = this.minusString(targetClearGroup.preTaskGroup, arr1[j]);
          }
        }
      } else {
        for (var j = 0; j < arr1.length; j++) {
          if (str.indexOf(targetClearGroup.taskGroup) === -1) {
            if (targetClearGroup.preTaskGroup === '' || targetClearGroup.preTaskGroup === 'null') {
              return;
            } else {
              targetClearGroup.preTaskGroup = this.minusString(targetClearGroup.preTaskGroup, arr1[j]);
            }
          }
        }
      }
      //递归在pre字段加上sourceId
      if (this.map[targetClearGroup.taskGroup] !== undefined) {
        var arr = this.map[targetClearGroup.taskGroup];
        for (var i = 0; i < arr.length; i++) {
          this.deleteDeploy(arr1, arr[i]);
        }
      }
    },
    guid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
          v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    }
    ,
//初始化map对象
    initMap() {
      this.map = {};
      for (var i = 0; i < this.firstDay.length; i++) {
        for (var j = 0; j < this.firstDay[i].rows.length; j++) {
          var clearGroup = this.firstDay[i].rows[j];
          this.addStrToArray(clearGroup);
        }
      }
      for (var i = 0; i < this.secondDay.length; i++) {
        for (var j = 0; j < this.secondDay[i].rows.length; j++) {
          var clearGroup = this.secondDay[i].rows[j];
          this.addStrToArray(clearGroup);
        }
      }
      this.noTimeList.forEach(res => {
        this.initTaskAndMember(res);
      })
      this.init = false;
    }
    ,
//对map对象赋值
    addStrToArray(clearGroup) {
      this.initTaskAndMember(clearGroup);
      if (clearGroup.lastTaskGroup !== 'null') {
        var arr = clearGroup.lastTaskGroup.split(',');
        for (var g = 0; g < arr.length; g++) {
          if (arr[g] !== 'null' && arr[g] !== '') {
            var count = 0;
            if (this.map[arr[g]] !== undefined) {
              for (var i = 0; i < this.map[arr[g]].length; i++) {
                if (this.map[arr[g]][i].taskGroup === clearGroup.taskGroup) {
                  count++;
                }
              }
            } else {
              this.map[arr[g]] = [];
            }
            if (count === 0) {
              this.map[arr[g]].push(clearGroup);
            }
          }
        }
      }
    },
    //初始化任务和成员
    initTaskAndMember(clearGroup) {
      if (this.init) {
        // if (clearGroup.execTaskType !== '1' && clearGroup.execTaskType !== '2') {
        if ('2,3,4,5,6'.indexOf(clearGroup.execTaskType) >= 0) {
          this.httpUtil.comnQuery({
            action: 'TaClearGroupMember.findTaClearGroupMembers',
            params: clearGroup
          }).then(result => {
            clearGroup.member = [];
            clearGroup.member = result.rows;
          });
        }
        if (clearGroup.execTaskType !== '2') {
          this.httpUtil.comnQuery({
            action: 'KbatchTaskInfo.findKbatchTaskInfoWithFlag',
            params: {taskType: clearGroup.execTaskType,taskModel: clearGroup.taskModel, taskGroup: clearGroup.taskGroup}
          }).then(data => {
            clearGroup.taskInfos = [];
            clearGroup.taskInfos = data.rows;
          });
          this.httpUtil.comnQuery({
            action: 'KbatchTaskSet.findKbatchTaskSet',
            params: {taskGroup: clearGroup.taskGroup}
          }).then(data => {
            clearGroup.existTaskInfos = [];
            clearGroup.existTaskInfos = data.rows;
          });
        }
      }
    },
    initTaskAndMemberChange(clearGroup) {
      if (this.init) {
        // if (clearGroup.execTaskType !== '1' && clearGroup.execTaskType !== '2') {
        if ('2,3,4,5,6'.indexOf(clearGroup.execTaskType) >= 0) {
          this.httpUtil.comnQuery({
            action: 'TaClearGroupMember.findTaClearGroupMembers',
            params: clearGroup
          }).then(result => {
            clearGroup.member = [];
            clearGroup.member = result.rows;
          });
        }
        if (clearGroup.execTaskType !== '2') {
          this.httpUtil.comnQuery({
            action: 'KbatchTaskInfo.findKbatchTaskInfoWithFlag',
            params: {taskType: clearGroup.execTaskType,taskModel: clearGroup.taskModel, taskGroup: clearGroup.taskGroup}
          }).then(data => {
            clearGroup.taskInfos = [];
            clearGroup.taskInfos = data.rows;
          });
          clearGroup.existTaskInfos = [];
        }
      }
    },
    //提交
    submit() {
      if (!this.isActive) {
        Tools.alert("没有修改,无需提交", "danger");
        return;
      }
      var clearData = [];
      this.firstDay.forEach(res => {
        clearData = clearData.concat(res.rows);
      });
      this.secondDay.forEach(res => {
        clearData = clearData.concat(res.rows);
      });
      this.noTimeList.forEach(res => {
        clearData = clearData.concat(res);
      })
      var clearDataStr = JSON.stringify(clearData);
      const zip = new JSZip()
      zip.file('1.txt', clearDataStr)
      zip.generateAsync({type: 'blob'}).then((content)=>{
        const formData = new FormData()
        formData.append("files", content)
        this.httpUtil.upload({url: '/upload-files/server/DpsApp/Ta5015Combin/commit.json', formData})
        .then(res=>{
          Tools.alert(res.data.returnmsg || "操作失败", res.data.success ? "success" : "danger");
          this.reload = false;
          this.isActive = false;
          this.reload = true;
        })
      });
    },
    //非时序添加组
    addNoTimeClearGroup(flag) {
      this.formData = {};
      this.formData.flag = flag;
      this.$refs.addClearGroup.popup();
    },
    //提供子组件修改红点点的方法
    editActive1() {
      if (!this.isActive) {
        this.isActive = true;
      }
    }
  },
  created() {

  }
  ,
  computed: {
    iconStyle() {
      let iconStyle = {};
      iconStyle.background = this.$store.state.system.cardBackground
      return iconStyle;
    }
  }
  ,
  mounted() {
    this.httpUtil.ajax({
      url: '/commQuery/Ta5015/queryTa5015.json',
      params: {execTaskType:'1'}
    }).then(res => {
      if (res) {
        this.firstDay = res.firstDay;
        this.secondDay = res.secondDay;
        this.noTimeList = res.noTimeList;
        this.initG6();
        this.init = true;
        this.initMap();
      }
    });
    this.httpUtil.comnQuery({
      action: 'Ta5015.getAllTaskGloupName',
      params: {execTaskType:'1'}
    }).then(res => {
      this.clearNames = res.rows;
    });
  }
  ,
  updated() {
    //由于vue渲染是异步,updated方法是修改参数渲染后触发的
    if (this.focusEvent.execute && this.focusEvent.name != '') {
      this.$refs[this.focusEvent.name][0].focus();
      //用完事件 将focusEvent置为空
      this.focusEvent.execute = false;
      this.focusEvent.name = '';
    }
  },
  beforeRouteLeave: function(to,from,next) {
    if (this.isActive) {
      if (to.path === '/login') {
        Tools.alert("用户已安全退出，5s后跳转至登录界面，未保存部分将丢失","warning")
        setTimeout(() => {
            next();
        }, 5000);
      } else {
        this.$confirm('数据已修改但未保存，确认离开吗？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          next()
        }).catch(()=>{next(false)})
      }
    } else {
      next()
    }
  }
}
</script>

<style scoped>

#mainBody {
  position: unset;
}

.head-div {
  width: 100%;
  color: #999999;
}

.share-container {
  position: fixed;
  display: flex;
  top: 110px;
  right: 32px;
  z-index: 300;
  width: 100px;
  height: 52px;
  justify-content: center;
  align-items: center;
  background: #FFFFFF;
  box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.14);
  border-radius: 26px 0px 0px 26px;
}

.pd-text {
  font-size: 10px;
  padding-left: 12px;
}


.pd-button {
  display: block;
  padding-top: 8px;
  width: 50px;
  max-width: 50px;
  min-width: 50px;
  position: relative;
}

draggable {
  z-index: 2;
}

.pd-button:hover {
  background-color: #f3e9e9;
  border-radius: 3px;
  cursor: pointer;
}

.dot {
  display: block;
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: red;
  position: absolute;
  right: 3px;
  top: 3px;
}

.notTimeBox {
  width: 12.5%;
  position: fixed;
  border-radius: 6px;
  background: #F5F5F5;
  top: 142px;
  z-index: 2;
  right: 2%;
}

.clear {
  margin-top: 25px;
  display: inline-block;
}

.timeBox {
  width: 85.1%;
  position: relative;
  top: 25px;
}

.time {
  display: inline-block;
  width: 130px;
  height: 50px;
  font-size: 30px;
  color: #323232;
  line-height: 50px;
  position: relative;
  transform: translateY(-50%);
  top: 50%;
  padding-left: 10.5px;
  border-left: 2px solid #00BCD4;
  z-index: 3;
}

.row1 {
  width: 100%;
  height: 130px;
  padding-left: 20px;
  top: -20px;
  position: relative;
}

#mountNode {
  position: absolute !important;
  top: 0;
  z-index: 1;
}


.ClearGroupEditList {
  display: inline-block;
  position: relative;
  padding-left: 155px;
  width: 100%;
  height: 60px;
  top: -21px;
}

.item1 {
  display: inline-block;
  position: relative;
  margin: 0 60px;
  /*z-index: 2;*/
}

.mainBody {
  width: 100%;
  padding-bottom: 45px;
  position: relative;
}


.line {
  position: relative;
  margin-top: -20px;
  height: 75px;
}

.lineOne {
  display: inline-block;
  width: 50.5px;
  height: 18px;
  background-color: #6569FD;
  line-height: 18px;
  color: #ffffff;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  padding-left: 5px;
  border-radius: 2px 0px 0px 2px;
}

.lineTwo {
  width: 10px;
  height: 18px;
  display: inline-block;
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  left: 50.5px;
  border-style: solid;
  border-width: 8px 10px 10px 10px;
  border-color: transparent transparent transparent #6569FD;
}

.lineThree {
  display: inline-block;
  width: 97%;
  position: absolute;
  top: 38%;
  left: 59.5px;
  border: 1px solid #6569FD;
}

.lineFour {
  display: inline-block;
  border-style: solid;
  border-width: 8px 10px 10px 10px;
  border-color: transparent #6569FD transparent transparent;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: -10px;
}

.linefive {
  display: inline-block;
  width: 8px;
  height: 18px;
  background: #6569FD;
  border-radius: 2px 0px 0px 2px;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: -18px;
}

.middleLine {
  background-color: #DBDBDB;
  height: 1px;
  width: 90%;
  position: absolute;
  top: 58%;
  transform: translateY(-50%);
}

.plusGloup {
  z-index: 3;
  width: 20px;
  height: 20px;
  border: 2.5px solid #00BCD4;
  background: #FFFFFF;
  border-radius: 10px;
  position: absolute;
}

.plusGloup::before {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid #00BCD4;
}

.plusGloup::after {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid #00BCD4;
}

.plusGloup:hover {
  z-index: 3;
  width: 20px;
  height: 20px;
  border-radius: 10px;
  border: 0;
  background-color: #00BCD4;
  cursor: pointer;
}

.plusGloup:hover::after {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid white;
}

.plusGloup:hover::before {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid white;
}

.plusTime {
  z-index: 3;
  width: 20px;
  height: 20px;
  border: 2.5px solid #00BCD4;
  background: #FFFFFF;
  position: absolute;
}

.plusTime::before {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid #00BCD4;
}

.plusTime::after {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid #00BCD4;
}

.plusTime:hover {
  z-index: 3;
  width: 20px;
  height: 20px;
  border: 0;
  cursor: pointer;
  background-color: #00BCD4;
}

.plusTime:hover::after {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid white;
}

.plusTime:hover::before {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid white;
}

.firstPlus {
  position: relative;
  left: 60px;
  top: -8px;
  z-index: 3
}

.plusTimeItem {
  transform: translateX(27px);
  top: 77px;
}

.notTimeList {
  position: relative;
  top: 0;
}

.item3 {
  width: 157px;
  margin: 0 auto;
  margin-top: 30px;
  margin-bottom: 10px;
  display: block;
}

.bigTitle {
  padding-left: 20px;
  width: 74px;
  height: 18px;
  font-size: 18px;
  margin-bottom: 10px;
  color: #3B3B3B;
  line-height: 26px;
  margin-top: 8px;
}

.garbage {
  width: 16px;
  height: 20px;
  transform: translateY(-2px);
  position: absolute;
  margin-left: 8px;
  margin-top: 13.5px;
  cursor: pointer;
}

.garbage:hover {
  content: url("/static/images/clear/mouseover.png");
}

.noTimePlus {
  z-index: 3;
  width: 20px;
  height: 20px;
  border: 2.5px solid #00BCD4;
  background: #FFFFFF;
  border-radius: 10px;
  margin: 15px auto;
  position: relative;
}

.noTimePlus::before {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid #00BCD4;
}

.noTimePlus::after {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid #00BCD4;
}

.noTimePlus:hover {
  z-index: 3;
  width: 20px;
  height: 20px;
  border-radius: 10px;
  border: 0;
  background-color: #00BCD4;
  cursor: pointer;
}

.noTimePlus:hover::after {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid white;
}

.noTimePlus:hover::before {
  z-index: 3;
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid white;
}

.text {
  /*文字不能被选中*/
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  cursor: auto;
}

.iconText {
  margin-left: 110px;
  display: inline-block;
  margin-top: -10px;
}

.circleIcon {
  display: inline-block;
  margin: 25px 5px;
}

.circle {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background-color: #5DC97A;
  display: inline-block;
  transform: translateY(2px);
}

.moverArrow:hover ~ #mountNode {
  z-index: -1;
}

</style>

<style>
.el-popover {
  transform: translate(-6px, -10px);
}

svg rect:hover {
  cursor: default;
}

.time .el-input {
  font-size: 30px;
  width: 76px;
  border: 0;
}

.time .el-input .el-input__icon {
  display: none;
}

.time .el-input .el-input__inner {
  border: 0;
  padding: 0;
  cursor: pointer;
}

.el-container {
  min-height: 100%;
}

</style>
