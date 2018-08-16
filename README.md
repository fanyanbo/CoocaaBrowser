# CoocaaBrowser
它是酷开浏览器，内置于酷开系统中，由于广电限制，目前在酷开系统中并没有入口，只能外接键盘，按F8启动。
它除了具备通用浏览器功能外，还可加载混合开发的Web页面，为Web页面提供酷开系统的能力，满足基于酷开系统的运营需求。

# ccinit配置
```
{
    "project":{
        "repository" : "https://github.com/fanyanbo/CoocaaBrowser.git",
        "branch" : "master"
    },
    "cc": [
        {
            "directory": "sdk",
            "repositories": [
                {
                    "name":"Framework",
                    "repository": "ssh://source.skyworth.com/skyworth/CoocaaOS/Framework",
                    "branch": "CCOS/Rel6.2"
                },
                {
                    "name":"AF_SkyLogSDK",
                     "repository": "http://source.skyworth.com:3000/MoneyApps/AF_LogSDK.git",
                     "branch": "master"
                },
                {
                    "name":"AB_CommonUISDK",
                     "repository": "http://source.skyworth.com:3000/MoneyApps/AB_CommonUISDK.git",
                     "branch": "feature/Q6"
                },
                {
                    "name":"CoocaaOSWebViewSDK",
                     "repository": "https://github.com/fanyanbo/CoocaaOSWebViewSDK.git",
                     "branch": "feature-20180309"
                },
                {
                    "name":"AJ_ThemeSDK",
                     "repository": "http://source.skyworth.com:3000/MoneyApps/AJ_ThemeSDK.git",
                     "branch": "master"
                }
            ]
        }
    ]
}
```
