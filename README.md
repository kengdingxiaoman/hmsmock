# hmsmock
模拟卫士通加密机—SJL05

用于POS交易时本地调试使用，线上环境使用的是加密机，但一般都对IP会有限制，所以本地调试时无法使用加密机

hmsmock编译成jar包后，可以本地启动，启动后会模拟加密机，只需把加密机的地址和端口改为hmsmock

## 工程结构
- constants：定义常量和枚举
- exception：定义异常和错误原因
- sample：一些例子
- server: server类，监听请求，分发至具体处理类
- service：具体命令的实际处理类
- util：各种工具类，也包含各种算法的实现
- 类 ServerStart：启动类

## 支持的命令

支持变种机密的基本命令，也就是 d1 开头的命令

| 命令 | 解释 |
| :-: | - |
| 41 | 解密区域密钥加密的key|
| d1 02 | 将一个密钥加密的key转换为另一个密钥加密的结果 |
| d1 07 | 生成密钥 |
| d1 12 | 加密明文 |
| d1 14 | 解密密文 |
| d1 22 | 生成 pinblock |
| d1 24 | 转换 pinblock |
| d1 26 | 解密 pinblock |
| d1 32 | 结算 mac 值 |

## 支持的 mac 算法
- ANSI9.9
- ANSI9.19
- ECB

## 工程启动
将工程编译后会生成 jar， 如果是在linux环境，那使用命令运行jar：

nohup java -jar XXXXX.jar &

日志会记录到nohup文件中。

同时需要在同一目录下放置 lmk 文件，内容类似： 5986bfbd461beb417a1b4e4f4c077e22

用来生成最终的 LMK

## 大体流程说明

![hmsmock](http://ouruoqnrh.bkt.clouddn.com/hmsmock%E6%B5%81%E7%A8%8B.png)
