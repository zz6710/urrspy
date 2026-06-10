import { findComponent } from '../components/componentsConfig'
import { getStyle } from '@/pages/design/utils/style'
import { log } from 'mathjs/lib/entry/pureFunctionsAny.generated';

/**
 * 组件模板代码
 *
 */
export function K_FIELD_TEXT_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    // 在绝对定位中，表单外的input框，默认宽度为180
    if(config.style.position == 'absolute') {
      stringStyle += 'width: 200px;'
    }
    let tempCode = `<k-field-text v-model="${value}" style="${stringStyle}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-text>`;

    return tempCode;
}

export function K_FIELD_UPLOAD_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-upload v-model="${value}" style="${stringStyle}" ${options} ${joinEventOption(data.events)} ${joinCustomAttrOption(config)}></k-field-upload>`;
    return tempCode;
}

export function K_FIELD_RICH_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config,data);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-rich v-model="${value}" style="${stringStyle}" ${options} ${joinEventOption(data.events)} ${joinCustomAttrOption(config)}></k-field-rich>`;
    return tempCode;
}

export function K_STEPS_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    let stepsOptions = joinOptions(config);
    let stepContent="";
    if(config.list && config.list.length > 0){
        config.list.forEach(step => {
            let stepOptions = joinOptions(step);
            let formItemContent = "";
            if(step.list && step.list.length > 0){
                step.list.forEach(item=>{
                    if(item.type == 'el-row'){
                        formItemContent += EL_ROW_CODE(item, data, true);
                    } else {
                        formItemContent += K_FORM_ITEM_CODE(item, data);
                    }
                })
            }
            stepContent=stepContent+`<k-step ${stepOptions} >${formItemContent}</k-step>`
        });
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-steps style="${stringStyle}" ${stepsOptions} ${joinCustomAttrOption(config)} ${joinEventOption(events)}>
        ${stepContent}
    </k-steps>`
    return tempCode;
}

export function EL_TABS_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    let tabsOptions = joinOptions(config);
    let tabsContent="";
    if(config.list && config.list.length > 0){
        config.list.forEach(pane => {
            // 图标
            let slot="";
            if(pane.options.icon.type && pane.options.icon.value!==""){
                slot = `<span slot="label">${ICON_INIT(pane.options.icon)} ${pane.options.label}</span>`;
            }
            let paneOptions = joinOptions(pane);
            let paneContent = "";
            if(pane.list && pane.list.length > 0){
                pane.list.forEach(item=>{
                    let type = item.type.toUpperCase().replaceAll("-", "_")
                    paneContent += this[`${type}_CODE`](item, data)
                })
            }
            tabsContent+=`<el-tab-pane ${paneOptions} >${slot}${paneContent}</el-tab-pane>`
        });
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<el-tabs style="${stringStyle}" ${tabsOptions} ${joinCustomAttrOption(config)} ${joinEventOption(events)}>
        ${tabsContent}
    </el-tabs>`
    return tempCode;
}

export function EL_COLLAPSE_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    let tabsOptions = joinOptions(config);
    let tabsContent="";
    if(config.list && config.list.length > 0){
        config.list.forEach(item => {
            // 图标
            let slot="";
            if(item.options.icon.value && item.options.icon.value!==""){
                let slotInner="";
                if(item.options.icon.type == 'icon'){
                    slotInner = `<i class="${item.options.icon.value}"/>`
                } else if(item.options.icon.type == 'md'){
                    slotInner = `<md-icon style="margin:0 !important" >${item.options.icon.value}</md-icon>`;
                } else if(item.options.icon.type == 'svg'){
                    slotInner = `<md-icon md-src="${item.options.icon.value}" />`;
                } else {
                    console.log(" 暂不支持的图标类型 ", item.options.icon)
                }
                let itemLabel= item.options.title;
                slot = `<template slot="title">${slotInner} ${itemLabel}</template>`;
            }

            //slot = `<template slot="title">${ICON_INIT(item.options.icon)} ${item.options.title}</template>`;

            let itemOptions = joinOptions(item);
            let itemContent = "";
            if(item.list && item.list.length > 0){
                item.list.forEach(t=>{
                    let type = t.type.toUpperCase().replaceAll("-", "_")
                    itemContent += this[`${type}_CODE`](t, data)
                })
            }
            tabsContent+=`<el-collapse-item ${itemOptions} >${slot}${itemContent}</el-collapse-item>`
        });
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<el-collapse style="${stringStyle}" ${tabsOptions} ${joinCustomAttrOption(config)} ${joinEventOption(events)}>
        ${tabsContent}
    </el-collapse>`
    return tempCode;
}
/**
 * 组件模板代码
 *
 */
 export function EL_DIVIDER_CODE(config, data){
    // 组件Data初始化
    dataInit(config.model, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);
    let slot = ``;
    if(config.options.direction==='horizontal'){
        slot = `${ICON_INIT(config.options.icon)} ${config.options.value}`
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    return `<el-divider style="${stringStyle}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}>${slot}</el-divider>`;
}
export function K_BTN_CODE(config, data){

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // class
    let _class = "";
    if(config.options.class && config.options.class.length){
        _class = `class="${config.options.class.join(" ")}"`
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    return `<k-btn style="${stringStyle}" ${_class}  ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config, data)}>
        ${ICON_INIT(config.options.icon)}
        ${config.options.btnName}
    </k-btn>`;
}

export function ICON_INIT(icon){
    if(icon){
        if(icon.type == 'icon'){
            return `<i class="${icon.value}" />`
        } else if(icon.type == 'md'){
            return `<md-icon >${icon.value}</md-icon>`;
        } else if(icon.type == 'svg'){
            return `<md-icon md-src="${icon.value}" />`;
        } else {
            console.log(" 暂不支持的图标类型 ", icon)
            return "";
        }
    }
}

export function K_FIELD_SELECT_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-select style="${stringStyle}" v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-select>`;

    return tempCode;
}

export function K_FIELD_RADIO_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config, data);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-radio style="${stringStyle}" v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-radio>`;

    return tempCode;
}

export function K_FIELD_CHECKBOX_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config, data);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-checkbox style="${stringStyle}" v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-checkbox>`;

    return tempCode;
}

export function K_FIELD_DATE_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);
    // 组件样式添加
    let stringStyle = changeStyle(config.style);
    let tempCode = `<k-field-date style="${stringStyle}" v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-date>`;

    return tempCode;
}

export function K_FIELD_TIME_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);
    let tempCode = `<k-field-time v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-time>`;

    return tempCode;
}

export function K_FIELD_CASCADER_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config, data);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-cascader style="${stringStyle}" v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-cascader>`;

    return tempCode;
}

export function K_FIELD_BSWITCH_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-bswitch style="${stringStyle}" v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-bswitch>`;

    return tempCode;
}

export function K_FIELD_TREE_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-field-tree style="${stringStyle}" v-model="${value}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-field-tree>`;

    return tempCode;
}

export function K_POPUP_CODE(config, data){
    let ref = config.ref;
    let options = joinOptionsNotBlank(config.options);
    let content = "";
    if(config.list && config.list.length > 0){
        config.list.forEach(v => {
            content += eval(joinFunctionName(v.type, "v, data"));
        });
    }
    let tempCode = `<k-popup ref="${ref}" ${options} ${joinCustomAttrOption(config)} :dataAppendToBody="true">
            ${content}
    </k-popup>`
    return tempCode;
}

export function ABSOLUTE_LAYOUT_CODE (config, data) {
  let ref = config.ref;
  let options = joinOptionsNotBlank(config.options);
  let content = "";
  if(config.list && config.list.length > 0){
      config.list.forEach(v => {
          content += eval(joinFunctionName(v.type, "v, data"));
      });
  }

  // 样式添加
  let stringStyle = changeStyle(config.style);
  if(!stringStyle.includes('position')) {
    stringStyle += 'position:relative;'
  }
  let tempCode = `<div style="${stringStyle}" ref="${ref}" ${options} ${joinCustomAttrOption(config)}>
    ${content}
  </div>`
  return tempCode;
}

export function BASE_LAYOUT_CODE (config, data) {
  let ref = config.ref;
  let options = joinOptionsNotBlank(config.options);
  let content = "";
  if(config.list && config.list.length > 0){
      config.list.forEach(v => {
          content += eval(joinFunctionName(v.type, "v, data"));
      });
  }

  // 样式添加
  let stringStyle = changeStyle(config.style);
  if(!stringStyle.includes('position')) {
    stringStyle += 'position:relative;'
  }
  let tempCode = `<div style="${stringStyle}" ref="${ref}" ${options} ${joinCustomAttrOption(config)}>
    ${content}
  </div>`
  return tempCode;
}

export function K_FORM_CODE(config, data){
    let ref = config.ref;
    let options = joinOptions(config);

    let formItemContent = "";
    if(config.list && config.list.length > 0){
        config.list.forEach(v => {
            if(v.type == 'el-row'){
                formItemContent += EL_ROW_CODE(v, data, true);
            } else {
                formItemContent += K_FORM_ITEM_CODE(v, data);
            }
        });
    }

    let btnContent = K_FORM_FOOTER_CODE(config.btns, data);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-form ref="${ref}" style="${stringStyle}" ${options} ${joinCustomAttrOption(config)}>
        ${formItemContent}
        ${btnContent}
    </k-form>`
    return tempCode;
}

export function K_FORM_ITEM_CODE(config, data){
    let options = joinOptionsNotBlank(config.formItem)

    let content = eval(joinFunctionName(config.type, "config, data"));
    let tempCode = `<k-form-item ${options} ${joinCustomAttrOption(config.formItem)}>
                        ${content}
                    </k-form-item>`;
    return tempCode;
}


export function K_FORM_FOOTER_CODE(btns, data){
    let content = "";
    if(btns && btns.length > 0){
        btns.forEach(v => {
            content += eval(joinFunctionName(v.type, "v, data"));
        });
    }
    let tempCode = `<k-form-footer data-align="center">
                        ${content}
                    </k-form-footer>`;
    return tempCode;
}

export function K_FORM_SEARCH_CODE(config, data){
    let options = joinOptions(config);

    let content = "";
    if(config.btns && config.btns.length > 0){
        config.btns.forEach(v => {
            content += eval(joinFunctionName(v.type, "v, data"));
        });
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-form-search style="${stringStyle}" ${options} ${joinCustomAttrOption(config)}>
        ${content}
    </k-form-search>`
    return tempCode;
}


export function K_FORM_SEARCH_CUSTOMIZE_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    let formItemContent = "";
    if(config.list && config.list.length > 0){
        config.list.forEach(v => {
            if(v.type == 'el-row'){
                formItemContent += EL_ROW_CODE(v, data, true);
            } else {
                formItemContent += K_FORM_ITEM_CODE(v, data);
            }
        });
    }

    // 按钮
    let btnContent = "";
    if(config.btns && config.btns.length > 0){
        let btns = "";
        config.btns.forEach(v => {
            btns += eval(joinFunctionName(v.type, "v, data"));
        });
        btnContent = `<div slot="button">${btns}</div>`
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-form-search-customize style="${stringStyle}" data-target="${config.options.dataTarget}" ${joinCustomAttrOption(config)}>
        ${formItemContent}
        ${btnContent}
    </k-form-search-customize>`;
    return tempCode;
}



export function EL_ROW_CODE(config, data, formFlag=false){
    let colContent = "";
    if(config.columns && config.columns.length > 0){
        config.columns.forEach(v => {
            colContent += `<el-col :span="${v.span}">`;
            if(v.list && v.list.length > 0){
                v.list.forEach(l => {
                    if(formFlag){
                        colContent += K_FORM_ITEM_CODE(l, data);
                    }else{
                        colContent += eval(joinFunctionName(l.type, "l, data"));
                    }
                });
            }
            colContent += `</el-col>`;
        })
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<el-row style="${stringStyle}">
    ${colContent}
  </el-row>`;
  return tempCode;
}


export function K_GRID_CODE(config, data){
    let ref = config.ref;

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    let options = joinOptions(config);

    let gridColumnContent = "";
    if(config.list && config.list.length > 0){
        config.list.forEach(v => {
            gridColumnContent += K_GRID_COLUMN_CODE(v, data);
        });
    }

    let btnContent = "";
    if(config.btns && config.btns.length > 0){
        config.btns.forEach(v => {
            btnContent += K_BTN_CODE(v, data);
        });
    }

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-grid ref="${ref}" style="${stringStyle}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}>
        ${gridColumnContent}
        <template slot="operate" slot-scope="scope">
            ${btnContent}
        </template>
    </k-grid>`
    return tempCode;
}

export function K_GRID_COLUMN_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);
    let tempCode = `<k-grid-column ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-grid-column>`;
    return tempCode;
}

export function K_LINE_CHART_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-line-chart style="${stringStyle}" ${options} ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-line-chart>`;
    return tempCode;
}

export function K_BAR_CHART_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-bar-chart ${options} style="${stringStyle}" ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-bar-chart>`;
    return tempCode;
}

export function K_PIE_CHART_CODE(config, data){
    // 组件Data初始化
    let value = config.model;
    dataInit(value, data.params, data._props);

    // 组件方法、事件初始化
    methodsInit(data, config.methods)
    let events = eventInit(data, config.events)

    // 组件属性初始化
    let options = joinOptions(config);

    // 组件样式添加
    let stringStyle = changeStyle(config.style);

    let tempCode = `<k-pie-chart ${options} style="${stringStyle}" ${joinEventOption(events)} ${joinCustomAttrOption(config)}></k-pie-chart>`;
    return tempCode;
}

/**
 * 布局
 * @param {Object} config 组件
 * @param {Object} data 页面data
 * @returns tempCode
 */
export function LAYOUT_CODE(config, data) {
  let tempCode = '';
  let stringStyle = changeStyle(config.style);
  if(!stringStyle.includes('position')) {
    stringStyle += 'position:relative;'
  }
  tempCode += `<div style="${stringStyle}">`
  return tempCode;
}


/**
 * 自定义控件
 * @param {Object} config 组件
 * @param {Object} data 页面data
 * @returns tempCode
 */
export function K_CUSTOMER_CODE(config, data) {
  let tempCode = null
  let content = "";
  if(config.list && config.list.length > 0){
    config.list.forEach(item => {
      if(item.list && item.list.length > 0) {
        item.list.forEach(v => {
          content += eval(joinFunctionName(v.type, "v, data"));
        })
      }
    });
  }
  // 样式添加
  let stringStyle = changeStyle(config.style);
  stringStyle += 'overflow: hidden;'
  tempCode = `<div style="${stringStyle}">
    ${content}
  </div>`
  return tempCode
}


/**
 * 拼接组件参数(指定属性)
 * @param {*} config
 * @returns
 */
export function joinOptions(config, data){
    let _options = "";
    let component = findComponent(config.type);

    if(config.options){
        if(config.selectOptions && config.selectOptions.length > 0){
            Object.keys(config.options).forEach(key => {
                if(config.selectOptions.indexOf(key) > -1){
                    if(config.options[key] != null && config.options[key] != ""){
                        if(component.options[key].type == String){
                            _options += key + "=" + "\'"+config.options[key]+"\' "
                        } else if(component.options[key].type == Array){
                          if(['k-field-radio', 'k-field-checkbox'].includes(component.type) && key=='dataData') {   // checkbox、radio 中 data-data处理
                            _options += "data-display-field='label' data-value-field='key'"
                          }
                          let _name = "option_" + Math.ceil(Math.random() * 99999);
                          _options += ":" + key + "=" + "\""+ _name +"\" "
                          if(data && data.params){
                              data.params[_name] = config.options[key];
                          }
                        } else {
                          _options += ":" + key + "=" + "\""+config.options[key]+"\" "
                        }
                        // else {
                        //     _options += "@" + key + "=" + "\""+config.options[key]+"\" "
                        // }
                    }
                }
            })
        }
    }
    return _options;
}


function joinCustomAttrOption(config, data){
    if(config && config.customAttrs && config.customAttrs.length > 0){
        return config.customAttrs.join(" ");
    } else {
        return "";
    }
}

function joinEventOption(events){
    let _event = ""
    if(events && events.length > 0){
        events.forEach(event => {
            _event += event.key + "=" + "\""+event.value+"\" "
        })
    }
    return _event;
}

/**
 * 拼接组件参数
 * @param {*} config
 * @returns
 */
 export function getOptionsJson(config){
    let _options = {};
    let component = findComponent(config.type);

    if(config.options){
        if(config.selectOptions && config.selectOptions.length > 0){
            Object.keys(config.options).forEach(key => {
                if(config.selectOptions.indexOf(key) > -1){
                    _options[key] = config.options[key]
                }
            })
        }
    }
    return _options;
}

/**
 * 根据组件类型拼接代码生成方法名
 * @param {*} type
 * @param {*} paramName
 * @returns
 */
function joinFunctionName(type, paramName){
    return type.toUpperCase().replaceAll("-", "_") + "_CODE(" + paramName + ")"
}

/**
 * 组件v-model对象初始化
 * @param {*} model
 * @param {*} params
 */
function dataInit(model, params, props){
    let _params = params;
    if(model){
        let modelArr = model.split(".");
        // 如果props已存在则不初始化data
        if(modelArr.length > 0 && props){
            if(isArray(props) && props.indexOf(modelArr[0]) > -1){
                return;
            } else if(props.hasOwnProperty(modelArr[0])){
                return;
            }
        }
        for(let i=0; i < modelArr.length; i++){
            if(!_params.hasOwnProperty(modelArr[i])){
                if(i == modelArr.length-1 || modelArr.length==1){
                    _params[modelArr[i]] = null;
                } else {
                    _params[modelArr[i]] = {};
                }
            } else if(!_params[modelArr[i]] && i!=modelArr.length-1) {
                _params[modelArr[i]] = {};
            }
            _params = _params[modelArr[i]]
        }
    }
}

function joinOptionsNotBlank(options){
    let _options = "";
    if(options && Object.keys(options).length > 0){
        Object.keys(options).forEach(key => {
            if(options[key]){
                let type = typeof(options[key]);
                if(type == "string"){
                    _options += key + "=" + "\""+ options[key] +"\""
                } else if(type == "number"||type == "boolean"){
                    _options += ":" + key + "=" + "\""+ options[key] +"\""
                } else {
                    console.log(" 暂不支持转换类型 ", key, options[key], type);
                }
            }
        });
    }
    return _options;
}

function methodsInit(data, methods){
    if(methods && methods.length>0){
        methods.forEach(v => {
            data.methods.push(v.body);
        });
    }
}

function eventInit(data, events){
    let _events = [];
    if(events && events.length>0){
        events.forEach(v => {
            data.methods.push(v.body);
            let key = "@" + v.propertyName;
            _events.push({key: key, value: v.functionName});
        });
    }
    return _events;
}


/**
 * 判断对象是否数组
 */
function isArray(object) {
    if (object == null) {
        return false;
    }
    return object != null && typeof object == "object" &&
        object.splice != null && object.join != null && object.length != null;
}

/**
 * 样式对象转换为字符串
 * @param {Object} objStyle
 * @returns {string}
 */
function changeStyle(objStyle) {
  let stringStyle = ''
  objStyle = getStyle(objStyle);      // 添加单位
  for (const key in objStyle) {
    if (Object.hasOwnProperty.call(objStyle, key)) {
      if(objStyle[key] && typeof(objStyle[key]) != 'undefined') {
        stringStyle+= `${(key)}:${objStyle[key]};`
      }
    }
  }
  return stringStyle
}
