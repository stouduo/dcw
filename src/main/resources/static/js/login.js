/**
 * Created by souhiroshisakai on 2017/10/5.
 */
/* 登录输入验证 */
// 登录账号框聚焦边框颜色及为输入提示变化
$('.phone-email').focus(function () {
    $('.phone-email').css({'border-color': 'rgb(0, 154, 255)'});
    $('.err-pe').remove();
    var le = $('.err').children().length;
    if (le == 0) {
        $('.err').css({'display': 'none'});
    }
})
// 登录密码框聚焦边框颜色及为输入提示变化
$('.password').focus(function () {
    $('.password').css({'border-color': 'rgb(0, 154, 255)'});
    $('.err-psw').remove();
    var le = $('.err').children().length;
    if (le == 0) {
        $('.err').css({'display': 'none'});
    }
})
// 找回新密码框聚焦边框颜色及为输入提示变化
$('.new-password').focus(function () {
    $('.new-password').css({'border-color': 'rgb(0, 154, 255)'});
    $('.err-new-psw').remove();
    var le = $('.err').children().length;
    if (le == 0) {
        $('.err').css({'display': 'none'});
    }
})
// 找回再次输入密码框聚焦边框颜色及为输入提示变化
$('.re-password').focus(function () {
    $('.re-password').css({'border-color': 'rgb(0, 154, 255)'});
    $('.err-re-psw').remove();
    var le = $('.err').children().length;
    if (le == 0) {
        $('.err').css({'display': 'none'});
    }
})
// 登录/找回密码账号框失焦验证输入是否符合规则
$('.login .phone-email, .resetPsw .phone-email').blur(function () {
    verifyPE(1);
})
// 注册时，账号框失焦边框颜色变化
$('.register .phone-email').blur(function () {
    $('.phone-email').css({'border-color': '#E6E9ED'});
})

$('#phone_email').blur(function () {

});

// 登录密码框失焦验证是否输入密码
$('.password').blur(function () {
    var psw = $('.password').val();
    verifyPsw(psw, 1);
})
// 找回新密码框失焦验证是否输入密码
$('.new-password').blur(function () {
    var psw = $('.new-password').val();
    verifyPsw(psw, 2);
})
// 找回再次输入密码框失焦验证是否输入密码
$('.re-password').blur(function () {
    var psw = $('.re-password').val();
    verifyPsw(psw, 3);
})
// 点击登录按钮
$("#login").on('submit', function () {
    $('.err p').remove();
    var isRemember = $('#login-box .remember')[0].checked; //是否选中下次自动登录
    var vPe = verifyPE(1);
    var psw = $('.password')[0].value;
    var vPsw = verifyPsw(psw, 1);
    return vPe && vPsw;
});
$.ajaxSetup({
    async: false
});
// 点击去注册按钮
var flag = true;
$('#register').on('submit', function () {
    flag && $('.err p').remove();
    // var psw = $('.password')[1].value;
    // var vPsw = verifyPsw(psw, 1);
    var value = $('#phone_email').val();
    if (verifyPE(2) && flag)
        $.get('/user/signup/validInfo', {tel: value, email: value}, function (data) {
            if (data.code == 0) {
                $('.err').append('<p class="err-pe">电话号码或邮箱已被占用</p>');
                $('.err').css({'display': 'block'});
                flag = false;
            }
        }, 'json');
    return flag;
})
// 找回密码点击修改密码
$('.resetPsw-btn').on('click', function () {
    $('.err p').remove();
    var vPe = verifyPE(1);
    var psw = $('.password').val();
    var vPsw = verifyPsw(psw, 1);
    var newPsw = $('.new-password').val();
    verifyPsw(newPsw, 2);
    var rePsw = $('.re-password').val();
    verifyPsw(rePsw, 3);
    var nPsw = verifyNewPsw(psw, newPsw, rePsw);
    console.log(nPsw)
    if (vPe == true && vPsw == true && nPsw == true) {
        console.log('执行修改密码后续代码')
    }
})
// 找回密码判断与原密码以及两次输入密码
function verifyNewPsw(psw, newPsw, rePsw) {
    if (psw == newPsw) {
        alert('新密码不能与原密码相同')
        return false;
    } else if (newPsw != rePsw) {
        alert('两次输入的新密码不一致')
        return false;
    } else {
        return true;
    }
}
// 验证输入账号是否符合规则
function verifyPE(num) {
    var pe = '';
    // 1登录/找回密码 2注册
    if (num == 1) {
        pe = $('.phone-email')[0].value;
    } else if (num == 2) {
        pe = $('.phone-email')[1].value;
    }
    var phoneMatch = /(1\d{2}\d{8})/;
    var emailMatch = /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
    if (pe.match(phoneMatch) || pe.match(emailMatch)) {
        $('.phone-email').css({'border-color': '#E6E9ED'});
        return true;
    } else if (pe == '') {
        $('.err').append('<p class="err-pe">手机号/邮箱不能为空</p>');
        $('.err').css({'display': 'block'});
        $('.phone-email').css({'border-color': '#FF7466'});
        return false;
    } else {
        $('.err').append('<p class="err-pe">请输入正确的手机号/邮箱</p>');
        $('.err').css({'display': 'block'});
        $('.phone-email').css({'border-color': '#FF7466'});
        return false;
    }
}
// 验证输入的密码是否符合规则
function verifyPsw(psw, num) {
    if (psw == '') {
        if (num == 1) {
            $('.err').append('<p class="err-psw">密码不能为空</p>');
            $('.password').css({'border-color': '#FF7466'});
        } else if (num == 2) {
            $('.err').append('<p class="err-new-psw">新密码不能为空</p>');
            $('.new-password').css({'border-color': '#FF7466'});
        } else if (num == 3) {
            $('.err').append('<p class="err-re-psw">再次输入密码不能为空</p>');
            $('.re-password').css({'border-color': '#FF7466'});
        }
        $('.err').css({'display': 'block'});
        return false;
    } else {
        if (num == 1) {
            $('.password').css({'border-color': '#E6E9ED'});
        } else if (num == 2) {
            $('.new-password').css({'border-color': '#E6E9ED'});
        } else if (num == 3) {
            $('.re-password').css({'border-color': '#E6E9ED'});
        }

        return true;
    }
}
// 点击去注册账号
$('#no-id a').on('click', function () {
    $('#login-box .login').css('display', 'none');
    $('#login-box .register').css('display', 'block');
    $('#no-id').css('display', 'none');
    $('#had-id').css('display', 'block');
    $('.err').hide();
})
// 已注册去登录
$('#had-id a').on('click', function () {
    $('#login-box .login').css('display', 'block');
    $('#login-box .register').css('display', 'none');
    $('#no-id').css('display', 'block');
    $('#had-id').css('display', 'none');
    $('.err').hide();
})
// 注册时切换邮箱和电话按钮
$('.switch').on('click', function () {
    var classtxt = $('.switch')[0].classList[1];
    if (classtxt == 'phone') {
        console.log(1)
        $('#phone_email').attr("name", "email");
        $('.switch').addClass('email');
        $('.switch').removeClass('phone').html('切换为手机号');
        $('.register .name').html('请输入你的邮箱');
    } else if (classtxt == 'email') {
        console.log(2)
        $('#phone_email').attr("name", "tel");
        $('.switch').addClass('phone');
        $('.switch').removeClass('email').html('切换为邮箱');
        $('.register .name').html('请输入你的手机号');
    }
})

