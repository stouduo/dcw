window.userAddress = '';//在JS中存储用户完整地址
;(function () {
    $.getJSON('/js/address.json', function (address) {
        var province,city,block,details='';
        var $province = $(".province");
        var getProvinceList = function () {
            var list = [];
            for(var i=0,l=address.length;i<l;i++){
                if('0' ==address[i].parentId){
                    list.push($.extend({},address[i]));
                }
            }
            return list;
        };
        /**
         * 获取下一级地址目录
         * @param nowDistrictAreaName 当前地区的名字
         */
        var getNextDistrictList = function (nowDistrictAreaName) {
            var list = [],id='';
            for(var i=0,l=address.length;i<l;i++){
                if(address[i].areaName === nowDistrictAreaName){
                    id=address[i].areaCode;
                }
            }
            for(var i=0,l=address.length;i<l;i++){
                if(id===address[i].parentId){
                    list.push($.extend({},address[i]));
                }
            }
            return list;
        };
        /***
         * 根据获取的下一级目录列表生成select
         * @param addressList
         * @param defaultText
         * @returns {string}
         */
        var createOptions = function (addressList,defaultText) {
            var template = '<option value="q_MyValue">q_MyText</option>';
            var html = '<option value="" >'+(defaultText || "--请选择--")+'</option>';
            for(var i=0,l=addressList.length;i<l;i++){
                html+= template.replace("q_MyValue",addressList[i].areaName).replace("q_MyText",addressList[i].areaName);
            }
            return html;
        }
        var selectHandler = function (data,level) {
            window.data = data;
            var areaName = data.value;
            var list = getNextDistrictList(areaName);
            var html = "";
            var $select = $(data.elem).parent().next();
            if(!level || level == 0){
                html =createOptions(list, "市");
                $select.next().find(".block").html("<option value=''>市区/县</option>");
                $select = $select.find(".city");
            }else{
                html = createOptions(list,"市区/县");
                $select = $select.find(".block");
            }
            $select.html(html);
            form.render();
        };

        var init = function () {
            var list = getProvinceList();
            var html = createOptions(list,"省/自治区/直辖市");
            $province.html(html);
            form.render();
        };
        form.on('select(province)', function(data){
            province = data.value;
            selectHandler(data,0);
        });
        form.on('select(city)', function(data){
            city = data.value;
            selectHandler(data,1);
        });
        form.on('select(block)', function(data){
            block = data.value;
        });
        init();
    });

}());