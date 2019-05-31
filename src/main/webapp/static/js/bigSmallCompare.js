/**
 * 校外客户信息列表
 */

//设置全局表单提交格式
Vue.http.options.emulateJSON = true;

// Vue实例
var vm = new Vue({
    el: '#app',
    data() {
        return {
            //element-ui的table需要的参数必须是Array类型的
            odds: [
            ],
            bigSmall: [
            ],
            createTime: this.getCreateTime(),

            //编辑表
            editor: {
                title: '',
                price: '',
                image: '',
                category: '',
                brand: '',
                seller: ''
            },
            winCount:0,
            waterCount:0,
            lostCount:0,
            //添加dialog
            showSave: false,
            //编辑dialog
            showEditor: false,

            //条件查询单独封装的对象
            searchEntity: {},

            //checkbox选择的行中所有数据，将会放入multipartSelection数组中
            multipleSelection: [],
            selectIds: [], //被checkbox选择的id值，用于批量删除
            count: 0, //tag栏，此项那是checkbox选择了几行

            //分页选项
            pageConf: {
                //设置一些初始值(会被覆盖)
                pageCode: 1, //当前页
                pageSize: 6, //每页显示的记录数
                totalPage: 12, //总记录数
                pageOption: [6, 10, 20], //分页选项
            },

            loading: {},
            activeIndex: '2', //默认激活
            tableHeight: window.innerHeight -290,
        }
    },
    methods: {
        /**
         * loading加载动画
         */
        loadings() {
            this.loading = this.$loading({
                lock: true,
                text: '拼命加载中',
                spinner: 'el-icon-loading',
            });
            setTimeout(() => {
                this.loading.close();
            }, 20000000);
        },

        /**
         * Public method
         */
        // 获取所有数据
        getAllBigSmallData() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/getAllBigSmallData.do').then(result => {
                this.bigSmall = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },
        // 已结算数据
        getIsEneBigSmallData() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/getBigSmallCompareDataBy.do?isEnd=1').then(result => {
                this.bigSmall = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });
            this.$http.get('bigSmall/countBigSmallCompareDataBy.do?isEnd=1').then(result => {
                this.winCount = result.body.winCount;
            this.waterCount = result.body.waterCount;
            this.lostCount = result.body.lostCount;
            this.loading.close(); //数据更新成功就手动关闭动画
        });

        },
        // 未结算数据
        getIsNotEneBigSmallData() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/getBigSmallCompareDataBy.do?isEnd=0').then(result => {
                this.bigSmall = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },

        // 购买，结算数据
        getBuyIsEneBigSmallData() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/getBigSmallDataBy.do?isEnd=1&isBuy=1').then(result => {
                this.bigSmall = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画
        });
            this.$http.get('bigSmall/countBigSmallDataBy.do?isEnd=1&isBuy=1').then(result => {
                this.winCount = result.body.winCount;
                this.waterCount = result.body.waterCount;
                this.lostCount = result.body.lostCount;
            this.loading.close(); //数据更新成功就手动关闭动画
        });

        },
        // 购买，未结算数据
        getBuyIsNotEneBigSmallData() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/getBigSmallDataBy.do?isEnd=0&isBuy=1').then(result => {
                this.bigSmall = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },

        // 购买，结算数据（公司）
        getBuyIsEneBigSmallDataBy(company) {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/getBigSmallDataBy.do?isEnd=1&isBuy=1&company='+company).then(result => {
                this.bigSmall = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画
        });
            this.$http.get('bigSmall/countBigSmallDataBy.do?isEnd=1&isBuy=1&company='+company).then(result => {
                this.winCount = result.body.winCount;
            this.waterCount = result.body.waterCount;
            this.lostCount = result.body.lostCount;
            this.loading.close(); //数据更新成功就手动关闭动画
        });

        },
        // 购买，未结算数据
        getBuyIsNotEneBigSmallDataBy(company) {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/getBigSmallDataBy.do?isEnd=0&isBuy=1&company='+company).then(result => {
                this.bigSmall = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },

        //规律计算
        calculationResult() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('bigSmall/calculationResult.do').then(result => {
                this.getAllBigSmallData();
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },
        //数据同步+购买结算
        syncDataFromNetIncrement() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('sync/syncDataFromNetIncrement.do').then(result => {
                this.getAllBigSmallData();
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },


        //刷新列表
        reloadList() {
            console.log("totalPage:" + this.pageConf.totalPage + ",pageSize:" + this.pageConf.pageSize + ",:pageCode:" + this.pageConf.pageCode);
            this.search(this.pageConf.pageCode, this.pageConf.pageSize);
        },
        //条件查询
        search(pageCode, pageSize) {
            this.loadings();
            this.$http.post('/goods/findByConPage.do?pageSize=' + pageSize + '&pageCode=' + pageCode, this.searchEntity).then(result => {
                console.log(result);
                this.goods = result.body.rows;
                this.pageConf.totalPage = result.body.total;
                this.loading.close(); //数据更新成功就手动关闭动画
            });

        },
        //条件查询
        insertLastYz(inputDateStr) {
            this.loadings();
            this.$http.get('odds/insertLastYz.do?inputDateStr=' + inputDateStr, this.searchEntity).then(result => {
            this.loading.close(); //数据更新成功就手动关闭动画
        });

        },
        // 获取所有数据
        findAll() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('odds/getLastYz.do').then(result => {
                this.odds = result.body;
                this.loading.close(); //数据更新成功就手动关闭动画

        });

        },
        // 获取所有数据
        findAllWithNoFresh() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('odds/getLastYzAll.do').then(result => {
                this.odds = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },
        // 获取已完成数据
        findFinish() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('odds/getLastYzFinish.do').then(result => {
                this.odds = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },// 获取已完成数据7天
        findFinish7() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('odds/getLastYzFinish7.do').then(result => {
                this.odds = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },// 获取已完成数据
        findFinish30() {
            this.loadings();
            this.createTime = this.getCreateTime();
            this.$http.get('odds/getLastYzFinish30.do').then(result => {
                this.odds = result.body;
            this.loading.close(); //数据更新成功就手动关闭动画

        });

        },




        // 获取所有数据
        getCreateTime() {
            var date = new Date();
            var y = date.getFullYear();
            var MM = date.getMonth() + 1;
            MM = MM < 10 ? ('0' + MM) : MM;
            var d = date.getDate();
            d = d < 10 ? ('0' + d) : d;
            var h = date.getHours();
            h = h < 10 ? ('0' + h) : h;
            var m = date.getMinutes();
            m = m < 10 ? ('0' + m) : m;
            var s = date.getSeconds();
            s = s < 10 ? ('0' + s) : s;
            return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
        },//刷新列表
        winAdd() {
            this.winCount = this.winCount + 1
        },
        winDec() {
            this.winCount = this.winCount - 1
        },
        lostAdd() {
            this.lostCount = this.lostCount + 1
        },
        lostDec() {
            this.lostCount = this.lostCount - 1
        }

    },

    // 生命周期函数
    created() {
        // this.findAll();
        // this.search(this.pageConf.pageCode, this.pageConf.pageSize);
        // this.loadings(); //加载动画
    },

});
