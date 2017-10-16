/**
 * Created by souhiroshisakai on 2017/10/8.
 */
// 回车搜索
$('.top .search input').keyup(function (e) {
    if (e.keyCode == 13 || e.keyCode == 108) {
        console.log('执行搜索代码');
    }
})

// 点击头像
$('.top-right .head').on('click', function (e) {
    $('.top-right .more').toggle();
    e.stopPropagation();
})

// 点击也面其余位置隐藏
$('body, pages').on('click', function () {
    $('.top-right .more').hide();
})
// 阻止点击信息框的冒泡事件
$('.top-right .more').on('click', function (e) {
    e.stopPropagation();
})