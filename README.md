# AppDownloadUpdeta
App升级更新，兼容Android6.0  7.0 8.0


## Android引入说明
```Java

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
dependencies {
	        implementation 'com.github.zhx3511:AppDownloadUpdeta:0.0.1'
	}
```
## 使用说明

```java
new UpdateApp.VersionInfo(this)
                .setUrl("http://gdown.baidu.com/data/wisegame/f170a8c78bcf9aac/QQ_818.apk")
                .setNewVersion("1.1.0")
                .build();
                
```


## API
 方法名 | 是否必填 | 参数类型 | 说明 
 ---|---|---|---|
 setUrl | 是 | String | 更新下载apk的地址
 setNewVersion | 是 | String | 最新版本
 setRemark | 否 | String | 版本升级备注
 setForceUpdeta | 否 | boolean | 是否强制更新
 setDownload_dialog_titile | 否 | String | 设置更新对话框标题
 setDownload_dialog_content | 否 | String | 设置更新对话框内容
 setDownload_dialog_but_no | 否 | String | 设置更新对话框取消按钮文案
 setDownload_dialog_but_ok | 否 | String | 设置更新对话框更新按钮文案
 
 ## 
