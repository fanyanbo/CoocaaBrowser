构建酷开浏览器

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
