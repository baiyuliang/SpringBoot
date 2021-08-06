$(function () {
    zylVerCode();
});

function zylVerCode(len) {
    len = len || 4;
    let $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
    let maxPos = $chars.length;
    let zylCode = '';
    for (let i = 0; i < len; i++) {
        zylCode += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    $(".zylVerCode").html(zylCode);
}