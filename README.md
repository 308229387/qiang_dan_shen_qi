1.URL地址:
  线上地址:zhaobiao.58.com
  线下地址:http://192.168.118.132:9090/
  沙箱地址:http://192.168.118.91:9090/
  
2.账号:
  线上账号/沙箱账号是一样的
        用户名:琼nl 密码:qwer123
        用户名:zzyvz_p4 密码qwer123
        用户名:bigbang120/130 密码:931625046
  
  线下账号
        用户名:bigbang1/2/3 密码:aaa123/aaa111(两个选)
  
  
3.代码
        各自界面的ViewModel进行了各自业务Bean的注册----GrabListViewModel进行了主列表页的业务注册
        各自界面的Adapter进行了各自业务类型到adapter类型的转化----PopAdapter进行了主列表也的业务类型到adapter的类型的转化
        
4.以后每个线上版本都打一个branch,方便回溯

5.打包渠道
    目前分为
            百度市场:baidu
            应用宝高速:yingyongbao
            应用宝本地:yingyongbao_local
            360:360
            本地:fir
            
            
6.每次打完包后的工具上线前要使用android sdk中的zipalign工具进行apk的优化
http://www.cnblogs.com/xirihanlin/archive/2010/04/12/1710164.html