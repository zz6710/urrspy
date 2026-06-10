//对后代节点广播实践
function broadcast(componentName, eventName, params) {
  this.$children.forEach(child => {
    var name = child.$options.name;
    if (name === componentName) {
      child.$emit.apply(child, [eventName].concat(params));
    } else {
      broadcast.apply(child, [componentName, eventName].concat([params]));
    }
  });
}

function getChildrens(componentName, childrens) {
  this.$children.forEach(child => {
    var name = child.$options.name;
    if (name === componentName) {
      childrens.push(child);
    } else {
      getChildrens.apply(child, [componentName, childrens]);
    }
  });
}


export default function() {
  return {
    methods: {
      //冒泡事件
      dispatch(componentName, eventName, params) {
        var parent = this.$parent || this.$root;
        var name = parent.$options.name;

        while (parent && (!name || name !== componentName)) {
          parent = parent.$parent;

          if (parent) {
            name = parent.$options.name;
          }
        }
        if (parent) {
          parent.$emit.apply(parent, [eventName].concat(params));
        }
      },
      //对后代节点广播实践
      broadcast(componentName, eventName, params) {
        broadcast.call(this, componentName, eventName, params);
      },
      //根据控件名获取父组件
      getParent(componentName) {
        var parent = this.$parent || this.$root;
        var name = parent.$options.name;

        while (parent && (!name || name !== componentName)) {
          parent = parent.$parent;

          if (parent) {
            name = parent.$options.name;
          }
        }

        return parent;
      },
      //根据引用名获取父引用对象
      getParentRef(ref) {
        var parent = this.$parent || this.$root;
        var $ref = parent.$refs[ref];

        while (parent && !$ref) {
          parent = parent.$parent;

          if (parent) {
            $ref = parent.$refs[ref];
          }
        }

        return $ref;
      },
      //根据控件名获取子组件列表
      getChildrens(componentName) {
        let childrens = [];
        getChildrens.call(this, componentName, childrens);
        return childrens;
      }
    }
  }
};
