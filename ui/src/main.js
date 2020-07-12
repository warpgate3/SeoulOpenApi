import Vue from 'vue'
import App from './App'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import Paginate from 'vuejs-paginate'
import PulseLoader from 'vue-spinner/src/PulseLoader.vue'

Vue.component('paginate', Paginate)
Vue.component('ring-loader', PulseLoader)
Vue.use(BootstrapVue);
new Vue({
    render: h => h(App),
}).$mount('#app')
