- 先去http://source.skyworth.com:3000/Web-X/ProjectInit.git下载工程初始化文件
- 修改并确认cc.int文件配置的仓库是否正确
- 执行gradle  ccinit命令，初始化源代码
- 将PowerWebViewSDK.rar解压拷贝到工程cc/sdk/目录下

-  cc.init配置

{
    "project":{
        "repository": "http://source.skyworth.com:3000/Web-X/CoocaaBrowser.git",
        "branch": "master"
    },
    "cc": [
        {
            "directory": "sdk",
            "repositories": [
                {
                    "repository": "ssh://source.skyworth.com/skyworth/CoocaaOS/Framework",
                    "branch": "CCOS/Rel6.2"
                },
                {
                    "name":"AB_CommonUISDK",
                    "repository": "http://source.skyworth.com:3000/MoneyApps/AB_CommonUISDK.git",
                    "branch": "feature/Q6"
                },
                {
                    "name":"AJ_ThemeSDK",
                    "repository": "http://source.skyworth.com:3000/MoneyApps/AJ_ThemeSDK.git",
                    "branch": "master"
                }
            ]
        },
        {
            "directory": "app",
            "repositories": [
                {
                    "repository": "ssh://source.skyworth.com/skyworth/CoocaaOS/SkyworthApp/SysApp/SkyBrowser",
                    "branch": "CCOS/Rel6.0"
                }
            ]
        }
    ]
}

