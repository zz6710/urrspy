export const styleData = [
    { key: 'left', label: 'x 坐标' },
    { key: 'top', label: 'y 坐标' },
    { key: 'width', label: '宽' },
    { key: 'height', label: '高' },
    { key: 'color', label: '颜色' },
    { key: 'backgroundColor', label: '背景色' },
    { key: 'borderWidth', label: '边框宽度' },
    { key: 'borderStyle', label: '边框风格' },
    { key: 'borderColor', label: '边框颜色' },
    { key: 'borderRadius', label: '边框半径' },
    { key: 'fontSize', label: '字体大小' },
    { key: 'fontWeight', label: '字体粗细' },
    { key: 'lineHeight', label: '行高' },
    { key: 'letterSpacing', label: '字间距' },
    { key: 'textAlign', label: '左右对齐' },
    { key: 'verticalAlign', label: '上下对齐' },
    { key: 'opacity', label: '透明度' },
]


/**
 * 组件样式参数转换
 * @param {*} style
 * @param {*} excludes
 * @param {*} includes
 * @returns
 */
export function getStyle(style, excludes = [], includes = [], ) {
    if(!style){
        return {};
    }
    const needUnit = [
        'fontSize',
        'width',
        'height',
        'top',
        'left',
        "right",
        "bottom",
        'borderWidth',
        'letterSpacing',
        'borderRadius',
    ]
    const result = {}
    Object.keys(style).forEach(key => {
        // excludes不存在 && includes中存在或为空
        if (!excludes.includes(key) && (!includes || includes.length==0 || includes.includes(key))) {
            if (key != 'rotate') {
                result[key] = style[key]
                if(style[key] && typeof(style[key]) != 'undefined' && style[key].toString().includes('%')) {
                } else if (needUnit.includes(key)) {   // 尺寸单位增加px  TODO 百分比未处理
                    result[key] += 'px'
                }
            } else {
                result.transform = key + '(' + style[key] + 'deg)'
            }
        }
    })
    return result
}
