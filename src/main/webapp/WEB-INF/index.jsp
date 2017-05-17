<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>

</head>
<body>
    <div id="weichatlogin"></div>
    <script type="text/javascript">
        var obj = new WxLogin({
            id:"weichatlogin",
            appid: "wx8f3bdad3dc9b9e85",
            scope: "snsapi_login",
            redirect_uri: "http://believesun.win/weichat/callback",
            state: "weichattest",
            style: "white",
            href: ""
        });
    </script>
</body>
</html>