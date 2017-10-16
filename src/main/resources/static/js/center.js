/**
 * Created by souhiroshisakai on 2017/10/8.
 */
// 点击个人中心左侧筛选列表
$('.page-center .left .item').on('click', function () {
    $(this).siblings().removeClass('on');
    $(this).addClass('on');
    var index = $(this).index()
    if (index == 0) {
        $('.page-center .basic').siblings().hide();
        $('.page-center .basic').show();
    } else if (index == 1) {
        $('.page-center .setting').siblings().hide();
        $('.page-center .setting').show();
    } else if (index == 2) {
        $('.page-center .log').siblings().hide();
        $('.page-center .log').show();
    }
})
// 修改昵称
$('.page-center .setting .new-name').on('click', function () {
    var layerIdex = layer.open({
        type: 1,
        title: '更改用户名',
        area: ['600px'],
        shadeClose: true,
        btn: ['保存'],
        content: $('.change-nickname'),
        yes: function () {
            $.get('/user/signup/validInfo', {tel: value, email: value}, function (data) {
                if (data.code == 0) {
                    $('.err').append('<p class="err-pe">电话号码或邮箱已被占用</p>');
                    $('.err').css({'display': 'block'});
                    flag = false;
                }
            }, 'json');
            var newName = $('.change-nickname input').val();
            if (length < 2 || length > 15) {
                $('.change-nickname').append('<p class="err-change">昵称不能为空昵称昵称长度应在2~15个字之间</p>')
            } else {
                console.log('这里执行保存代码')
                layer.close(layerIdex);
            }
        }
    })
})

// 修改昵称
$('.page-center .setting .new-name').on('click', function () {
    var layerIdex = layer.open({
        type: 1,
        title: '更改邮箱',
        area: ['600px'],
        shadeClose: true,
        btn: ['激活邮箱'],
        content: $('.change-email'),
        yes: function () {

            var newEmail = $('.change-email input').val()
            if (length < 2 || length > 15) {
                $('.change-email').append('<p class="err-change">昵称不能为空昵称昵称长度应在2~15个字之间</p>')
            } else {
                console.log('这里执行保存代码')
                layer.close(layerIdex);
            }
        }
    })
})
// 修改密码
$('.page-center .setting .password').on('click', function () {
    var layerIndex = layer.open({
        type: 1,
        title: '修改密码',
        area: ['600px'],
        shadeClose: true,
        btn: ['保存修改', '取消'],
        content: $('.change-password'),
        yes: function () {
            $('.err-change').remove();
            var psw = $('.change-password .password').val();
            var newPsw = $('.change-password .new-psw').val();
            var rePsw = $('.change-password .re-psw').val();
            if (psw != '' && newPsw != '' && rePsw != '') {
                if (newPsw != rePsw) {
                    $('.change-password').append('<p class="err-change">两次输入的新密码不一致</p>');
                } else if (psw == newPsw) {
                    $('.change-password').append('<p class="err-change">输入的原密码与新密码不能相同</p>');
                } else {
                    console.log('这里执行修改密码代码');
                    layer.close(layerIndex);
                }
            } else {
                if (psw == '') {
                    $('.change-password').append('<p class="err-change">原密码不能为空</p>');
                } else if (newPsw == '') {
                    $('.change-password').append('<p class="err-change">新密码不能为空</p>');
                } else if (rePsw == '') {
                    $('.change-password').append('<p class="err-change">新密码确认不能为空</p>');
                }
            }
        },
        btn2: function () {
            layer.close(layerIndex);
        }
    })
})
// 选择日志类型
$('.log .log-check .item').on('click', function () {
    $(this).siblings().removeClass('on');
    $(this).addClass('on');
    var index = $(this).index();
    if (index == 0) {
        $('.page-center .log-box .log-sheets').show();
        $('.page-center .log-box .log-mail').hide();
    } else if (index == 1) {
        $('.page-center .log-box .log-sheets').hide();
        $('.page-center .log-box .log-mail').show();
    }
})
// 短信日志选项
$('.log .mail-items li').on('click', function () {
    $(this).siblings().removeClass('on');
    $(this).addClass('on');
    var index = $(this).index();
    $('.page-center .log-box .mail-table' + (index + 1)).siblings().hide();
    $('.page-center .log-box .mail-table' + (index + 1)).show();
})
// 搜索框
$('.page-center .mail-box .search input').keyup(function (e) {
    if (e.keyCode == 13 || e.keyCode == 108) {
        var val = $(this).val();
        var domClass = $(this)[0].className;
        var index = domClass.charAt(domClass.length - 1);
        if (index == 1) {
            // 短信推送
        } else if (index == 2) {
            // 短信验证
        } else if (index == 3) {
            // 邮件推送
        } else if (index == 4) {
            // API短信
        }
    }
})