<template>
  <textarea ref="code" name="code">
  </textarea>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";

  import '@/libs/codemirror/lib/codemirror.css';
  import '@/libs/codemirror/theme/cobalt.css';
  import '@/libs/codemirror/addon/display/fullscreen.css';
  import CodeMirror from 'codemirror';

  export default {
    name: 'KFieldCode',
    mixins: [props(), event()],
    props: {

    },
    data() {
      return {
        editor: {}
      }
    },
    created() {
      setTimeout(() => {
        this.editor = CodeMirror.fromTextArea(this.$refs.code, {
          lineNumbers: true,
          styleActiveLine: true,
          matchBrackets: true,
          theme: "cobalt",
          indentUnit: 0,
          readOnly: true, //只读
          showCursorWhenSelecting: true,
          extraKeys: {
            "F11": function(cm) {
              cm.setOption("fullScreen", !cm.getOption("fullScreen"));
            },
            "Esc": function(cm) {
              if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
            }
          }
        });

        let value;
        if (this.value) {
          value = this.value;
        } else if (this.$slots.default[0].text) {
          value = this.$slots.default[0].text;
        } else if (this.$slots.default[0].children) {
          value = this.$slots.default[0].children[0].text;
        }

        this.editor.setValue(value);
      }, 10);
    },
    mounted: function() {

    },
    methods: {}
  };
</script>
