;(function () {
    $.getJSON('/js/address.json', function (address) {
        var form = layui.form;
        var $detail = $('#user-detail-address');
        var province, city, block, details = '';
        var $province = $("#province");
        var $city = $("#city");
        var $block = $("#block");
        var getProvinceList = function () {
            var list = [];
            for (var i = 0, l = address.length; i < l; i++) {
                if ('0' == address[i].parentId) {
                    list.push($.extend({}, address[i]));
                }
            }
            return list;
        };
        /**
         * 获取下一级地址目录
         * @param nowDistrictAreaName 当前地区的名字
         */
        var getNextDistrictList = function (nowDistrictAreaName) {
//                    {"areaCode":"1","areaName":"北京","areaPinYin":"bei jing","parentId":"0"}
            var list = [], id = '';
            for (var i = 0, l = address.length; i < l; i++) {
                if (address[i].areaName === nowDistrictAreaName) {
                    id = address[i].areaCode;
                }
            }
            for (var i = 0, l = address.length; i < l; i++) {
                if (id === address[i].parentId) {
                    list.push($.extend({}, address[i]));
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
        var createOptions = function (addressList, defaultText) {
            var template = '<option value="q_MyValue">q_MyText</option>';
            var html = '<option value="-1" >' + (defaultText || "--请选择--") + '</option>';
            for (var i = 0, l = addressList.length; i < l; i++) {
                html += template.replace("q_MyValue", addressList[i].areaName).replace("q_MyText", addressList[i].areaName);
            }
            return html;
        }
        var selectHandler = function (data, level) {
            window.data = data;
            var areaName = data.value;
            var list = getNextDistrictList(areaName);
            var html = "";
            var $select = null;
            if (!level || level == 0) {
                html = createOptions(list, "市");
                $select = $city;
            } else {
                html = createOptions(list, "市区/县");
                $select = $block;
            }
            $select.html(html);
            form.render();
        };

        var init = function () {
            var list = getProvinceList();
            var html = createOptions(list, "省/自治区/直辖市");
            $province.html(html);
            form.render();
        };
        $detail.blur(function () {
            details = this.value;
        });
        form.on('select(province)', function (data) {
//                    alert(data)
            province = data.value;
            selectHandler(data, 0);
        });
        form.on('select(city)', function (data) {
//                    alert(data)
            city = data.value;
            selectHandler(data, 1);
        });
        form.on('select(block)', function (data) {
//                    alert(data)
            block = data.value;
        });
        init();
    });

}());