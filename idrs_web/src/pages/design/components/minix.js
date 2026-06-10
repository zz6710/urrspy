import { getOptionsJson } from '../utils/componentTempleCode'

export default {
    data () {
        return {
            options: {},
        }
    },
    watch: {
        element: {
          handler: function(newVal, oldVal){
            this.options = getOptionsJson(this.element);
          },
          deep: true
        }
      }
}