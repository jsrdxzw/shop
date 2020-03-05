window.app = {
    /* 开发环境 */
    // serverUrl: "http://localhost:8088",                                   // 接口服务接口地址
    // paymentServerUrl: "http://192.168.1.3:8089",                            // 支付中心服务地址
    // shopServerUrl: "http://localhost:8080/foodie-shop/",                  // 门户网站地址
    // centerServerUrl: "http://localhost:8080/foodie-center/",              // 用户中心地址
    // cookieDomain: "localhost",                                                       // cookie 域

    /* 生产环境 */
    serverUrl: "http://api.z.jsrdxzw.com",                      // 接口服务接口地址
    paymentServerUrl: "http://payment.t.mukewang.com/foodie-payment",       // 支付中心服务地址
    shopServerUrl: "http://shop.z.jsrdxzw.com/foodie-shop",                            // 门户网站地址
    centerServerUrl: "http://center.z.jsrdxzw.com/foodie-center",                        // 用户中心地址
    cookieDomain: "z.jsrdxzw.com;",

    ctx: "/foodie-shop",

    getCookie: function (cname) {
        const name = cname + "=";
        const ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            // console.log(c)
            while (c.charAt(0) === ' ') c = c.substring(1);
            if (c.indexOf(name) !== -1) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    },

    goErrorPage() {
        window.location.href = "http://www.imooc.com/error/noexists";
    },

    setCookie: function (name, value) {
        const Days = 365;
        const exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        let cookieContent = name + "=" + encodeURIComponent(value) + ";path=/;";
        if (this.cookieDomain != null && this.cookieDomain !== '') {
            cookieContent += "domain=" + this.cookieDomain;
        }
        document.cookie = cookieContent + cookieContent;
        // document.cookie = name + "="+ encodeURIComponent (value) + ";path=/;domain=" + cookieDomain;//expires=" + exp.toGMTString();
    },


    deleteCookie: function (name) {
        document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    },

    getUrlParam(paramName) {
        const reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");    //构造一个含有目标参数的正则表达式对象
        const r = window.location.search.substr(1).match(reg);            //匹配目标参数
        if (r != null) return decodeURI(r[2]);
        return null;             //返回参数值
    },

    /**
     * 构建购物车商品对象
     */
    ShopcartItem: function (itemId, itemImgUrl, itemName, specId, specName, buyCounts, priceDiscount, priceNormal) {
        this.itemId = itemId;
        this.itemImgUrl = itemImgUrl;
        this.itemName = itemName;
        this.specId = specId;
        this.specName = specName;
        this.buyCounts = buyCounts;
        this.priceDiscount = priceDiscount;
        this.priceNormal = priceNormal;
    },

    addItemToShopcart(pendingItem) {
        // 判断有没有购物车，如果没有购物车，则new 一个购物车list
        // 如果有购物车，则直接把shopcartItem丢进去
        const foodieShopcartCookie = this.getCookie("shopcart");
        let foodieShopcart = [];
        if (foodieShopcartCookie != null && foodieShopcartCookie !== "" && foodieShopcartCookie !== undefined) {
            const foodieShopcartStr = decodeURIComponent(foodieShopcartCookie);
            foodieShopcart = JSON.parse(foodieShopcartStr);

            let isHavingItem = false;
            // 如果添加的商品已经存在与购物车中，则购物车中已经存在的商品数量累加新增的
            for (let i = 0; i < foodieShopcart.length; i++) {
                const tmpItem = foodieShopcart[i];
                const specId = tmpItem.specId;
                if (specId === pendingItem.specId) {
                    isHavingItem = true;
                    tmpItem.buyCounts = tmpItem.buyCounts + pendingItem.buyCounts;
                    // 删除主图在数组中的位置
                    foodieShopcart.splice(i, 1, tmpItem);
                }
            }
            if (!isHavingItem) {
                foodieShopcart.push(pendingItem);
            }
        } else {
            foodieShopcart.push(pendingItem);
        }

        this.setCookie("shopcart", JSON.stringify(foodieShopcart));
    },

    /**
     * 获得购物车中的数量
     */
    getShopcartItemCounts() {
        // 判断有没有购物车，如果没有购物车，则new 一个购物车list
        // 如果有购物车，则直接把shopcartItem丢进去
        const foodieShopcartCookie = this.getCookie("shopcart");
        let foodieShopcart = [];
        if (foodieShopcartCookie != null && foodieShopcartCookie !== "" && foodieShopcartCookie !== undefined) {
            const foodieShopcartStr = decodeURIComponent(foodieShopcartCookie);
            foodieShopcart = JSON.parse(foodieShopcartStr);
        }
        return foodieShopcart.length;
    },

    /**
     * 获得购物车列表
     */
    getShopcartList() {
        // 判断有没有购物车，如果没有购物车，则new 一个购物车list
        // 如果有购物车，则直接把shopcartItem丢进去
        const foodieShopcartCookie = this.getCookie("shopcart");
        let foodieShopcart = [];
        if (foodieShopcartCookie != null && foodieShopcartCookie !== "" && foodieShopcartCookie !== undefined) {
            const foodieShopcartStr = decodeURIComponent(foodieShopcartCookie);
            foodieShopcart = JSON.parse(foodieShopcartStr);
        }
        return foodieShopcart;
    },

    checkMobile(mobile) {
        const myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        return myreg.test(mobile);

    },

    checkEmail(email) {
        const myreg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
        return myreg.test(email);

    },
};
