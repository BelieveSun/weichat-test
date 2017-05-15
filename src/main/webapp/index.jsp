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
            appid: "wx8901463f7fe8e0a9",
            scope: "snsapi_base",
            redirect_uri: "http%3a%2f%2fsun.gj.ngrok.cc%2fweichat%2fcallback",
            state: "csds",
            style: "white",
            href: "https://passport.csdn.net/css/replace-wx-style.css"
        });
    </script>
</body>
</html>